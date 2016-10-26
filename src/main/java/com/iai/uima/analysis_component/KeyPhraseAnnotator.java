package com.iai.uima.analysis_component;

import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kea.main.KEAKeyphraseExtractor;
import kea.main.KeyPhrase;
import kea.stemmers.FrenchStemmer;
import kea.stemmers.GermanStemmer;
import kea.stemmers.PorterStemmer;
import kea.stemmers.SpanishStemmer;
import kea.stemmers.Stemmer;
import kea.stopwords.Stopwords;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
//import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.dbpedia.spotlight.uima.types.DBpediaResource;
import org.dbpedia.spotlight.uima.types.TopDBpediaResource;

import com.iai.uima.jcas.tcas.KeyPhraseAnnotation;
import com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated;
import com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched;
import com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced;
//import com.iai.uima.jcas.tcas.SentenceAnnotation;
import com.iai.uima.jcas.tcas.SentenceAnnotation;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADJ;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADV;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.N;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.V;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Location;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Organization;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Person;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
//import edu.stanford.nlp.util.CoreMap; 
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class KeyPhraseAnnotator extends JCasAnnotator_ImplBase {

	KEAKeyphraseExtractor ke = new KEAKeyphraseExtractor();
	StanfordCoreNLP pipeline;

	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue = "en")
	private String LANGUAGE;
			
	public static final String PARAM_MODEL_LOCATION = "modelLocation";
	@ConfigurationParameter(name = PARAM_MODEL_LOCATION, mandatory = false)
	private String MODEL_LOCATION;

	public static final String PARAM_KEYPHRASE_RATIO = "ratioOfKeyPhrases";
	@ConfigurationParameter(name = PARAM_KEYPHRASE_RATIO, defaultValue = "50")
	private int KEAPHRASE_RATIO;
	
	public static final String PARAM_MAX_NUMBER_OF_KEYPHRASES = "maxNumberOfKeyphrases";
	@ConfigurationParameter(name = PARAM_MAX_NUMBER_OF_KEYPHRASES, defaultValue = "10")
	private int MAX_NUMBER_OF_KEYPHRASES;
	
	public static final String PARAM_KEA_MAX_NUMBER_OF_KEYPHRASES = "maxNumberOfKeaKeyphrases";
	@ConfigurationParameter(name = PARAM_KEA_MAX_NUMBER_OF_KEYPHRASES, defaultValue = "50")
	private int MAX_NUMBER_OF_KEA_KEYPHRASES;
	
	public static final String PARAM_MAX_NUMBER_OF_WORDS_IN_KEYPHRASE = "maxNumberOfWordsInKeyphrase";
	@ConfigurationParameter(name = PARAM_MAX_NUMBER_OF_WORDS_IN_KEYPHRASE, defaultValue = "10")
	private int MAX_NUMBER_OF_WORDS_IN_KEYPHRASE ;
	
	public static final String PARAM_MIN_NUMBER_OF_WORDS_IN_KEYPHRASE = "minNumberOfWordsInKeyphrase";
	@ConfigurationParameter(name = PARAM_MIN_NUMBER_OF_WORDS_IN_KEYPHRASE, defaultValue = "2")
	private int MIN_NUMBER_OF_WORDS_IN_KEYPHRASE ;
	
	public static final String PARAM_EXTEND_KEYPHRASE_TO_CHUNK = "extendKeyphraseToChunk";
	@ConfigurationParameter(name = PARAM_EXTEND_KEYPHRASE_TO_CHUNK, defaultValue = "true")
	private boolean EXTEND_KEYPHRASE_TO_CHUNK;
	
	public static final String PARAM_EXTEND_TO_PP = "extendToPp";
	@ConfigurationParameter(name = PARAM_EXTEND_TO_PP, defaultValue = "true")
	private boolean EXTEND_TO_PP;
	
	
	public static final String PARAM_NER_FILTER = "nerFilter";
	@ConfigurationParameter(name = PARAM_NER_FILTER, defaultValue = "true")
	private boolean NER_FILTER;
	
	
	public static final String PARAM_DBPEDIA_FILTER = "dbpediaFilter";
	@ConfigurationParameter(name = PARAM_DBPEDIA_FILTER, defaultValue = "true")
	private boolean DBPEDIA_FILTER;
	
	//for determining potential of chunks
	public static final String PARAM_DETERMINE_POS_OF_MANUAL = "determinePosOfManual";
	@ConfigurationParameter(name = PARAM_DETERMINE_POS_OF_MANUAL, defaultValue = "false")
	private boolean DETERMINE_POS_OF_MANUAL;
	
	
	public static final String PARAM_LOAD_MANUAL_KEYPHRASES = "loadManualKeyphrases";
	@ConfigurationParameter(name = PARAM_LOAD_MANUAL_KEYPHRASES, defaultValue = "false")
	private boolean LOAD_MANUAL_KEYPHRASES;
	
	public static final String PARAM_POS_FILTER = "posFilter";
	@ConfigurationParameter(name = PARAM_POS_FILTER, defaultValue = "false")
	private boolean POS_FILTER;
	
	public static final String PARAM_DEPRECATED_FILTERS = "deprecatedFilters";
	@ConfigurationParameter(name = PARAM_DEPRECATED_FILTERS, mandatory = false, defaultValue = " eumssiSetting ")
	private String DEPRECATED_FILTERS;
	
//	@ConfigurationParameter(name = PARAM_DEPRECATED_FILTERS, mandatory = false, defaultValue = " ignoreNoDeprecatedKp ")
	//EVAL_MODE: replaceKeyphrase replaces keyphrase with replacee, enrichKeyphrases adds enrich to keyphrase
//	public static final String PARAM_EVAL_REPLACE_ENRICH = "evalReplaceEnrich";
//	@ConfigurationParameter(name = PARAM_EVAL_REPLACE_ENRICH, defaultValue = "NO")
//	private String EVAL_REPLACE_ENRICH;
	
	public static final String PARAM_ADJ_NOUN_LIST = "adjNounList";
	@ConfigurationParameter(name = PARAM_ADJ_NOUN_LIST,  mandatory=false)//, defaultValue = "/com/iai/uima/kea/data/wordlists/en/ADJ-NOUN-relation.final")
	private String ADJ_NOUN_LIST;
	
	

	private Hashtable<String, String> adj_noun;


	public static final String PARAM_VERB_NOUN_LIST = "verbNounList";
	@ConfigurationParameter(name = PARAM_VERB_NOUN_LIST,  mandatory=false)//, defaultValue = "/com/iai/uima/kea/data/wordlists/en/VERB-NOUN-relation.final") //SP dynamic language parameter not possible in defaultValue
	private String VERB_NOUN_LIST;
	
	private Hashtable<String, String> verb_noun;
	
	public static final String PARAM_CITY_COUNTRY_LIST = "cityCountryList";
	@ConfigurationParameter(name = PARAM_CITY_COUNTRY_LIST,  mandatory=false) //, defaultValue = "/com/iai/uima/kea/data/wordlists/en/city-country-relation.final")
	private String CITY_COUNTRY_LIST;

	private Hashtable<String, String> city_country;

	public static final String PARAM_COUNTRY_REGION_LIST = "countryRegionList";
	@ConfigurationParameter(name = PARAM_COUNTRY_REGION_LIST,  mandatory=false) //, defaultValue = "/com/iai/uima/kea/data/wordlists/en/country-region-relation.final")
	private String COUNTRY_REGION_LIST;

	private Hashtable<String, String> country_region;

	public static final String PARAM_ABBREV_LONG_LIST = "abbrevLongList";
	@ConfigurationParameter(name = PARAM_ABBREV_LONG_LIST, mandatory=false) // ,defaultValue = "/com/iai/uima/kea/data/wordlists/en/abbrev_long.final")
	private String ABBREV_LONG_LIST;

	public static final String PARAM_POS_COUNT_LIST = "posCountList";
	@ConfigurationParameter(name = PARAM_POS_COUNT_LIST, mandatory=false) // ,defaultValue = "/com/iai/uima/kea/data/wordlists/en/posCount")
	private String POS_COUNT_LIST;
	
	public static final String PARAM_KEYPHRASE_LIST = "keyphraseList";
	@ConfigurationParameter(name = PARAM_KEYPHRASE_LIST, mandatory=false) // ,defaultValue = "/com/iai/uima/kea/data/wordlists/en/keyphraseList")
	private String KEYPHRASE_LIST;
	
	public static final String PARAM_STOPWORDLIST = "stopwordList";
	@ConfigurationParameter(name = PARAM_STOPWORDLIST, mandatory=false)
	private String STOPWORD_LIST;

	private Hashtable<String, String> abbrev_long;
	private Hashtable<String, String> pos_count;
	private Hashtable<String, Integer> thesaurus_list;

	private Stemmer getStemmer(String lang) {
		return lang.equals("es") ? new SpanishStemmer()
		: lang.equals("fr") ? new FrenchStemmer()
		: lang.equals("en") ? new PorterStemmer() : lang
				.equals("de") ? new GermanStemmer() : null;
	}

	private Stopwords getStopwords(String lang, String path) {
		return new Stopwords(this.getClass().getResourceAsStream(path));
	}

	public static boolean isUpperCase2(String s)
	{
		for (int i=0; i<s.length(); i++)
		{
			if (Character.isLowerCase(s.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);

		
//		if (!LANGUAGE.equals("en")){ // no chunker available for languages other than en
//			EXTEND_KEYPHRASE_TO_CHUNK = false;
//		}
		
		if (EXTEND_KEYPHRASE_TO_CHUNK || DETERMINE_POS_OF_MANUAL || POS_FILTER){
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma"); 
		pipeline = new StanfordCoreNLP(props);
		}

		BufferedReader read_adj_noun;
		BufferedReader read_verb_noun;
		BufferedReader read_city_country;
		BufferedReader read_country_region;
		BufferedReader read_abbrev_long;
		BufferedReader read_pos_count;
		
		if (MODEL_LOCATION == null)
			MODEL_LOCATION = "/com/iai/uima/kea/data/models/" + LANGUAGE + "/model";
//			System.out.println("MODEL_LOCATION: " + MODEL_LOCATION + " lang: " + LANGUAGE);
		if (STOPWORD_LIST == null)
			STOPWORD_LIST = "/com/iai/uima/kea/data/stopwords/stopwords_" + LANGUAGE + ".txt";
		
		if (ADJ_NOUN_LIST == null)
			ADJ_NOUN_LIST = "/com/iai/uima/kea/data/wordlists/" + LANGUAGE + "/ADJ-NOUN-relation.final";

		if (VERB_NOUN_LIST == null)
			VERB_NOUN_LIST = "/com/iai/uima/kea/data/wordlists/" + LANGUAGE + "/VERB-NOUN-relation.final";
		
		if (CITY_COUNTRY_LIST == null)
			CITY_COUNTRY_LIST = "/com/iai/uima/kea/data/wordlists/" + LANGUAGE + "/city-country-relation.final";

		if (COUNTRY_REGION_LIST == null)
			COUNTRY_REGION_LIST = "/com/iai/uima/kea/data/wordlists/" + LANGUAGE + "/country-region-relation.final";

		if (ABBREV_LONG_LIST == null)
			ABBREV_LONG_LIST = "/com/iai/uima/kea/data/wordlists/" + LANGUAGE + "/abbrev_long.final";
		
		if (POS_COUNT_LIST == null)
			POS_COUNT_LIST = "/com/iai/uima/kea/data/wordlists/" + LANGUAGE + "/posCount";
		
		if (KEYPHRASE_LIST == null)
			KEYPHRASE_LIST = "/com/iai/uima/kea/data/wordlists/" + LANGUAGE + "/keyphraseList";
		
		String line;


		try {

			read_adj_noun = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(ADJ_NOUN_LIST)));
			read_verb_noun = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(VERB_NOUN_LIST)));
			read_city_country = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(CITY_COUNTRY_LIST)));
			read_country_region = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(COUNTRY_REGION_LIST)));
			read_abbrev_long = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(ABBREV_LONG_LIST)));
			read_pos_count = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(POS_COUNT_LIST)));
//			read_keyphrase_list = new BufferedReader(new InputStreamReader(
//					getClass().getResourceAsStream(KEYPHRASE_LIST)));

			adj_noun = new Hashtable<String, String>();

			while ((line = read_adj_noun.readLine()) != null) {
				String[] content = line.split("\t");
				if (content.length >= 2)
				 adj_noun.put(content[0], content[1]); 
//				System.out.println("ADJ-NOUN" + content[0] + "&&" + content[1]);
			}
			read_adj_noun.close();
			
			verb_noun = new Hashtable<String, String>();

			while ((line = read_verb_noun.readLine()) != null) {
				String[] content = line.split("\t");
//				verb_noun.put(content[0], content[1]); //ORI
//				System.out.println(getStemmer(LANGUAGE).stemString(content[0]) + content[1]);
				if (content.length >= 2)
				 verb_noun.put(getStemmer(LANGUAGE).stemString(content[0]), content[1]); //SPnew
			}
			read_verb_noun.close();

			city_country = new Hashtable<String, String>();

			while ((line = read_city_country.readLine()) != null) {
				String[] content = line.split("\t");
				if (content.length >= 2)
				 city_country.put(content[0], content[1]);
			}
			read_city_country.close();

			country_region = new Hashtable<String, String>();

			while ((line = read_country_region.readLine()) != null) {
				String[] content = line.split("\t");
				if (content.length >= 2)
				 country_region.put(content[0], content[1]);
			}
			read_country_region.close();

			abbrev_long = new Hashtable<String, String>();

			while ((line = read_abbrev_long.readLine()) != null) {
				String[] content = line.split("\t");
				if (content.length >= 2)
				 abbrev_long.put(content[0], content[1]);
			}
			read_abbrev_long.close();
			
			pos_count = new Hashtable<String, String>();

			while ((line = read_pos_count.readLine()) != null) {
				String[] content = line.split("=");
				if (content.length >= 2)
				 pos_count.put(content[0], content[1]);
				
			}
			read_pos_count.close();
			
			thesaurus_list = new Hashtable<String, Integer>();

			

		} catch (IOException e1) {
			throw new ResourceInitializationException(e1);
		}

		ke = new KEAKeyphraseExtractor();

		// A. required arguments (no defaults):

		// 1. Name of the directory -- give the path to your directory with
		// documents
		// documents should be in txt format with an extention "txt".
		// Note: keyphrases with the same name as documents, but extension "key"
		// one keyphrase per line!

		// ke.setDirName(KEA_HOME + "/testdocs/en/test");

		// 3. Name of the vocabulary -- name of the file (without extension)
		// that is stored in VOCABULARIES
		// or "none" if no Vocabulary is used (free keyphrase extraction).
		ke.setVocabulary("none");

		// 4. Format of the vocabulary in 3. Leave empty if vocabulary = "none",
		// use "skos" or "txt" otherwise.
		ke.setVocabularyFormat("");

		// B. optional arguments if you want to change the defaults
		// 5. Encoding of the document
		ke.setEncoding("UTF-8");

		// 6. Language of the document -- use "es" for Spanish, "fr" for French
		// or other languages as specified in your "skos" vocabulary
		ke.setDocumentLanguage(LANGUAGE); // es for Spanish, fr for French

		// 7. Stemmer -- adjust if you use a different language than English or
		// want to alterate results
		// (We have obtained better results for Spanish and French with
		// NoStemmer)
		ke.setStemmer(getStemmer(LANGUAGE));
		// ke.setStemmer(new NoStemmer());

		// 10. Set to true, if you want to compute global dictionaries from the
		// test collection
		ke.setBuildGlobal(false);

		ke.setModelName(MODEL_LOCATION);

		// 8. Stopwords
		ke.setStopwords(getStopwords(LANGUAGE, STOPWORD_LIST));
		
		try {
			ke.loadModel();
		} catch (Exception e) {
			e.printStackTrace();
			throw(new ResourceInitializationException(e));
		}

	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		int numOfwords = aJCas.getDocumentText().split("[^\\s]").length;
		HashSet<String> manualKeyPhrases = new HashSet<String>();
		
		if (LOAD_MANUAL_KEYPHRASES || DETERMINE_POS_OF_MANUAL){
		
		DocumentMetaData meta = DocumentMetaData.get(aJCas);
		String baseUri = meta.getDocumentBaseUri();
		String docID = meta.getDocumentId().substring(0, meta.getDocumentId().lastIndexOf('.'));
		URI uri = null;
		try {
			uri = new URI(baseUri + "manual_keyphrases/" + docID + ".key");
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(uri)));
			String line;
			while ((line = br.readLine()) != null) {
				manualKeyPhrases.add(line.toLowerCase().trim());
//				System.out.println("MANUAL:" + line.toLowerCase().trim());
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("Keyphrase file " + uri + " could not be found");
			return;
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

		
		
		
		
//		String[] chunkList = null;
		
//		String [] chunkList = {"World War II", "rising number", "European migrant crisis"};
//		if (EXTEND_KEYPHRASE_TO_CHUNK)
//			ke.setChunkList(chunkList);
		
		if (KEAPHRASE_RATIO != 0){
		ke.setNumPhrases(numOfwords / KEAPHRASE_RATIO); //for platform
//		System.out.println("RATIO applied..");
		}
		else {
		ke.setNumPhrases(MAX_NUMBER_OF_KEA_KEYPHRASES); //for evaluation
//		System.out.println("MAX_NUMBER_OF_KEA_KEYPHRASES applied..");
		}
//        System.out.println("KEAPHRASE_RATIO" + KEAPHRASE_RATIO);
		
//		 System.out.println("NER_FILTER" + NER_FILTER);
        
		ArrayList<KeyPhrase> keyPhrases = ke.extractKeyphrasesToList(aJCas
				.getDocumentText());

		String text = aJCas.getDocumentText();

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text); //Stanford corenlp class
		

        //sorted by length, longest first
		Collections.sort(keyPhrases, new KeyPhraseComparator());
		
		//Copy of keyPhrases with sort by rank, lowest ranked first
		ArrayList<KeyPhrase> keyPhrasesByRank = new ArrayList<KeyPhrase> (keyPhrases);
		Collections.sort(keyPhrasesByRank, new KeyPhraseByRankComparator());
		

		HashSet<String> keaKeyPhrases = new HashSet<String>();
		
		
		HashSet<String> deprecatedKeyPhrases = new HashSet<String>();
		HashSet<String> deprecatedPosFilterKeyPhrases = new HashSet<String>();
		HashSet<String>  dbpediaCoveredText = new HashSet<String>();
//		 
		HashMap<String,Integer> posSequenceCount = new HashMap<String,Integer>();
		
		HashMap<String,String> keaKeyPos = new HashMap<String,String>();
		HashMap<String,String> manualKeyPos = new HashMap<String,String>();
		HashSet<String>  manualKeyFound = new HashSet<String>();
		
		HashMap<String,Integer> keaPosSequenceCount = new HashMap<String,Integer>();
		HashSet<String>  keaKeyFound = new HashSet<String>();
		
		//SPnew language-specific settings, should be externalized
		 HashSet<String> verbTag = new HashSet<String>(); 
		 HashSet<String> adjTag = new HashSet<String>();
		 
		 HashSet<Integer> rankGroupFound = new HashSet<Integer>();
		 
		 HashSet<String> stopWordExceptionSet = new HashSet<String>();
		 HashSet<String> standaloneStopWordSet = new HashSet<String>();
		 HashSet<String> skipAsInitialPosTag = new HashSet<String>();
		 HashSet<String> skipAsInitialToken = new HashSet<String>();
		 HashSet<String> skipAsFinalPosTag = new HashSet<String>();
		 HashSet<String> skipAsPreposition = new HashSet<String>();
		 HashSet<String> postnominalPreposition = new HashSet<String>();
		 boolean chunksFound = true;
		 
		 boolean postNominalAdjFilter;
		 boolean advFilter;
		 boolean verbFilter;
		 boolean stopwordFilter;
//		 boolean finiteVerbFilter;
		 boolean incompleteNerFilter = true;
		 boolean kpEqualsNerFilter = true;
		 boolean containedInLargerHigherRankedKeyphraseFilter;
		 boolean replaceKeyphrases;
		 boolean enrichKeyphrases;
		 boolean stopWordException;
		 boolean standaloneStopwordFilter;
		 boolean rerankBasedLongKeyphrases;
		 boolean turnoffAllDeprecated;
		 boolean turnonAllDeprecatedExceptNerFilter;
		 boolean deprecatedOnlyPosFilter;
		
		 
		 turnoffAllDeprecated = false;
		 turnonAllDeprecatedExceptNerFilter = false;
		 deprecatedOnlyPosFilter = false;
		
		 
		
		 
		 
		 
//		 EXTEND_KEYPHRASE_TO_CHUNK = false; //XXXX
		 postNominalAdjFilter = true;
		 stopwordFilter = true;
		 stopWordException = false;
		 standaloneStopwordFilter = true;
		 advFilter = true;
		 verbFilter = true;
		 
		 replaceKeyphrases = true;
		 enrichKeyphrases = true;
		 
//		 System.out.println("NER_FILTER" + NER_FILTER);
		 
		 containedInLargerHigherRankedKeyphraseFilter = false;
		 
		 rerankBasedLongKeyphrases = false;
		 
//		 System.out.println("DEPSETTING" + DEPRECATED_FILTERS);
		 if (DEPRECATED_FILTERS.equals(" ignoreNoDeprecatedKp ")){
			 turnoffAllDeprecated = true;
		 }
		 else if (DEPRECATED_FILTERS.equals(" turnonAllExceptNerFilter ")){
			 turnonAllDeprecatedExceptNerFilter = true;
			 System.out.println("DEPSETTING" + DEPRECATED_FILTERS);
		 }
		 else if (DEPRECATED_FILTERS.equals(" ignoreAllDeprecatedKp ")){
			 POS_FILTER = false;
		 }
		 else if (DEPRECATED_FILTERS.equals(" posFilter ")){
			 deprecatedOnlyPosFilter = true;
			 POS_FILTER = true;
		 }
		 
		 if (DEPRECATED_FILTERS.equals(" eumssiSetting ")){
//			 System.out.println("DEPSETTING" + DEPRECATED_FILTERS);
//			 EXTEND_KEYPHRASE_TO_CHUNK = false;
			 postNominalAdjFilter = false;
			 stopwordFilter = false;
			 stopWordException = false;
			 standaloneStopwordFilter = false;
			 advFilter = false;
			 verbFilter = false;
//			 replaceKeyphrases = false;
//			 enrichKeyphrases = false;
//			 incompleteNerFilter = false;
			 kpEqualsNerFilter = false;
//			 containedInLargerHigherRankedKeyphraseFilter = false;
			 rerankBasedLongKeyphrases = false;
			 POS_FILTER = false;
		 }
		 
		 if (turnoffAllDeprecated == true){
			 EXTEND_KEYPHRASE_TO_CHUNK = false;
			 postNominalAdjFilter = false;
			 stopwordFilter = false;
			 stopWordException = false;
			 standaloneStopwordFilter = false;
			 advFilter = false;
			 verbFilter = false;
			 replaceKeyphrases = false;
			 enrichKeyphrases = false;
			 incompleteNerFilter = false;
			 kpEqualsNerFilter = false;
			 containedInLargerHigherRankedKeyphraseFilter = false;
			 rerankBasedLongKeyphrases = false;
			 POS_FILTER = false;
		 }
		 
		 if (turnonAllDeprecatedExceptNerFilter == true){
//			 EXTEND_KEYPHRASE_TO_CHUNK = false;
//			 postNominalAdjFilter = false;
//			 stopwordFilter = false;
//			 stopWordException = false;
//			 standaloneStopwordFilter = false;
//			 advFilter = false;
//			 verbFilter = false;
//			 replaceKeyphrases = false;
//			 enrichKeyphrases = false;
//			 incompleteNerFilter = false;
			 kpEqualsNerFilter = false;
//			 containedInLargerHigherRankedKeyphraseFilter = false;
//			 rerankBasedLongKeyphrases = false;
			 POS_FILTER = false;
		 }
		 if (NER_FILTER){
			 incompleteNerFilter = true;
			 kpEqualsNerFilter = true;
			 }
			 else if (NER_FILTER == false){
				 incompleteNerFilter = true;
				 kpEqualsNerFilter = false;
				 }
		 
		 if (deprecatedOnlyPosFilter == true){
			 EXTEND_KEYPHRASE_TO_CHUNK = false;
			 postNominalAdjFilter = false;
			 stopwordFilter = false;
			 stopWordException = false;
			 standaloneStopwordFilter = false;
			 advFilter = false;
			 verbFilter = false;
			 replaceKeyphrases = false;
			 enrichKeyphrases = false;
			 incompleteNerFilter = false;
			 kpEqualsNerFilter = false;
			 containedInLargerHigherRankedKeyphraseFilter = false;
			 rerankBasedLongKeyphrases = false;
			 POS_FILTER = true;
		 }
		 
		 boolean printZw = false; //SP Zwischenausgabe
		 
		 skipAsInitialToken.add("]");  //case exists
		 skipAsInitialToken.add("[");
		 skipAsInitialToken.add(")");
		 skipAsInitialToken.add("(");
		 skipAsInitialToken.add(",");
		 skipAsInitialToken.add(";");
		 skipAsInitialToken.add(":");
		 skipAsInitialToken.add("\"");
		 skipAsInitialToken.add("*");
		 skipAsInitialToken.add("-");
		 skipAsInitialToken.add("_");
		 
		 if (LANGUAGE == "en"){ //problems with tokenizer, does not split John's  or Marks/dpa etc
			 postNominalAdjFilter = true;
			 verbTag.add("VBD");
			 verbTag.add("VBZ");
			 verbTag.add("VBP");
			 verbTag.add("VB");
			 verbTag.add("VBN");
			 adjTag.add("JJS");
			 adjTag.add("JJ");
			 adjTag.add("JJR");
			 stopWordExceptionSet.add("and");
			 stopWordExceptionSet.add("the");
			 stopWordExceptionSet.add("of");
			 stopWordExceptionSet.add("to");
			 stopWordExceptionSet.add("in");
			 stopWordExceptionSet.add("for");
			 
			 
			 
			 skipAsInitialPosTag.add("DT");
			 skipAsInitialPosTag.add("PRP");
			 skipAsInitialPosTag.add("PRP$");
			 skipAsInitialPosTag.add(",");
			 skipAsInitialPosTag.add("RBS");
			 skipAsInitialPosTag.add("POS");
			 skipAsInitialPosTag.add("WDT");
			 skipAsInitialPosTag.add("TO");
			 skipAsInitialPosTag.add("CC");
			 skipAsInitialPosTag.add("CD");
			 
			 skipAsFinalPosTag.add("DT");
			 skipAsFinalPosTag.add("PRP");
			 skipAsFinalPosTag.add("PRP$");
			 skipAsFinalPosTag.add(",");
			 skipAsFinalPosTag.add("RBS");
			 skipAsFinalPosTag.add("POS");
			 skipAsFinalPosTag.add("WDT");
			 skipAsFinalPosTag.add("TO");
			 skipAsFinalPosTag.add("CC");
			 skipAsFinalPosTag.add("CD");
			 skipAsFinalPosTag.add("VBG");
			 skipAsFinalPosTag.add("VBD");
			 
			 standaloneStopWordSet.add("country");
			 standaloneStopWordSet.add("countries");
			 standaloneStopWordSet.add("home");
			 standaloneStopWordSet.add("world");
			 standaloneStopWordSet.add("land");
			 standaloneStopWordSet.add("case");
			 standaloneStopWordSet.add("cases");
			 standaloneStopWordSet.add("assumption");
			 standaloneStopWordSet.add("method");
			 standaloneStopWordSet.add("methods");
			 standaloneStopWordSet.add("device");
			 standaloneStopWordSet.add("devices");
			 standaloneStopWordSet.add("nation");
			 standaloneStopWordSet.add("nations");
			 standaloneStopWordSet.add("number");
			 standaloneStopWordSet.add("numbers");
			 standaloneStopWordSet.add("time");
			 
			 //not used any more, use postnominalPreposition instead
			 skipAsPreposition.add("like");
			 skipAsPreposition.add("as");
			 skipAsPreposition.add("than");
			 skipAsPreposition.add("over");
			 skipAsPreposition.add("that");
			 skipAsPreposition.add("for");
			 skipAsPreposition.add("such");
			 skipAsPreposition.add("between");
			 skipAsPreposition.add("with");
			 skipAsPreposition.add("among");
			 skipAsPreposition.add("unless");
			 skipAsPreposition.add("after");
			 skipAsPreposition.add("before");
			 skipAsPreposition.add("because");
			 skipAsPreposition.add("below");
			 skipAsPreposition.add("around");
			 skipAsPreposition.add("along");
			 skipAsPreposition.add("beyond");
			 skipAsPreposition.add("if");
			 
			 postnominalPreposition.add("in");
			 postnominalPreposition.add("of");
			 postnominalPreposition.add("for");
			 postnominalPreposition.add("from");
			 postnominalPreposition.add("at");
			 postnominalPreposition.add("on");
			 postnominalPreposition.add("to");
			 postnominalPreposition.add("against");
			 postnominalPreposition.add("with");
			 postnominalPreposition.add("as");
			 postnominalPreposition.add("by");
			 postnominalPreposition.add("between");
			 postnominalPreposition.add("into");
		 }
		 else if (LANGUAGE == "de"){
			 postNominalAdjFilter = true;
			 verbTag.add("VVFIN");
			 adjTag.add("ADJ");
		 }
		 else if (LANGUAGE == "fr"){   //SP problems with tokenizer, does not split d'un est-elle etc.
			 postNominalAdjFilter = false;
			 verbTag.add("V");
			 verbTag.add("VPP");
		 }
		 else if (LANGUAGE == "es"){ 
			 postNominalAdjFilter = false;
			 verbTag.add("V");
			 verbTag.add("VPP");
		 }
		

		
		 if (EXTEND_KEYPHRASE_TO_CHUNK || DETERMINE_POS_OF_MANUAL || POS_FILTER){
			pipeline.annotate(document); 
		 
			boolean chunkFound = false;
			
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			
			for (KeyPhrase kp : keyPhrases) {
				
				keaKeyPhrases.add(kp.getUnstemmed().toLowerCase());
			}
		 
				
			for (CoreMap sentence : sentences) {
//				if (sentence.get(TokensAnnotation.class).size() > 5) { 
					SentenceAnnotation sentenceAnn = new SentenceAnnotation(aJCas);
			
				int beginSentence = sentence.get(TokensAnnotation.class).get(0)
						.beginPosition();
//				System.out.println("SENTENCE" + sentence.toString());
				int endSentence = sentence.get(TokensAnnotation.class)
						.get(sentence.get(TokensAnnotation.class).size() - 1)
						.endPosition();
				sentenceAnn.setBegin(beginSentence);
				sentenceAnn.setEnd(endSentence);
				sentenceAnn.addToIndexes();
				
				if (JCasUtil.contains(aJCas, sentenceAnn, Chunk.class)){
					chunkFound = true;
				}
				
				List<Token> tokens= JCasUtil.selectCovered(aJCas,
						Token.class, sentenceAnn);
				
				
				//Determine POS  of kea keyphrases
				if (POS_FILTER){
				DeterminePosResult keaResult = determinePos ( tokens,  keaKeyPhrases,  aJCas, keaPosSequenceCount, keaKeyFound, keaKeyPos);
				keaKeyPos = keaResult.getKeyPos();
				keaPosSequenceCount = keaResult.getPosSequenceCount();
				keaKeyFound = keaResult.getKeyFound();
				}
				
				//Determine POS sequence of manual keyphrases
			if (DETERMINE_POS_OF_MANUAL && manualKeyPhrases != null ){	
				DeterminePosResult result = determinePos ( tokens,  manualKeyPhrases,  aJCas, posSequenceCount, manualKeyFound, manualKeyPos);
				posSequenceCount = result.getPosSequenceCount();
				manualKeyFound = result.getKeyFound();
				manualKeyPos = result.getKeyPos();
			}
		
				List<DBpediaResource> dbpeds = JCasUtil.selectCovered(aJCas, 
						DBpediaResource.class, sentenceAnn);
				
				
//				List<TopDBpediaResource> dbpeds = JCasUtil.selectCovered(aJCas, 
//						TopDBpediaResource.class, sentenceAnn);
				
				for (DBpediaResource dbped : dbpeds){
//					dbpediaCoveredText.add(dbped.getLabel().toLowerCase().trim());
//					System.out.println("DBPEDIA LABEL:" + dbped.getLabel());
					dbpediaCoveredText.add(dbped.getCoveredText().toLowerCase().trim());
//					System.out.println("DBPEDIA COVERED TEXT:" + dbped.getCoveredText().toLowerCase()); 
				}
				
				
			}
			
			
//			keaKeyPos
			if (POS_FILTER){
			Set<String> keaKeySet = keaKeyPos.keySet();
			 Iterator<String> keaKeySetIterator = keaKeySet.iterator();

			 while (keaKeySetIterator.hasNext()) {
				 String keaKey = keaKeySetIterator.next();
//				 System.out.println("KEA" + keaKey + "POS" +  keaKeyPos.get(keaKey));
//				 String keaPos = keaKeyPos.get(keaKey).replaceFirst("\\[", "").replaceAll("\\]$","");
				 String keaPos = keaKeyPos.get(keaKey);
						if (!pos_count.containsKey(keaPos)){
							String depkeaKey = keaKey.replaceAll(", ", " ").replaceFirst("\\[", "").replaceAll("\\]$","");
							deprecatedPosFilterKeyPhrases.add(depkeaKey);
//							System.out.println("DEPKEAKEY" + depkeaKey + "FILTEREDPOS" + keaPos);
//							later:isKeyPhraseDeprecated = isUndocumentedPosSequence = true;
						}
//						else{
//							System.out.println("NOT FILTERED" + keaKey + " POS " + keaPos );
//						}
			 }
			}
			
			if (DETERMINE_POS_OF_MANUAL){
			 Set<String> keySet = posSequenceCount.keySet();
			 Iterator<String> keySetIterator = keySet.iterator();
			 StringBuilder posCount = new StringBuilder("");
			 while (keySetIterator.hasNext()) {
			    
			    String key = keySetIterator.next();
//			    System.out.println("POS: " + key + " Count: " + posSequenceCount.get(key));
			    posCount.append(key);
			    posCount.append(";");
			    posCount.append(posSequenceCount.get(key));
			    posCount.append(";");
			    
			 }
//			 System.out.println("STRINGBUILD" + posCount );
			 com.iai.uima.jcas.tcas.posSequenceCount posCountAnno = new com.iai.uima.jcas.tcas.posSequenceCount(aJCas);
			 posCountAnno.setPosSequenceCount(posCount.toString());
			 posCountAnno.addToIndexes();
//			 System.out.println("MANCOUNT:" + manualKeyPhrases.size());
			}
			
			if (!chunkFound){
//				EXTEND_KEYPHRASE_TO_CHUNK = false;
//				System.out.println("NO_CHUNKS_FOUND");
				chunksFound = false;
			}
		 }

		 
			
		    Collection<N> nouns = select(aJCas, N.class);
			Collection<ADJ> adjs = select(aJCas, ADJ.class);
			Collection<V> verbs = select(aJCas, V.class);
			Collection<ADV> adverbs = select(aJCas, ADV.class);
			Collection<Person> namedPersons = select(aJCas, Person.class);
			Collection<Location> namedLocations = select(aJCas, Location.class);
			Collection<Organization> namedOrganizations = select(aJCas, Organization.class);
			Collection<NamedEntity> namedEntities =select(aJCas, NamedEntity.class);
			
			HashSet<N> nounsList = new HashSet<N>(nouns);
			HashSet<ADJ> adjsList = new HashSet<ADJ>(adjs);
			HashSet<V> verbsList = new HashSet<V>(verbs);
			HashSet<ADV> adverbsList = new HashSet<ADV>(adverbs);
//			HashSet<N> namedNounsList = new HashSet<N>(nouns);
			
//			for (N noun: nounsList){
				
//			}
			
			//SP unclear whether the following is needed..
			//delete adj, verb, adv candidates that can also be nouns
//			for (N noun : select(aJCas, N.class)) { 
			for (N noun : nouns) {
				//exclude NNPs like Todd in Christine Todd Whitman as kp
//				if (noun.getPosValue().toString().equals("NNP")){
//					namedNounsList.add(noun);
//				}
	
				for (ADJ adj : adjs)
					if (noun.getCoveredText().equalsIgnoreCase( //NEG correct?
							adj.getCoveredText()))
						adjsList.remove(adj);
				for (V verb : verbs)
					//					if (!noun.getCoveredText().equalsIgnoreCase(  // ORI NEG wrong
					if (noun.getCoveredText().equalsIgnoreCase(  //SPneu
							verb.getCoveredText()))
						verbsList.remove(verb);
				//SPnew no resctriction on ADV
								for (ADV adv : adverbs){
									if (noun.getCoveredText().equalsIgnoreCase( //NEG !noun deleted SPnew
											adv.getCoveredText()))
										adverbsList.remove(adv);
								}
			}
			
			HashSet<Person> namedPersonsList = new HashSet<Person>(namedPersons);
			HashSet<Location> namedLocationsList = new HashSet<Location>(namedLocations);
			HashSet<Organization> namedOrganizationsList = new HashSet<Organization>(namedOrganizations);
			HashSet<NamedEntity>namedEntitiesList = new HashSet<NamedEntity>(namedEntities);
			HashSet<String> namedEntitiesCoveredText = new HashSet<String>();
			
			List<KeyPhraseAnnotation> listKeyPhraseAnnotation = new ArrayList<KeyPhraseAnnotation>();
			
			HashMap<String, Integer> kpChunkBasedRank = new HashMap<String, Integer>();
			HashSet<String> kpChunkBasedBeginEnd = new HashSet<String>();
//			HashSet<String> replaceEnrich = new HashSet<String>();
			String kpBeginEndNew = null;
			HashMap<String, Integer> kpReRank = new HashMap<String, Integer>();
			//environment - environmental
			HashMap<String, Integer> kpReplaceReRank = new HashMap<String, Integer>();
//			HashSet<String> kpReplaceDeprecated = new HashSet<String>();
			HashMap<String,String> upCapTolowCap = new HashMap<String,String>();
			HashMap<String,String> replaceEnrich = new HashMap<String,String>();
			HashMap<String,Integer> keyPhraseToRank = new HashMap<String,Integer>();
			
			
			//avoid doublettes if matcherLowerCase and matcher hit
			HashSet<String> kpAnnotationBeginEnd = new HashSet<String>();
			
//			Collection<KeyPhraseAnnotation> addedKeyPhrases = select(aJCas, KeyPhraseAnnotation.class);
//			Collection<KeyPhraseAnnotation> allKeyPhrases = select(aJCas, KeyPhraseAnnotation.class);
			
			
			for (NamedEntity ner : namedEntitiesList){
				namedEntitiesCoveredText.add(ner.getCoveredText().toLowerCase());
//				System.out.println("NER COVERED TEXT:" + ner.getCoveredText().toLowerCase()); 
			}
			
			
			
	KP:	for (KeyPhrase kp : keyPhrases) {
			
//			keaKeyPhrases.add(kp.getUnstemmed().toLowerCase());

			
		
			String patternLowerCaseString = null;
			Pattern patternLowerCase;
			
			
			// Fracking -> fracking
			patternLowerCaseString = String.format("(\\b)(%s)(\\b)", kp.getUnstemmed().toLowerCase());
			patternLowerCase = Pattern.compile(patternLowerCaseString);
			
			String patternString = String.format("(\\b)(%s)(\\b)", kp.getUnstemmed());
			Pattern pattern = Pattern.compile(patternString);
			
			
			boolean isUndocumentedPosSequence = false;
			boolean containsStopword = false;
			boolean isContainedInLargerKeyprase = false;
//			boolean isContainedInDeprecatedKeyPhrase = false;
			boolean isDeprecatedEndsWithAdjective = false;
			boolean isDeprecatedContainsAdverb = false;
			boolean isDeprecatedEndsWithFiniteVerb = false;
			boolean isDeprecatedContainsIncompleteNer = false;
			/* Deprecated keyphrase */
			boolean isKeyPhraseDeprecated = false;
			/* Replaced keyphrase */
			boolean isKeyPhraseReplaced = false;
			boolean isAdjectiveReplacedWithNoun = false;
			boolean isVerbReplacedWithNoun = false;
			
			/* Enriched Keyphrase */
			boolean isKeyPhraseEnriched = false;

			boolean isEnrichedWithAbbreviation = false;
			boolean isEnrichedWithCountry = false;
			boolean isEnrichedWithLongForm = false;
			boolean isEnrichedWithRegion = false;

			//flag for treatment of NER
			boolean kpIsOkNer = false;
			boolean containsFirstName = false;
			boolean containsLastName = false;
			boolean kpContainsOkNer = false;
			boolean isDeprecatedEqualsNer = false;
			
			//score for similarity with thesaurus 
//			int thesaurusScore = 0;
			
			String keyPhraseReplacee = kp.getUnstemmed();
			HashSet<String> kpSet= new HashSet<String>(Arrays.asList(kp.getUnstemmed().toLowerCase().split(" ")));

			String[] kpSplit = kp.getUnstemmed().split(" ");
			
			if (POS_FILTER){
				if (deprecatedPosFilterKeyPhrases.contains(kp.getUnstemmed().toLowerCase())){
					isKeyPhraseDeprecated = isUndocumentedPosSequence = true;
//					System.out.println("deprecated POS_FILTER" + kp.getUnstemmed().toLowerCase());
				}
			}
			
			
			if (stopwordFilter){
			for (String sw : kpSplit) {
				if (ke.getStopwords().isStopword(sw)){
//				if (ke.getStopwords().isStopword(sw) && !stopWordExceptionSet.contains(sw)){
				  if(stopWordException && stopWordExceptionSet.contains(sw)){
//					  System.out.println("STOPWORD Exception " + sw + " " +  kp.getUnstemmed());
				  }
				  else {
				  
					if (printZw) { System.out.println("STOPWORD " + sw + " " +  kp.getUnstemmed());}
					isKeyPhraseDeprecated = containsStopword = true;
				  }
				}
			}
			}
			
			
		

			if (postNominalAdjFilter){ //SP filter does not apply to fr and es
	ADJ:	 for (ADJ adj : adjsList){
				//keyphrases that end with ADJ are deprecated
				if (adjTag.contains(adj.getPosValue())
						&& kp.getUnstemmed().endsWith(adj.getCoveredText().toLowerCase())
						&& !kp.getUnstemmed().equalsIgnoreCase(adj.getCoveredText())){
					if (printZw) { System.out.println("DEPRECATED ends in adj " + kp.getUnstemmed());}
					isDeprecatedEndsWithAdjective = isKeyPhraseDeprecated = true;
					break ADJ;
				}
		     }
			}
			//SPnew: keyphrases that contain ADV are deprecated
			if (advFilter){
	ADV:	for (ADV adv : adverbsList){
				if (kpSet.contains(adv.getCoveredText().toLowerCase())){
					isDeprecatedContainsAdverb = isKeyPhraseDeprecated = true;
					if (printZw) {System.out.println("DEPRECATED contains ADV " + adv.getCoveredText() + " " + kp.getUnstemmed());}
					break ADV;
				}
			}
			}
			
			
			//SPnew: keyphrases that begin or end in the middle of a Person name are deprecated
			//ex: chancellor Angela  in: chancellor <ner>Angela merkel<\ner>
			//'Merkel' is ok but 'Merkel comes' or 'Augstein German' is not ok
			//problem: distinguish double first names from double last names:
			//Franz Josef Strauss versus Christine Todd Whitman (list of first names needed)
			//contains: Christine:Chris Christie
			//contains: Josef Strauss:Johannes Rau
			
			String [] kpWords = kp.getUnstemmed().toLowerCase().split(" ");
			
			//check if kp contains NER before applying Incomplete Ner Filter 
			//no NER in kp "European immigrant crisis" LOC in kp "European Union"
			
			boolean applyNerLoop = false;
		if(incompleteNerFilter || kpEqualsNerFilter){
			Matcher matcherForNer = pattern.matcher(
					aJCas.getDocumentText());
		KPOFFSETS:		while (matcherForNer.find()){
				int kpBegin = matcherForNer.start(2);  
				int kpEnd = matcherForNer.end(2);
				for (NamedEntity ner: namedEntitiesList){
					int nerBegin = ner.getBegin();
			        int nerEnd = ner.getEnd();
			        if ((nerBegin <= kpBegin && kpBegin <= nerEnd) || 
			        		(nerBegin <= kpEnd && kpEnd <= nerEnd) ){
//			        	System.out.println("containsNER " + kp.getUnstemmed().toLowerCase());
			        	applyNerLoop = true;
//			        	incompleteNerFilter = true;
			        	break KPOFFSETS;
			        }
				}
			}
		}
//		if(incompleteNerFilter){	
			if(applyNerLoop){	
//				System.out.println("INCOMPLETE NER FILTER " + kp.getUnstemmed().toLowerCase());
	PER:	for (Person person : namedPersonsList){
//				System.out.println("NER found " +  person.getCoveredText());
			//accept keyphrases that equal one NER annotation in the text, additionally accept
		    //keyphrases that correspond to the last name of a Person.
		    //names (and last names) are also exempted from the isContainedInLongerKeyphrase filter.
		
		        //OK: kp equals NER
//		        

		
		 		if (kp.getUnstemmed().toLowerCase().contains(person.getCoveredText().toLowerCase())
		 			&& kp.getUnstemmed().equalsIgnoreCase(person.getCoveredText())
		 			){
//		 			System.out.println("PER OK EQ FULL " +  kp.getUnstemmed() + ":" + person.getCoveredText());
		 			kpIsOkNer = true;
		 			isDeprecatedContainsIncompleteNer = false;
		 			isDeprecatedEqualsNer = true;
		 			break PER;
		 		}
		
				String [] namedPersonWords = person.getCoveredText().split(" ");
				
				//Name consists of at least 2 words
				if (namedPersonWords.length > 1){
					
					
					String lastName = namedPersonWords[(namedPersonWords.length -1)];
					String firstName = namedPersonWords[0];
					
				    //OK: kp equals last name
					if (kp.getUnstemmed().toLowerCase().contains(lastName.toLowerCase())
							&& kp.getUnstemmed().equalsIgnoreCase(lastName)){
//						System.out.println("PER OK last " +  kp.getUnstemmed() + ":" + person.getCoveredText());
						kpIsOkNer = true;
						isDeprecatedEqualsNer = true;
						isDeprecatedContainsIncompleteNer = false;
						break PER;
					}
					containsFirstName = false;
					containsLastName = false;
					for (String kpWord : kpWords){
						if (kpWord.equalsIgnoreCase(firstName)){
							isDeprecatedContainsIncompleteNer = true;
							containsFirstName = true;
//							System.out.println("PER first " +  kp.getUnstemmed() + ":" + firstName + ":" + person.getCoveredText());
						}
						if (kpWord.equalsIgnoreCase(lastName)){
							containsLastName = true;
							isDeprecatedContainsIncompleteNer = true;
//							System.out.println("PER last " +  kp.getUnstemmed() + ":" + lastName);
						}
					}
					//"Todd" in "Christine Todd Whitman"
					if (!containsFirstName && !containsLastName){
					for (String nerWords: namedPersonWords){
						if (nerWords.equals(kp.getUnstemmed())){
							isDeprecatedContainsIncompleteNer = true;
//							System.out.println("NERP contains KP " + kp.getUnstemmed() + ":" + person.getCoveredText());
						}
					}
					}
					//OK: kp contains full name: "chancellor Angela Merkel"
					if (containsFirstName && containsLastName 
							&& kp.getUnstemmed().toLowerCase().contains(person.getCoveredText())){
						kpContainsOkNer = true;
						isDeprecatedContainsIncompleteNer = false;
//						System.out.println("PER OK CONT FULL " +  kp.getUnstemmed() + ":" + person.getCoveredText());
						break PER;
					}
				}
			}
			
		
			
         if (kpIsOkNer == false){
	LOC:	for (Location location : namedLocationsList){
				//kp equals NER
				if (kp.getUnstemmed().toLowerCase().contains(location.getCoveredText().toLowerCase())
						&& kp.getUnstemmed().equalsIgnoreCase(location.getCoveredText())
						){
//					System.out.println("LOC OK EQ FULL  " +  kp.getUnstemmed() + ":" + location.getCoveredText());
					kpIsOkNer = true;
					isDeprecatedEqualsNer = true;
					isDeprecatedContainsIncompleteNer = false;
					break LOC;
				}
		
		
		
				String [] namedLocationWords = location.getCoveredText().split(" ");
				
			
				if (namedLocationWords.length > 1){
					
					
					
					String lastName = namedLocationWords[(namedLocationWords.length -1)];
					String firstName = namedLocationWords[0];
					containsFirstName = false;
					containsLastName = false;
					for (String kpWord : kpWords){
						if (kpWord.equalsIgnoreCase(firstName)){
							containsFirstName = true;
							isDeprecatedContainsIncompleteNer = true;
//							System.out.println("LOC first " +  kp.getUnstemmed() + ":" + firstName + ":" + location.getCoveredText());
						}
						if (kpWord.equalsIgnoreCase(lastName)){
							containsLastName = true;
							isDeprecatedContainsIncompleteNer = true;
//							System.out.println("LOC last " +  kp.getUnstemmed() + ":" + lastName + ":" + location.getCoveredText());
						}
					}
					if (!containsFirstName && !containsLastName){
						for (String nerWords: namedLocationWords){
							if (nerWords.equals(kp.getUnstemmed())){
								isDeprecatedContainsIncompleteNer = true;
//								System.out.println("NERL contains KP " + kp.getUnstemmed() + ":" + location.getCoveredText());
							}
						}
						}
					//kp contains NER
					if (containsFirstName && containsLastName 
					&& kp.getUnstemmed().toLowerCase().contains(location.getCoveredText())
						){
//						System.out.println("LOC OK CONT FULL " +  kp.getUnstemmed() + ":" + location.getCoveredText());
						kpContainsOkNer = true;
						isDeprecatedContainsIncompleteNer = false;
						break LOC;
					}
				}
			}
         }
			
         //NER:ORG 
         
         
         if (kpIsOkNer == false){
	ORG:	for (Organization organization : namedOrganizationsList){
				//kp equals NER
				if (kp.getUnstemmed().toLowerCase().contains(organization.getCoveredText().toLowerCase())
						&& kp.getUnstemmed().equalsIgnoreCase(organization.getCoveredText())
						){
//					System.out.println("ORG OK EQ FULL  " +  kp.getUnstemmed() + ":" + organization.getCoveredText());
					kpIsOkNer = true;
					isDeprecatedEqualsNer = true;
					isDeprecatedContainsIncompleteNer = false;
					break ORG;
				}
				else if (isUpperCase2(kp.getUnstemmed().toString())
						&& organization.getCoveredText().contains(kp.getUnstemmed())){
//					System.out.println("ORG OK ALLCAP  " +  kp.getUnstemmed() + ":" + organization.getCoveredText());
					kpIsOkNer = true;
					isDeprecatedEqualsNer = true; //SP check if this case is really Equality of KP and ORG
					isDeprecatedContainsIncompleteNer = false;
					break ORG;
					
				}
		
		
		
				String [] namedOrganizationWords = organization.getCoveredText().split(" ");
				
			
				if (namedOrganizationWords.length > 1){
					String lastName = namedOrganizationWords[(namedOrganizationWords.length -1)];
					String firstName = namedOrganizationWords[0];
					containsFirstName = false;
					containsLastName = false;
					for (String kpWord : kpWords){
						if (kpWord.equalsIgnoreCase(firstName)){
							containsFirstName = true;
							isDeprecatedContainsIncompleteNer = true;
//							System.out.println("ORG first " +  kp.getUnstemmed() + ":" + firstName + ":" + organization.getCoveredText());
						}
						if (kpWord.equalsIgnoreCase(lastName)){
							containsLastName = true;
							isDeprecatedContainsIncompleteNer = true;
//							System.out.println("ORG last " +  kp.getUnstemmed() + ":" + lastName + ":" + organization.getCoveredText());
						}
					}
					if (!containsFirstName && !containsLastName){
						for (String nerWords: namedOrganizationWords){
							if (nerWords.equals(kp.getUnstemmed())){
								isDeprecatedContainsIncompleteNer = true;
//								System.out.println("NERO contains KP " + kp.getUnstemmed() + ":" + organization.getCoveredText());
							}
						}
						}
					//kp contains NER
					if (containsFirstName && containsLastName 
					&& kp.getUnstemmed().toLowerCase().contains(organization.getCoveredText())
						){
						if (printZw) {System.out.println("ORG OK CONT FULL " +  kp.getUnstemmed() + ":" + organization.getCoveredText());}
						kpContainsOkNer = true;
						isDeprecatedContainsIncompleteNer = false;
						break ORG;
					}
				}
			}
         }
         
         
			
         
			if (incompleteNerFilter && isDeprecatedContainsIncompleteNer == true){
				if (printZw) { System.out.println("DEP NER INCOMPLETE kp " +  kp.getUnstemmed());}
				isKeyPhraseDeprecated = true;
				isDeprecatedContainsIncompleteNer = true;
			}
			if (kpEqualsNerFilter && isDeprecatedEqualsNer){
				if (printZw) { System.out.println("DEP NER EQUALS kp " +  kp.getUnstemmed());}
				isKeyPhraseDeprecated = true;
				isDeprecatedEqualsNer = true;
				//SP deprecated Type missing
			}
			
		}
			
			
	
	if (verbFilter){		
	FIV:	for (V finV : verbsList){
				// keyphrases that are finite verbs are deprecated
				if (verbTag.contains(finV.getPosValue())
						&& kpSet.contains(finV.getCoveredText().toLowerCase())
//						&& split.length == 1
						&& kp.getUnstemmed().equalsIgnoreCase(finV.getCoveredText()))
				{
					if (replaceKeyphrases){
		VERBNOUN:	for (String verb : verb_noun.keySet()){
					    //SP generic alternative for all languages but equal to Unstemmed only fulfilled if no inflection
//						if (verb.equalsIgnoreCase(kp.getUnstemmed())){ 
						//SP mimic English finite verb inflection, irregular verbs not accounted for
					    if (LANGUAGE == "en" && 
					    		(kp.getUnstemmed().toLowerCase().equals(kp.getStemmed().toLowerCase() + "s") ||
					    		 kp.getUnstemmed().toLowerCase().equals(kp.getStemmed().toLowerCase() + "ed") ||
					    		 kp.getUnstemmed().toLowerCase().equals(kp.getStemmed().toLowerCase() + "d") 
					    				) 
							     && verb.equalsIgnoreCase(kp.getStemmed())){
						 	   keyPhraseReplacee = verb_noun.get(verb);
							if (printZw) {System.out.println("Replace verb_noun " +  kp.getUnstemmed() + " " + keyPhraseReplacee);}
							   isKeyPhraseReplaced = isVerbReplacedWithNoun = true;
							   break VERBNOUN;
					    }
					}
					}
                if (isKeyPhraseReplaced == false){			
                	if (printZw) { System.out.println("FINITE " + kp.getUnstemmed().toString());}
					isDeprecatedEndsWithFiniteVerb  = isKeyPhraseDeprecated = true;
					break FIV;
                }
				}
			}	
	}
			
		if (replaceKeyphrases){
ADJNOUN:	for (String adj : adj_noun.keySet()){
				//				if (kp.getUnstemmed().toLowerCase().equals(kp.getStemmed().toLowerCase() + "s")){
				//					System.out.println("Hello, World HACK");
				//				}
				if (adj.equalsIgnoreCase(kp.getUnstemmed())){ 
					keyPhraseReplacee = adj_noun.get(adj);
				if (printZw) {System.out.println("Hello, World Replace adj_noun " +  kp.getUnstemmed() + " " + keyPhraseReplacee);}
					isKeyPhraseReplaced = isAdjectiveReplacedWithNoun = true;
//					System.out.println("Hello, World Replace adj_noun " +  kp.getUnstemmed() + "-> " + keyPhraseReplacee);
					break ADJNOUN;
				}
				// HACK to account for Germans -> Germany, the adj_noun list contains only german germany.
				// if the stemmed and unstemmed version of the adj differs only with respect to a word-final s then
				// the mapping applies.
				// Using stemmed versions is too risky since stems are too general crofting -> croft should apply
				// but croft -> croft or should not apply

				else if (LANGUAGE == "en" && (kp.getUnstemmed().toLowerCase().equals(kp.getStemmed().toLowerCase() + "s")) && 
					adj.equalsIgnoreCase(kp.getStemmed())){
					if (printZw) {System.out.println("HACK" + kp.getStemmed().toString() + " " + keyPhraseReplacee);}
					keyPhraseReplacee = adj_noun.get(adj);
					isKeyPhraseReplaced = isAdjectiveReplacedWithNoun = true;
//					System.out.println("Hello, World Replace adj_noun " +  kp.getUnstemmed()+ "-> " + keyPhraseReplacee);
					break ADJNOUN;
				}
			}
		  if (kpEqualsNerFilter && isKeyPhraseReplaced){
		NER: for (NamedEntity ner: namedEntitiesList){
				  if (ner.getCoveredText().equals(keyPhraseReplacee)){
				  isKeyPhraseReplaced = isAdjectiveReplacedWithNoun = false;
				  isKeyPhraseDeprecated = isDeprecatedEqualsNer = true;
				  if (printZw) {System.out.println("DEP REPLACED IS NER KP " + kp.getUnstemmed()+ "REP " + keyPhraseReplacee + "NER" + ner.getCoveredText());}
				  break NER;
				  }  
			}
		  }
		  //
		if (isKeyPhraseReplaced && EXTEND_KEYPHRASE_TO_CHUNK){
KPREPLACE:  for (KeyPhrase kpRep : keyPhrases){
//	if (printZw) {System.out.println("KP ALL " + kpRep.getUnstemmed());}
					  if (kpRep.getUnstemmed().equalsIgnoreCase(keyPhraseReplacee)){
						  //environmental - environment
						//kp is deprecated, kpRep is preferred
//						  isKeyPhraseDeprecated = true;
						  isKeyPhraseReplaced = isAdjectiveReplacedWithNoun = false; //unclear whether it makes sense
						  if (kpRep.getRank()>kp.getRank()){
						// kpRep with higher score  gets score of kp
						     kpReplaceReRank.put(kpRep.getUnstemmed(), kp.getRank());
//						     System.out.println("REPLACEDRERANK " + kp.getUnstemmed()+ kp.getRank() +
//						    		 "REP "  + kpRep.getUnstemmed() + kpRep.getRank());	
						  }
						 if (printZw) {System.out.println("DEP REPLACED IS NOUN KP " + kp.getUnstemmed()+ "REP " + keyPhraseReplacee + "NOUN" + kpRep.getUnstemmed());}
						  break KPREPLACE;
					  }
				  }	  
			}
		
		  
		  
		}
		
			
		  if (enrichKeyphrases){	
COUNTREG: for (String country : country_region.keySet()){
				if (country.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = country_region.get(country);
					//SPneu
					if (printZw) { System.out.println("Hello, World Replace country_region " + keyPhraseReplacee + kp.getUnstemmed());}
					isKeyPhraseEnriched = true;
					isEnrichedWithRegion = true;
					break COUNTREG;
				}
			}

CITCOUNT: for (String city : city_country.keySet()){
				if (city.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = city_country.get(city);
					if (printZw) { System.out.println("Hello, World Replace city_country " + keyPhraseReplacee + kp.getUnstemmed());}
					isKeyPhraseEnriched = true;
					isEnrichedWithCountry = true;
					break CITCOUNT;
				}
			}

ABBLONG:		for (String abbrev : abbrev_long.keySet()){
				if (abbrev.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = abbrev_long.get(abbrev);
					if (printZw) { System.out.println("Hello, World Replace long abbrev ");}
					isKeyPhraseEnriched = true;
					isEnrichedWithLongForm = true;
					break ABBLONG;
				}
			}

LONGABB:		for (Entry<String, String> entry : abbrev_long.entrySet()){
				if (entry.getValue().equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = entry.getKey();
					if (printZw) { System.out.println("Hello, World Replace abbrev long ");}
					isKeyPhraseEnriched = true;
					isEnrichedWithAbbreviation = true;
					break LONGABB;
				}
			}
		  }
//		  System.out.println("KP3 " + kp.getUnstemmed().toLowerCase());
			if (isKeyPhraseDeprecated){
//				deprecatedKeyPhrases.add(kp); //ORI impossible to get string value??
//				System.out.println("DEPRECATED last " + isKeyPhraseDeprecated + " " + kp.getUnstemmed());
				deprecatedKeyPhrases.add(kp.getUnstemmed().toLowerCase()); //easier to store keyphrase-string
			}

			
			Collection<KeyPhraseAnnotation> addedKeyPhrases = select(aJCas, KeyPhraseAnnotation.class);
			
			
			if (containedInLargerHigherRankedKeyphraseFilter){
			if (kpIsOkNer == false){
	OTHERFORLARGERHIGHERRANKED:		for (KeyPhraseAnnotation other : addedKeyPhrases) {
				
		
		String patternOtherString = String.format("(\\b)(%s)(\\b)", kp.getUnstemmed().toLowerCase());
		Pattern patternOther = Pattern.compile(patternOtherString);
		Matcher matcherOther = patternOther.matcher(
				other.getKeyPhrase().toLowerCase());
		
		
//				if ( other.getKeyPhrase().toLowerCase().contains(kp.getUnstemmed().toLowerCase()) //ORI
				if (matcherOther.find()
						&&	!other.getKeyPhrase().equalsIgnoreCase(kp.getUnstemmed())	//ORI
						&& !deprecatedKeyPhrases.contains(other.getKeyPhrase().toLowerCase())){ 
					if (other.getRank() < kp.getRank()){
						isKeyPhraseDeprecated = isContainedInLargerKeyprase = true;
						if (printZw) {System.out.println("DEPRECATED contained in Larger higher ranked kp " 
						+ isKeyPhraseDeprecated + " " + kp.getUnstemmed());}
						
						break OTHERFORLARGERHIGHERRANKED;
						
					}
				}
			}
			if (isKeyPhraseDeprecated){
				deprecatedKeyPhrases.add(kp.getUnstemmed().toLowerCase()); //easier to store keyphrase-string
			}
			}
			}
			



			KeyPhraseAnnotation annotation;
			Matcher matcher = pattern.matcher(
					aJCas.getDocumentText());
			
			//For Fracking - fracking
			Matcher matcherLowerCase = patternLowerCase.matcher(
					aJCas.getDocumentText()); 
			String kpOffsets = null;
			
MATCHLOWCASE:		while (matcherLowerCase.find()) {
				
				
				if (isKeyPhraseDeprecated) {
//					System.out.println("DEPRECATED " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotationDeprecated(aJCas);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsStopword(containsStopword);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setEndsWithAdjective(isDeprecatedEndsWithAdjective);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsFiniteVerb(isDeprecatedEndsWithFiniteVerb);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsAdverb(isDeprecatedContainsAdverb);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setIsContainedInLongerKeyPhrase(isContainedInLargerKeyprase);
//					((KeyPhraseAnnotationDeprecated) annotation)
//					.setIsContainedInDeprecatedKeyPhrase(isContainedInDeprecatedKeyPhrase);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsIncompleteNer(isDeprecatedContainsIncompleteNer);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setEqualsNer(isDeprecatedEqualsNer);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setPosFilter(isUndocumentedPosSequence);
					
				} else if (isKeyPhraseReplaced) {
//					System.out.println("REPLACED " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotationReplaced(aJCas);
					((KeyPhraseAnnotationReplaced) annotation)
				    .setIsAdjectiveReplacedWithNoun(isAdjectiveReplacedWithNoun); //SPnew
				((KeyPhraseAnnotationReplaced) annotation)  
				    .setIsVerbReplacedWithNoun(isVerbReplacedWithNoun);
				((KeyPhraseAnnotationReplaced) annotation)
					.setReplacee(keyPhraseReplacee);
				replaceEnrich.put(kp.getUnstemmed(), keyPhraseReplacee);
				
				} else if (isKeyPhraseEnriched) {
//					System.out.println("ENRICHED " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotationEnriched(aJCas);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithAbbreviation(isEnrichedWithAbbreviation);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithCountry(isEnrichedWithCountry);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithLongForm(isEnrichedWithLongForm);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithRegion(isEnrichedWithRegion);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichment(keyPhraseReplacee);
					replaceEnrich.put(kp.getUnstemmed(), keyPhraseReplacee);
					
				} else {
//					System.out.println("NORMAL " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotation(aJCas);
					
				}


				if (!matcherLowerCase.group(2).equals(kp.getUnstemmed())){
					annotation.setKeyPhrase(matcherLowerCase.group(2));
					upCapTolowCap.put(kp.getUnstemmed(),matcherLowerCase.group(2));
//					System.out.println("UP TO LOW" + kp.getUnstemmed() + "->" + matcherLowerCase.group(2) );
				}
				else {
				annotation.setKeyPhrase(kp.getUnstemmed());
//				System.out.println("ORIGINAL" + kp.getUnstemmed());
				}
				
				
				
//					if (isKeyPhraseReplaced && EVAL_REPLACE_ENRICH.contains("REPLACE")){
//						annotation.setKeyPhrase(keyPhraseReplacee);
//						if (printZw) {System.out.println("EVAL_MODE REPLACE");}
//					}
//					else if (isKeyPhraseEnriched && EVAL_REPLACE_ENRICH.contains("ENRICH")){
//						KeyPhraseAnnotation annotationAdd = new KeyPhraseAnnotationEnriched(aJCas);
//						annotationAdd.setKeyPhrase(keyPhraseReplacee);
//						annotationAdd.setProbability(kp.getProbability());
//						annotationAdd.setRank(kp.getRank()); //later revised
//						annotationAdd.setStem("ADD");
//						annotationAdd.setBegin(matcher.start(2));  //SPnew
//						annotationAdd.setEnd(matcher.end(2));
//						annotationAdd.setLanguage(LANGUAGE);
//						listKeyPhraseAnnotation.add(annotationAdd);
//						if (printZw) {System.out.println("EVAL_MODE ENRICH");}
//						
//					}
				
				
				annotation.setProbability(kp.getProbability());
				annotation.setStem(kp.getStemmed());
				//hits if replacee follows replaced (i.e. is shorter
				
				annotation.setRank(kp.getRank()); //later revised
				annotation.setRankGroup(kp.getRank());
				
					annotation.setBegin(matcherLowerCase.start(2)); 
					annotation.setEnd(matcherLowerCase.end(2));
					kpOffsets = "" + matcherLowerCase.start(2) + "-" + matcherLowerCase.end(2);
					kpAnnotationBeginEnd.add(kpOffsets);
//					System.out.println("LowerCase " + annotation.getKeyPhrase() + "RANK" + annotation.getRank() + " " + matcherLowerCase.start(2) + "-" + matcherLowerCase.end(2));
				

				
				annotation.setLanguage(LANGUAGE);
				listKeyPhraseAnnotation.add(annotation);


//					annotation.addToIndexes(); //later
				
			}//end matcherLowerCase.find()
//			
			
			
			
	MATCHER:		while (matcher.find()) {
				kpOffsets = "" + matcher.start(2) + "-" + matcher.end(2);
				if (kpAnnotationBeginEnd.contains(kpOffsets)){
					continue MATCHER;
				}
				if (isKeyPhraseDeprecated) {
//					System.out.println("DEPRECATED " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotationDeprecated(aJCas);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsStopword(containsStopword);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setEndsWithAdjective(isDeprecatedEndsWithAdjective);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsFiniteVerb(isDeprecatedEndsWithFiniteVerb);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsAdverb(isDeprecatedContainsAdverb);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setIsContainedInLongerKeyPhrase(isContainedInLargerKeyprase);
//					((KeyPhraseAnnotationDeprecated) annotation)
//					.setIsContainedInDeprecatedKeyPhrase(isContainedInDeprecatedKeyPhrase);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setContainsIncompleteNer(isDeprecatedContainsIncompleteNer);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setEqualsNer(isDeprecatedEqualsNer);
					((KeyPhraseAnnotationDeprecated) annotation)
					.setPosFilter(isUndocumentedPosSequence);
				} else if (isKeyPhraseReplaced) {
//					System.out.println("REPLACED " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotationReplaced(aJCas);
					((KeyPhraseAnnotationReplaced) annotation)
				    .setIsAdjectiveReplacedWithNoun(isAdjectiveReplacedWithNoun); //SPnew
				((KeyPhraseAnnotationReplaced) annotation)  
				    .setIsVerbReplacedWithNoun(isVerbReplacedWithNoun);
				((KeyPhraseAnnotationReplaced) annotation)
					.setReplacee(keyPhraseReplacee);
				replaceEnrich.put(kp.getUnstemmed(), keyPhraseReplacee);
				} else if (isKeyPhraseEnriched) {
//					System.out.println("ENRICHED " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotationEnriched(aJCas);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithAbbreviation(isEnrichedWithAbbreviation);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithCountry(isEnrichedWithCountry);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithLongForm(isEnrichedWithLongForm);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichedWithRegion(isEnrichedWithRegion);
					((KeyPhraseAnnotationEnriched) annotation)
					.setEnrichment(keyPhraseReplacee);
					replaceEnrich.put(kp.getUnstemmed(), keyPhraseReplacee);
					
					
				} else {
//					System.out.println("NORMAL " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotation(aJCas);
				}

				
				if (upCapTolowCap.containsKey(kp.getUnstemmed())){
					annotation.setKeyPhrase(upCapTolowCap.get(kp.getUnstemmed()));
//					System.out.println("UP TO LOW matcher" + kp.getUnstemmed() + "->" + upCapTolowCap.get(kp.getUnstemmed()));
				}
				else {
//				System.out.println("ORIGINAL matcher" + matcher.group(2));
				annotation.setKeyPhrase(kp.getUnstemmed());
				
				
				
				}
//				System.out.println("LOWER CASE ONLY" + kp.getUnstemmed());
//				}
				
				
				
				annotation.setKeyPhrase(kp.getUnstemmed());
				
			
//					if (isKeyPhraseReplaced && EVAL_REPLACE_ENRICH.contains("REPLACE")){
//						annotation.setKeyPhrase(keyPhraseReplacee);
//						if (printZw) {System.out.println("EVAL_MODE REPLACE");}
//					}
//					else if (isKeyPhraseEnriched && EVAL_REPLACE_ENRICH.contains("ENRICH")){
//						KeyPhraseAnnotation annotationAdd = new KeyPhraseAnnotationEnriched(aJCas);
//						annotationAdd.setKeyPhrase(keyPhraseReplacee);
//						annotationAdd.setProbability(kp.getProbability());
//						annotationAdd.setRank(kp.getRank()); //later revised
//						annotationAdd.setStem("ADD");
//						annotationAdd.setBegin(matcher.start(2));  //SPnew
//						annotationAdd.setEnd(matcher.end(2));
//						annotationAdd.setLanguage(LANGUAGE);
//						listKeyPhraseAnnotation.add(annotationAdd);
//						if (printZw) {System.out.println("EVAL_MODE ENRICH");}
//					}
				
				
				annotation.setProbability(kp.getProbability());
				annotation.setStem(kp.getStemmed());
				//hits if replacee follows replaced (i.e. is shorter
				
				annotation.setRank(kp.getRank()); //later revised
				annotation.setRankGroup(kp.getRank());
				
				annotation.setBegin(matcher.start(2));  //SPnew
				annotation.setEnd(matcher.end(2)); 
//				System.out.println("matcher " + annotation.getKeyPhrase() + "RANK" + annotation.getRank() + " "  + matcher.start(2) + "-" + matcher.end(2));

				
				annotation.setLanguage(LANGUAGE);
				listKeyPhraseAnnotation.add(annotation);


//					annotation.addToIndexes(); //later
				
			}//end Matcher.find
		} //for kp
		

		
		
		

	
		boolean byRank = false;
		

		byRank = true;

		
//		if (EXTEND_KEYPHRASE_TO_CHUNK == false) //don't activate,  blocks addToIndexes
//			return;
		
		if (byRank){
			Collections.sort(listKeyPhraseAnnotation, new KeyPhraseAnnotationByRankLengthComparator());
//			System.out.println ("sort by rank");
			}
		String oldText = "";
		int oldRank = -1;
		int addToRank = 0;
		int finalRank = -1;
		int i;
		i = -1;

//		MAX_NUMBER_OF_KEYPHRASES = 10; //SP global parameter
		int keyphraseCount =0;
		HashSet <String> kpText = new HashSet<>();
		String kpOri = null;
		int countChunkKpPerKp = 0;
		KPA: for (KeyPhraseAnnotation kpa : listKeyPhraseAnnotation) {
			
//			System.out.println("KPA TYPE " + kpa.getType().getShortName() + " " + kpa.getCoveredText() + "RANK:" + kpa.getRank());
			if (kpa.getType().getShortName().equals("KeyPhraseAnnotationDeprecated")) {
				kpa.addToIndexes();
//				System.out.println("KPA TYPE " + kpa.getType().getShortName() + " " + kpa.getCoveredText());
			}
			else if (
					(kpa.getType().getShortName().equals("KeyPhraseAnnotation") 
						||	kpa.getType().getShortName().equals("KeyPhraseAnnotationReplaced") 
						||	kpa.getType().getShortName().equals("KeyPhraseAnnotationEnriched") 
							)){
				String [] kpaParts = kpa.getCoveredText().split(" ");

				
				if (kpa.getType().getShortName().equals("KeyPhraseAnnotationReplaced") 
						||	kpa.getType().getShortName().equals("KeyPhraseAnnotationEnriched") 
							)
//				System.out.println("REPENRICH FOUND" + kpa.getKeyPhrase());
				
				if (EXTEND_KEYPHRASE_TO_CHUNK == false && DBPEDIA_FILTER
						&& dbpediaCoveredText.contains(kpa.getCoveredText().toLowerCase().trim())){
//					System.out.println("DBPEDIA FILTER NO CHUNK " + kpa.getCoveredText().toLowerCase());
					continue KPA; //no addToIndexes
			 	}
				
				 	kpa.addToIndexes();
				 	
				 	
				 	

				 	
				 	if (EXTEND_KEYPHRASE_TO_CHUNK == false){
				 		continue KPA;
				 	}
				 	int kpBeginNew  = kpa.getBegin();
					int kpEndNew = kpa.getEnd();
					int kpRankNew = kpa.getRank();
					int kpGroup = kpa.getRank();
					
					Integer kpGroupInteger = new Integer(kpGroup);
							
					kpBeginEndNew = "" + kpBeginNew + "-" + kpEndNew;
					String kpKeyPhraseNew = kpa.getKeyPhrase();

					
					
					if (EXTEND_KEYPHRASE_TO_CHUNK && chunksFound){	//NEW
						kpBeginNew = -1;
						kpEndNew = -1;
						kpKeyPhraseNew = "";
						kpOri = kpa.getCoveredText();
//						System.out.println("KP BEFORE CHUNK EXTENSION" + kpa.getCoveredText());
						
						
//						if (chunksFound){ //NEW
						List <Chunk> chunks = JCasUtil.selectCovering(aJCas,
								Chunk.class, kpa);
						
						List <Chunk> precedingChunks = JCasUtil.selectPreceding(aJCas, Chunk.class, kpa,2);
						
						List <Chunk> followingChunks = JCasUtil.selectFollowing(aJCas, Chunk.class, kpa,2);
						
						if (chunks.size()==0){
							//Unattended garbage piled
//						System.out.println("IGNORE CONTAINS CHUNK BORDER OR NO CHUNK " + kpa.getCoveredText());
						kpa.removeFromIndexes();
						continue KPA;
						}
						
						
						
						if(incompleteNerFilter || kpEqualsNerFilter){
						
						
//						Iterator it = replaceEnrich.entrySet().iterator();
//					    while (it.hasNext()) {
//					        Map.Entry pair = (Map.Entry)it.next();
//					        System.out.println("REPEN" + pair.getKey() + " = " + pair.getValue());
//					        it.remove(); // avoids a ConcurrentModificationException
//					    }
					    
						if (replaceEnrich.containsKey(kpa.getCoveredText()) 
								&& namedEntitiesCoveredText.contains(replaceEnrich.get(kpa.getCoveredText().toLowerCase()))){
//										System.out.println("IGNORE REPLACED NER " + kpa.getCoveredText() + ":" + replaceEnrich.get(kpa.getCoveredText()));
										kpa.removeFromIndexes();
										continue KPA;
									
						}
						}
						
						for (Chunk aChunk : chunks) {
							
						  if(incompleteNerFilter || kpEqualsNerFilter){

							for (Person person : namedPersonsList){
								if (aChunk.getChunkValue()!=null && aChunk.getCoveredText().contains(person.getCoveredText())){
//									System.out.println("IGNORE PER " + kpa.getCoveredText() + ":" + aChunk.getCoveredText());
									kpa.removeFromIndexes();
									continue KPA;
								}
							}
						  }
						  
						  
						  if (aChunk.getChunkValue()!=null && (aChunk.getCoveredText().contains("http:") 
								  || aChunk.getCoveredText().contains("https:") 
								  || aChunk.getCoveredText().contains("www."))){
//							  System.out.println("IGNORE WEB ADDRESS " + kpa.getCoveredText() + ":" + aChunk.getCoveredText());
							  kpa.removeFromIndexes();
								continue KPA;
						  }
						 
						  
						 
						 
						  
							
//							System.out.println("KP " + kpa.getCoveredText());
//							System.out.println("CHUNK " + aChunk.getCoveredText() + " " + 
//							aChunk.getChunkValue().toString());
							if (aChunk.getChunkValue()!=null
							&& aChunk.getChunkValue().toString().equals("VP")){
							 if (kpaParts != null && kpaParts.length==1){
								//TO DO: get subsequent PP NP
								//travelling across the mediterranean sea
								//concerned about fracking
//								 System.out.println("IGNORED VP " + kpa.getCoveredText());
								kpBeginNew = kpa.getBegin();
								kpEndNew = kpa.getEnd();
								kpKeyPhraseNew = kpa.getCoveredText();
								//currently deleted
								kpa.removeFromIndexes();
								continue KPA;
							 }
							 else {
//								 System.out.println("IGNORED VP " + kpa.getCoveredText());
								 kpa.removeFromIndexes();
									continue KPA;
							 }
							}
							else if (aChunk.getChunkValue()!=null
							&& aChunk.getChunkValue().toString().equals("NP")){
							kpKeyPhraseNew = aChunk.getCoveredText();
							
							List<Token> tokens= JCasUtil.selectCovered(aJCas,
									Token.class, aChunk);
							
				TOK:			for (Token aToken : tokens){
									while (kpKeyPhraseNew.startsWith(" ")){
										kpKeyPhraseNew = kpKeyPhraseNew.substring(1);
				//						System.out.println("BLANKWEG" + kpKeyPhraseNew);
									}
									if (aToken.getPos().getPosValue().toString() != null
											&& skipAsInitialPosTag.contains(aToken.getPos().getPosValue().toString())
											){
	//								System.out.println("MORPH BEG " + aToken.getPos().getPosValue().toString() 
	//										+ aToken.getBegin() + "-" + aToken.getEnd());
									kpKeyPhraseNew = kpKeyPhraseNew.substring(aToken.getEnd() - aToken.getBegin());
									
	//								System.out.println("KPSUBCHUNK " + kpKeyPhraseNew);
									continue TOK;
									}
									//delete STOPWORDS at beginning of phrase
									else if (aToken.getCoveredText() != null
											&& (ke.getStopwords().isStopword(aToken.getCoveredText().toLowerCase())
											|| skipAsInitialToken.contains(aToken.getCoveredText().toLowerCase())	
										    )
											){
//									System.out.println("STOPWORD BEG " + aToken.getCoveredText() + " " + 
//											+ aToken.getBegin() + "-" + aToken.getEnd());
									
									if (aToken.getEnd() - aToken.getBegin() >= 0){
									kpKeyPhraseNew = kpKeyPhraseNew.substring(aToken.getEnd() - aToken.getBegin());
									}
									else {
//							    		System.out.println("IGNORE STRANGE " + kpKeyPhraseNew);
							    		kpa.removeFromIndexes();
										continue KPA;	
							    	}
									
//									System.out.println("KPSUBCHUNK " + kpKeyPhraseNew);
									continue TOK;
									}
									
									else {
									kpBeginNew = aToken.getBegin();
//									System.out.println("FIRST " + aToken.getCoveredText() + " " + aToken.getBegin());
									break TOK;
									}
								}
							
							
							while (kpKeyPhraseNew.startsWith(" ") || kpKeyPhraseNew.startsWith("*")){
								kpKeyPhraseNew = kpKeyPhraseNew.substring(1);
//								System.out.println("BLANKWEG" + kpKeyPhraseNew);
							}
							boolean endModified = false;
			TOKBACKWARDS:		for (i = tokens.size() - 1; i >= 0; i--) {
								while (kpKeyPhraseNew.endsWith(" ") || kpKeyPhraseNew.endsWith("*")
										|| kpKeyPhraseNew.endsWith("'")){
									kpKeyPhraseNew = kpKeyPhraseNew.substring(0,kpKeyPhraseNew.length()-1);
//									System.out.println("FINAL BLANKWEG" + kpKeyPhraseNew);
								}
							    if (tokens.get(i).getPos().getPosValue().toString() != null
										&& (skipAsFinalPosTag.contains(tokens.get(i).getPos().getPosValue().toString())
										|| skipAsInitialToken.contains(tokens.get(i).getCoveredText())
												)
										){
//								System.out.println("MORPH END " + tokens.get(i).getCoveredText()  
//										+ tokens.get(i).getCoveredText().length());
							    	int tokenlength = tokens.get(i).getCoveredText().length();
							    	if (kpKeyPhraseNew.length() - tokenlength - 1 > 0){
								kpKeyPhraseNew = kpKeyPhraseNew.substring(0, kpKeyPhraseNew.length() - tokenlength);
								endModified = true;
							    	}
							    	else {
//							    		System.out.println("IGNORE STRANGE " + kpKeyPhraseNew);
							    		kpa.removeFromIndexes();
										continue KPA;	
							    	}
								
//								System.out.println("KPSUBCHUNK " + kpKeyPhraseNew);
								continue TOKBACKWARDS;
								}
								else {
								kpEndNew = tokens.get(i).getEnd();
//								System.out.println("FIRST " + aToken.getCoveredText() + " " + aToken.getBegin());
								break TOKBACKWARDS;
								}
							    
							 }
							
							while (kpKeyPhraseNew.endsWith(" ")|| kpKeyPhraseNew.endsWith("*")){
								kpKeyPhraseNew = kpKeyPhraseNew.substring(0,kpKeyPhraseNew.length()-1);
//								System.out.println("FINAL BLANKWEG" + kpKeyPhraseNew);
							}
							if (kpEndNew == -1){
							Token lastToken = tokens.get(tokens.size()-1);
							kpEndNew = lastToken.getEnd();
//							System.out.println("LAST " + lastToken.getCoveredText());
							}
							
							
							
							
//						    //activate PPs if NP is DBPedia entry	
//							System.out.println("CHUNK" + kpKeyPhraseNew + " " + kpEndNew);
							if (!endModified && EXTEND_TO_PP
//									 && (( DBPEDIA_FILTER && dbpediaCoveredText.contains(kpKeyPhraseNew.toLowerCase()))
//										   || true //NEW
//									       || kpRankNew > 5 
//									       || kpKeyPhraseNew.split(" ").length == 1
//									       || dbpediaCoveredText.contains(kpOri.toLowerCase())
//									      
//											 )
//									
									){
//										System.out.println("NOTENDMODIFIED:" + kpKeyPhraseNew + kpEndNew);
										
									
							
                            	if (followingChunks.size() == 2 && 
                            			followingChunks.get(0).getChunkValue().toString().equals("PP")
                            			&& followingChunks.get(1).getChunkValue().toString().equals("NP")
                            			&& followingChunks.get(0).getBegin() == kpEndNew + 1 //energy. After
                            			){
                            		String postnominalPp = followingChunks.get(1).getCoveredText();
                            		String postnominalPrep = followingChunks.get(0).getCoveredText();
//                            		System.out.println("PREP" + postnominalPrep);
//                            		System.out.println("PPFULL" + kpKeyPhraseNew + " " + postnominalPrep + " "+ postnominalPp);	
                            		String[] newNpSplit = postnominalPp.split(" ");
                            		
            						boolean ppOk = true;

//            						if (skipAsPreposition.contains(postnominalPrep)){
            						if (!postnominalPreposition.contains(postnominalPrep)){
            							ppOk = false;
//            							System.out.println("BLOCKEDPREP" + postnominalPrep);
//            							System.out.println("PPFULL" + kpKeyPhraseNew + " " + postnominalPrep + " "+ postnominalPp);	
            						}
            						if (newNpSplit != null) {
//            							for (i = newNpSplit.length - 1; i >= 0; i--) {
            							if (newNpSplit[0].equals("the") || newNpSplit[0].equals("a") || newNpSplit[0].equals("an")){
//            								System.out.println("STOPWORD THE OK" + kpKeyPhraseNew + " " + postnominalPrep + " "+ postnominalPp);		
            			
            							}
            							else if (ke.getStopwords().isStopword(newNpSplit[0].toLowerCase())
            									){
            								ppOk = false;
//            								System.out.println("IGNORE STOPWORD " + kpKeyPhraseNew + " " + postnominalPrep + " "+ postnominalPp);		
            							}
            							if (
           									 ke.getStopwords().isStopword(newNpSplit[newNpSplit.length-1].toLowerCase())
           									){
           								ppOk = false;
//           								System.out.println("IGNORE STOPWORD " + kpKeyPhraseNew + " " + postnominalPrep + " "+ postnominalPp);		
           							}
//            							xxx
//            						}
            						}
            						//NEW
//            						if (dbpediaCoveredText.contains(postnominalPp)){
//            							ppOk = false;
////            							System.out.println("DBPEDIA in PP" + postnominalPp);
//            						}
//            						System.out.println("UPPER CASE PREP CHECK" + postnominalPrep.charAt(0) + "XX"+ postnominalPrep);
            						if (Character.isUpperCase(postnominalPrep.charAt(0))){ //Europe In Germany
            							ppOk = false; 
//            							System.out.println("UPPER CASE PREP" + postnominalPrep);
            						}
            						   
            				NER:		for (NamedEntity ner: namedEntitiesList){
//            							int nerBegin = ner.getBegin();
//            					        int nerEnd = ner.getEnd();
            					        String nerText = ner.getCoveredText().toLowerCase();
//            					        System.out.println("NER" + nerText);
            					        if (nerText.equalsIgnoreCase(kpKeyPhraseNew)
            					        	|| 	nerText.equalsIgnoreCase(postnominalPp)
            					        	|| nerText.equalsIgnoreCase(kpOri)
            					        	|| postnominalPp.toLowerCase().contains(nerText) //NEW
            					        		){
            					        	ppOk = false;
//                							System.out.println("NER in PP" + kpKeyPhraseNew + " " + postnominalPrep + " "+ postnominalPp);
            					            break NER;
            					        }         					 
            						}
            						
            						
            						
            						
            						
            						
//            						if (kpText.contains(postnominalPp)){
//            							System.out.println("PP is KP " + postnominalPp);	
//            							ppOk = true;
//            						}
            						
            						
            						if (ppOk){
//            							System.out.println("KPBEFORE" + kpKeyPhraseNew + kpEndNew);
            							kpKeyPhraseNew = kpKeyPhraseNew + " " + postnominalPrep + " " + postnominalPp;
            							kpEndNew = followingChunks.get(1).getEnd();
//            							System.out.println("PPAFTERCHUNK" + kpKeyPhraseNew + " " + kpEndNew);
        							}
                            		
                            		
//                            		System.out.println("KPAFTERCHUNK" + kpKeyPhraseNew + " " + kpEndNew);
                            		 
                            	}
                            	
                            	//precedingChunk often fill words like result of ..
//                            	if (precedingChunks.size() == 2 && precedingChunks.get(1).getChunkValue().toString().equals("PP")
//                            			&& precedingChunks.get(0).getChunkValue().toString().equals("NP")
//                            			){
//                            		System.out.println("KPBEFORECHUNK"  + "NP" + precedingChunks.get(0).getCoveredText()
//                            				+ "PP" + precedingChunks.get(1).getCoveredText() + "KEA" + kpKeyPhraseNew);
//                            		 
//                            	}
							}
							
							//remove: war ** History
							Pattern p = Pattern.compile("\\*\\*");
							Matcher m = p.matcher(kpKeyPhraseNew);
							if (m.find()){
								kpa.removeFromIndexes();
								continue KPA;
							}
							p = Pattern.compile("_");
							m = p.matcher(kpKeyPhraseNew);
							if (m.find()){
								kpa.removeFromIndexes();
								continue KPA;
							}
							p = Pattern.compile("\"");
							m = p.matcher(kpKeyPhraseNew);
							if (m.find()){
								kpa.removeFromIndexes();
								continue KPA;
							}
							
							
							
							
//							System.out.println("KPNEWCHUNK" + kpKeyPhraseNew + " " + kpEndNew);
							
							} //NEW NP
							else {
//								System.out.println("IGNORED CHUNK TYPE " + aChunk.getChunkValue().toString() + " " + kpa.getCoveredText());
								kpa.removeFromIndexes();
								continue KPA;
							}
						} //for chunks
						
						
						
						String[] kpaSplit = kpKeyPhraseNew.split(" ");
						
						if (kpaSplit != null && kpaSplit.length > 1) {
							if (ke.getStopwords().isStopword(kpaSplit[0].toLowerCase())
									|| ke.getStopwords().isStopword(kpaSplit[kpaSplit.length-1].toLowerCase())
									){
//								System.out.println("IGNORE STOPWORD " + kpKeyPhraseNew);
								kpa.removeFromIndexes();
								continue KPA;
							}
						}
						
						
//						if (LOAD_MANUAL_KEYPHRASES && manualKeyPhrases.contains(kpa.getCoveredText().toLowerCase())){
////							System.out.println("KPORIMANUAL" + kpa.getCoveredText());
//						kpa.addToIndexes(); //better, but CAUTION: does not recalculate rank
//						//shows potential but not licit technique
//						}
//						if (LOAD_MANUAL_KEYPHRASES &&
//								!kpa.getCoveredText().toLowerCase().equals(kpKeyPhraseNew.toLowerCase())){	
//							//shows potential but not licit technique
////							System.out.println("KPNEWCHUNK" + kpKeyPhraseNew);
//							if (manualKeyPhrases.contains(kpKeyPhraseNew.toLowerCase())){
////								System.out.println("KPMANUALCHUNK" + kpKeyPhraseNew);
//							}
//							else {
////								System.out.println("KPIGNORECHUNK" + kpKeyPhraseNew);
//								kpa.removeFromIndexes();
//								continue KPA;
//							}
//							}
						
						//Fracking -> fracking
						if (upCapTolowCap.containsKey(kpKeyPhraseNew)){
							kpKeyPhraseNew =upCapTolowCap.get(kpKeyPhraseNew);
//							System.out.println("CORRECT CHUNK LOW CAP" + kpKeyPhraseNew);
						}
//						} //if noChunksFound //NEW
					} // EXTENDKEYPHRASETOCHUNK
					
					
					if (DBPEDIA_FILTER
							&& dbpediaCoveredText.contains(kpKeyPhraseNew.toLowerCase().trim())){
//								System.out.println("DBPEDIA FILTER:" + kpKeyPhraseNew.toLowerCase() + "RANK" + kpa.getRank());
								kpa.removeFromIndexes();
								continue KPA;
							}
					else if (standaloneStopwordFilter && standaloneStopWordSet.contains(kpKeyPhraseNew)){
//						System.out.println("STANDALONE STOPWORD " + kpKeyPhraseNew);
						kpa.removeFromIndexes();
						continue KPA;
					}
					else if (kpKeyPhraseNew.trim().split("\\s+").length > MAX_NUMBER_OF_WORDS_IN_KEYPHRASE){
//						System.out.println("TOO MANY WORDS IN KP " + MAX_NUMBER_OF_WORDS_IN_KEYPHRASE + kpKeyPhraseNew);
						kpa.removeFromIndexes();
						continue KPA;
					}
					else if (kpKeyPhraseNew.trim().split("\\s+").length < MIN_NUMBER_OF_WORDS_IN_KEYPHRASE){
//						System.out.println("TOO FEW WORDS IN KP " + MIN_NUMBER_OF_WORDS_IN_KEYPHRASE + kpKeyPhraseNew);
						kpa.removeFromIndexes();
						continue KPA;
					}
					else if (rankGroupFound.contains(kpGroupInteger)){
//						System.out.println("RANKGROUP ALREADY " + kpKeyPhraseNew);
						kpa.removeFromIndexes();
						continue KPA;
					}
					else {
						for (String txt: kpText){
							if (txt.equalsIgnoreCase(kpKeyPhraseNew)){
								
							}
							else if (txt.contains(kpKeyPhraseNew.toLowerCase())){
//								System.out.println("SUBSTRING OF EXISTING " + kpKeyPhraseNew + "IN:" + txt);
								kpa.removeFromIndexes();
								continue KPA;
								
							}
							else if (kpKeyPhraseNew.toLowerCase().contains(txt)){
//								System.out.println("EXISTING CONTAINS ACTUAL" + txt + "IN:" + kpKeyPhraseNew);
								kpa.removeFromIndexes();
								continue KPA;
							}
							else if (kpKeyPhraseNew.endsWith("y")){
								if (txt.contains(kpKeyPhraseNew.substring(0,kpKeyPhraseNew.length()-1).toLowerCase())){
//									System.out.println("SUBSTRING OF EXISTING " + kpKeyPhraseNew + "IN:" + txt);
									kpa.removeFromIndexes();
									continue KPA;
									
								}
								else if (txt.endsWith("y")){
									if (kpKeyPhraseNew.toLowerCase().contains(txt.substring(0,txt.length()-1))){
//										System.out.println("EXISTING CONTAINS ACTUAL" + txt + "IN:" + kpKeyPhraseNew);
										kpa.removeFromIndexes();
										continue KPA;
									}
								}
							}
						}
					}
					
							if (kpBeginNew != -1 && kpEndNew != -1){
								kpa.removeFromIndexes();
								kpBeginEndNew = "" + kpBeginNew + "-" + kpEndNew;
								if (kpChunkBasedBeginEnd.contains(kpBeginEndNew)){
//									System.out.println("ANNO DOUBLE " +kpKeyPhraseNew);
									continue KPA;
								}
								
								if (!kpKeyPhraseNew.equalsIgnoreCase(kpOri)){
//									System.out.println("ANNO IDENT " +kpKeyPhraseNew);
									countChunkKpPerKp++;
									
								}

								
								if (kpRankNew == oldRank){
									addToRank = addToRank + 1;
									}
									else {
										oldRank = kpRankNew;
									}
								finalRank = kpRankNew+addToRank;
								if(kpText.contains(kpKeyPhraseNew.toLowerCase())){
//								System.out.println("BEFORE RERANK EXISTING " + kpKeyPhraseNew + " " + finalRank);
										if ( !kpChunkBasedRank.isEmpty() && kpChunkBasedRank.containsKey(kpKeyPhraseNew.toLowerCase())
												&& (kpChunkBasedRank.get(kpKeyPhraseNew.toLowerCase()) < finalRank)
										){
//											System.out.println("RERANK EXISTING " + kpKeyPhraseNew + " " + kpRankNew + "::" + kpChunkBasedRank.get(kpKeyPhraseNew.toLowerCase()));
											finalRank = kpChunkBasedRank.get(kpKeyPhraseNew.toLowerCase());
									
										}
								}
//								else if (standaloneStopwordFilter && standaloneStopWordSet.contains(kpKeyPhraseNew)){
//									System.out.println("STANDALONE STOPWORD " + kpKeyPhraseNew);
//									continue KPA;
//								}
//								else if (kpKeyPhraseNew.trim().split("\\s+").length > MAX_NUMBER_OF_WORDS_IN_KEYPHRASE){
//									System.out.println("TOO MANY WORDS IN KP " + MAX_NUMBER_OF_WORDS_IN_KEYPHRASE + kpKeyPhraseNew);
//									continue KPA;
//								}
								else if (keyphraseCount <= MAX_NUMBER_OF_KEYPHRASES){
									kpText.add(kpKeyPhraseNew.toLowerCase());
									kpChunkBasedRank.put(kpKeyPhraseNew.toLowerCase(), finalRank);
									keyphraseCount++;
									rankGroupFound.add(kpGroupInteger);
								}
								else if (keyphraseCount > MAX_NUMBER_OF_KEYPHRASES){
//									System.out.println("MAX REACHED NEW NOT ADDED " + kpKeyPhraseNew);
									break KPA;
								}
								
								
								
								kpa.setKeyPhrase(kpKeyPhraseNew);
								kpa.setBegin(kpBeginNew);
								kpa.setEnd(kpEndNew);
								kpa.setRank(finalRank);
								
								kpa.setRankGroup(kpGroup);
								kpBeginEndNew = "" + kpBeginNew + "-" + kpEndNew;
								kpChunkBasedBeginEnd.add(kpBeginEndNew);
								
//								System.out.println("KPFINAL" + kpKeyPhraseNew + 
//										"RANK:COUNT:END " + finalRank + ":" + keyphraseCount + ":" + kpEndNew);
//								System.out.println("OFFSETS " + kpa.getBegin() + "-" + kpa.getEnd());
								kpa.addToIndexes();
							}
			} 
		}//end KPA:
		
		
		
		
		
		
	}
	
	final class DeterminePosResult {
	    private final HashMap<String,Integer> first;
	    private final HashSet<String> second;
	    private final HashMap<String,String> third;
	    public DeterminePosResult( HashMap<String,Integer> first, HashSet<String> second, HashMap<String,String> third) {
	        this.first = first;
	        this.second = second;
	        this.third = third;
	    }

	    public  HashMap<String,Integer> getPosSequenceCount() {
	        return first;
	    }

	    public HashSet<String> getKeyFound() {
	        return second;
	    }
	    
	    public  HashMap<String,String> getKeyPos() {
	        return third;
	    }
	}
	

	public DeterminePosResult determinePos (List<Token> tokens, HashSet<String> keyPhrases, JCas aJCas, 
			HashMap<String,Integer> posSequenceCount, HashSet<String>  keyFound, HashMap<String,String> keyPos){
//		HashSet<String>  manualKeyFound = new HashSet<String>();
//		HashMap<String,Integer> posSequenceCount = new HashMap<String,Integer>();
//		HashMap<String,String> keyPos = new HashMap<String,String>();
		
		TOK:		for (Token aToken : tokens){
//			System.out.println("TOKEN FOUND1" + aToken.getCoveredText()); 
				for (String keyPhrase : keyPhrases){
//					System.out.println("MK" + manualKeyPhrase);
					String [] wordsInKeyphrase = keyPhrase.split("\\s+"); 
					int lengthOfKeyphrase = wordsInKeyphrase.length;
					String [] pos = new String [lengthOfKeyphrase];
					boolean successful = false;
					
					if (wordsInKeyphrase[0].equals(aToken.getCoveredText().toLowerCase())){
//						System.out.println("EQUAL TOKEN" + aToken.getCoveredText() + "POS" + aToken.getPos().getPosValue().toString() + " MK" + manualKeyPhrase);
						
						if (aToken.getPos().getPosValue().toString() != null){
						pos[0] = aToken.getPos().getPosValue().toString();
						}
						
						String [] wordsTextualKeyphrase = new String [lengthOfKeyphrase];
						wordsTextualKeyphrase[0] = aToken.getCoveredText().toLowerCase();
						if (lengthOfKeyphrase == 1){
							successful = true;
						}
						else if (lengthOfKeyphrase > 1){
						List<Token> tokensKey = JCasUtil.selectFollowing(aJCas, Token.class, aToken, lengthOfKeyphrase-1);
					    if (tokensKey != null){
					    	int i = 1;
						for (Token tokenKey: tokensKey){
							if (aToken.getPos().getPosValue().toString() != null && 
									!aToken.getPos().getPosValue().toString().equals(";")){ //semicolon is separator in StringBuilder value
							pos[i] = tokenKey.getPos().getPosValue().toString();
							
							wordsTextualKeyphrase[i] = tokenKey.getCoveredText().toLowerCase();
							}
							i++;
//					    	System.out.println("SINGLE TOK" + tokenManKey.getCoveredText());
//					    	System.out.println("SINGLE POS" + tokenManKey.getPos().getPosValue().toString());
					    }
						
					    }
					}
						if (wordsInKeyphrase != null && wordsTextualKeyphrase != null &&
								Arrays.toString(wordsInKeyphrase).equals(Arrays.toString(wordsTextualKeyphrase))){
							successful = true;
//							System.out.println("SUCCESS" + Arrays.toString(pos));
							String posString = Arrays.toString(pos);
							String keyString = Arrays.toString(wordsTextualKeyphrase);
							keyPos.put(keyString, posString);
							
							if (!keyFound.contains(keyString) && posSequenceCount.containsKey(posString)) {
								int posCount = posSequenceCount.get(posString) + 1;
//								System.out.println("ADD" + posString + posCount);
				            posSequenceCount.put(posString,posCount);
							}
							else if (!keyFound.contains(keyString)){
								 posSequenceCount.put(posString,1);
//								 System.out.println("NOADD" + posString + 1);
							}
							keyFound.add(keyString);
						}	
						
					}
				}
				}
	return new DeterminePosResult(posSequenceCount, keyFound,keyPos);
		}
	
	
	
	
	

	public KEAKeyphraseExtractor getKeyphraseExtractor() {
		return ke;
	}

	public void setKEAKeyphraseExtractor(KEAKeyphraseExtractor ke) {
		this.ke = ke;
	}

	
	
	
	
	
	
	
	
	
	
	
	private class KeyPhraseComparator implements Comparator<KeyPhrase> {

		@Override
		public int compare(KeyPhrase a, KeyPhrase b) {
			return b.getUnstemmed().split(" ").length - a.getUnstemmed().split(" ").length ;
		}

	}
	private class KeyPhraseByRankComparator implements Comparator<KeyPhrase> {

		@Override
		public int compare(KeyPhrase a, KeyPhrase b) {
			return a.getRank() - b.getRank() ;
		}

	}
	
	private class KeyPhraseAnnotationByRankLengthComparator implements Comparator<KeyPhraseAnnotation> {

		@Override
		public int compare(KeyPhraseAnnotation a, KeyPhraseAnnotation b) {
			int byRank = a.getRank() - b.getRank();
			
			if (byRank == 0){
				
			}
			return byRank != 0 ? byRank : b.getCoveredText().length() - a.getCoveredText().length();
		}

	}
	
	
		
	
}
