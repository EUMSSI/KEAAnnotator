package com.iai.uima.analysis_component;

import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
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
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.iai.uima.jcas.tcas.KeyPhraseAnnotation;
import com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated;
import com.iai.uima.jcas.tcas.KeyPhraseAnnotationEnriched;
import com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADJ;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADV;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.N;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.V;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Location;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Organization;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.Person;

public class KeyPhraseAnnotator extends JCasAnnotator_ImplBase {

	KEAKeyphraseExtractor ke = new KEAKeyphraseExtractor();

	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue = "en")
	private String LANGUAGE;

	public static final String PARAM_MODEL_LOCATION = "modelLocation";
	@ConfigurationParameter(name = PARAM_MODEL_LOCATION, mandatory = false)
	private String MODEL_LOCATION;

	public static final String PARAM_KEYPHRASE_RATIO = "ratioOfKeyPhrases";
	@ConfigurationParameter(name = PARAM_KEYPHRASE_RATIO, defaultValue = "50")
	private int KEAPHRASE_RATIO;

	public static final String PARAM_ADJ_NOUN_LIST = "adjNounList";
	@ConfigurationParameter(name = PARAM_ADJ_NOUN_LIST, defaultValue = "/com/iai/uima/kea/data/wordlists/en/ADJ-NOUN-relation.final")
	private String ADJ_NOUN_LIST;

	private Hashtable<String, String> adj_noun;


	public static final String PARAM_VERB_NOUN_LIST = "verbNounList";
	@ConfigurationParameter(name = PARAM_VERB_NOUN_LIST, defaultValue = "/com/iai/uima/kea/data/wordlists/en/VERB-NOUN-relation.final")
	private String VERB_NOUN_LIST;
	
	private Hashtable<String, String> verb_noun;
	
	public static final String PARAM_CITY_COUNTRY_LIST = "cityCountryList";
	@ConfigurationParameter(name = PARAM_CITY_COUNTRY_LIST,  mandatory=false, defaultValue = "/com/iai/uima/kea/data/wordlists/en/city-country-relation.final")
	private String CITY_COUNTRY_LIST;

	private Hashtable<String, String> city_country;

	public static final String PARAM_COUNTRY_REGION_LIST = "countryRegionList";
	@ConfigurationParameter(name = PARAM_COUNTRY_REGION_LIST,  mandatory=false, defaultValue = "/com/iai/uima/kea/data/wordlists/en/country-region-relation.final")
	private String COUNTRY_REGION_LIST;

	private Hashtable<String, String> country_region;

	public static final String PARAM_ABBREV_LONG_LIST = "abbrevLongList";
	@ConfigurationParameter(name = PARAM_ABBREV_LONG_LIST, mandatory=false ,defaultValue = "/com/iai/uima/kea/data/wordlists/en/abbrev_long.final")
	private String ABBREV_LONG_LIST;

	public static final String PARAM_STOPWORDLIST = "stopwordList";
	@ConfigurationParameter(name = PARAM_STOPWORDLIST, mandatory=false)
	private String STOPWORD_LIST;

	private Hashtable<String, String> abbrev_long;

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

		BufferedReader read_adj_noun;
		BufferedReader read_verb_noun;
		BufferedReader read_city_country;
		BufferedReader read_country_region;
		BufferedReader read_abbrev_long;
		if (MODEL_LOCATION == null)
			MODEL_LOCATION = "/com/iai/uima/kea/data/models/" + LANGUAGE + "/model";
//			System.out.println("MODEL_LOCATION: " + MODEL_LOCATION + " lang: " + LANGUAGE);
		if (STOPWORD_LIST == null)
			STOPWORD_LIST = "/com/iai/uima/kea/data/stopwords/stopwords_" + LANGUAGE + ".txt";
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

			adj_noun = new Hashtable<String, String>();

			while ((line = read_adj_noun.readLine()) != null) {
				String[] content = line.split("\t");
				adj_noun.put(content[0], content[1]); 
			}
			read_adj_noun.close();
			
			verb_noun = new Hashtable<String, String>();

			while ((line = read_verb_noun.readLine()) != null) {
				String[] content = line.split("\t");
//				verb_noun.put(content[0], content[1]); //ORI
//				System.out.println(getStemmer(LANGUAGE).stemString(content[0]) + content[1]);
				verb_noun.put(getStemmer(LANGUAGE).stemString(content[0]), content[1]); //SPnew
			}
			read_verb_noun.close();

			city_country = new Hashtable<String, String>();

			while ((line = read_city_country.readLine()) != null) {
				String[] content = line.split("\t");
				city_country.put(content[0], content[1]);
			}
			read_city_country.close();

			country_region = new Hashtable<String, String>();

			while ((line = read_country_region.readLine()) != null) {
				String[] content = line.split("\t");
				country_region.put(content[0], content[1]);
			}
			read_country_region.close();

			abbrev_long = new Hashtable<String, String>();

			while ((line = read_abbrev_long.readLine()) != null) {
				String[] content = line.split("\t");
				abbrev_long.put(content[0], content[1]);
			}
			read_abbrev_long.close();

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

		ke.setNumPhrases(numOfwords / KEAPHRASE_RATIO);

		ArrayList<KeyPhrase> keyPhrases = ke.extractKeyphrasesToList(aJCas
				.getDocumentText());



		Collections.sort(keyPhrases, new KeyPhraseComparator());

//		HashSet<KeyPhrase> deprecatedKeyPhrases = new HashSet<KeyPhrase>();
		HashSet<String> deprecatedKeyPhrases = new HashSet<String>();
		
		for (KeyPhrase kp : keyPhrases) {
			
			
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
			
//			System.out.println("KEYPHRASE "  + isKeyPhraseDeprecated + " " + containsStopword + " "+ kp.getUnstemmed());
			
			String keyPhraseReplacee = kp.getUnstemmed();

			String[] split = kp.getUnstemmed().split(" ");
			for (String sw : split) {
				if (ke.getStopwords().isStopword(sw)){
//					System.out.println("STOPWORD " + sw + " " +  kp.getUnstemmed());
					isKeyPhraseDeprecated = containsStopword = true;
				}
			}
			
			
			Collection<ADJ> adjs = select(aJCas, ADJ.class);
			Collection<V> verbs = select(aJCas, V.class);
			Collection<ADV> adverbs = select(aJCas, ADV.class);
			Collection<Person> namedPersons = select(aJCas, Person.class);
			Collection<Location> namedLocations = select(aJCas, Location.class);
			Collection<Organization> namedOrganizations = select(aJCas, Organization.class);
			
			HashSet<ADJ> adjsList = new HashSet<ADJ>(adjs);
			HashSet<V> verbsList = new HashSet<V>(verbs);
			HashSet<ADV> adverbsList = new HashSet<ADV>(adverbs);
			HashSet<Person> namedPersonsList = new HashSet<Person>(namedPersons);
			HashSet<Location> namedLocationsList = new HashSet<Location>(namedLocations);
			HashSet<Organization> namedOrganizationsList = new HashSet<Organization>(namedOrganizations);

			
			//SP unclear whether the following is needed..
			for (N noun : select(aJCas, N.class)) {
				for (ADJ adj : adjs)
					if (!noun.getCoveredText().equalsIgnoreCase( //NEG correct?
							adj.getCoveredText()))
						adjsList.remove(adj);
				for (V verb : verbs)
					//					if (!noun.getCoveredText().equalsIgnoreCase(  // ORI NEG wrong
					if (noun.getCoveredText().equalsIgnoreCase(  //SPneu
							verb.getCoveredText()))
						verbsList.remove(verb);
				//SPnew no resctriction on ADV
				//				for (ADV adv : adverbs)
				//					if (noun.getCoveredText().equalsIgnoreCase( //NEG !noun deleted SPnew
				//							adv.getCoveredText()))
				//						adverbsList.remove(adv);
			}

			for (ADJ adj : adjsList){
				//keyphrases that end with ADJ are deprecated
				if ((adj.getPosValue().equals("JJ")
						|| adj.getPosValue().equals("JJS") || adj.getPosValue()
						.equals("JJR"))
						&& kp.getUnstemmed().endsWith(adj.getCoveredText().toLowerCase())
						&& !kp.getUnstemmed().equalsIgnoreCase(adj.getCoveredText())){
					System.out.println("DEPRECATED 2 " + kp.getUnstemmed());
					isDeprecatedEndsWithAdjective = isKeyPhraseDeprecated = true;
				}
		   }
			//SPnew: keyphrases that contain ADV are deprecated
			for (ADV adv : adverbsList){
				if (kp.getUnstemmed().contains(adv.getCoveredText().toLowerCase())){ //SPnew
					//					if (kp.getUnstemmed().endsWith(adv.getCoveredText().toLowerCase())  //ORI
					//						&& !kp.getUnstemmed().equalsIgnoreCase(adv.getCoveredText()))  //ORI
					isDeprecatedContainsAdverb = isKeyPhraseDeprecated = true;
					System.out.println("DEPRECATED 3 " + kp.getUnstemmed());
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
			
	PER:	for (Person person : namedPersonsList){
//				System.out.println("NER found " +  person.getCoveredText());
			//accept keyphrases that equal one NER annotation in the text, additionallyaccept
		    //keyphrases that correspond to the last name of a Person.
		    //names (and last names) are also exempted from the isContainedInLongerKeyphrase filter.
		
		        //OK: kp equals NER
		 		if (kp.getUnstemmed().toLowerCase().contains(person.getCoveredText().toLowerCase())
		 			&& kp.getUnstemmed().equalsIgnoreCase(person.getCoveredText())
		 			){
//		 			System.out.println("PER OK EQ FULL " +  kp.getUnstemmed() + ":" + person.getCoveredText());
		 			kpIsOkNer = true;
		 			isDeprecatedContainsIncompleteNer = false;
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
					isDeprecatedContainsIncompleteNer = false;
					break ORG;
				}
				else if (isUpperCase2(kp.getUnstemmed().toString())
						&& organization.getCoveredText().contains(kp.getUnstemmed())){
//					System.out.println("ORG OK ALLCAP  " +  kp.getUnstemmed() + ":" + organization.getCoveredText());
					kpIsOkNer = true;
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
					//kp contains NER
					if (containsFirstName && containsLastName 
					&& kp.getUnstemmed().toLowerCase().contains(organization.getCoveredText())
						){
//						System.out.println("ORG OK CONT FULL " +  kp.getUnstemmed() + ":" + organization.getCoveredText());
						kpContainsOkNer = true;
						isDeprecatedContainsIncompleteNer = false;
						break ORG;
					}
				}
			}
         }
         
         
         
         
			if (isDeprecatedContainsIncompleteNer == true){
//				System.out.println("DEP NER kp " +  kp.getUnstemmed());
				isKeyPhraseDeprecated = true;
			}
			
			for (V finV : verbsList){
				// keyphrases that are finite verbs are deprecated
				if ((finV.getPosValue().equals("VBD")
						|| finV.getPosValue().equals("VBZ")
						|| finV.getPosValue().equals("VBP")
						|| finV.getPosValue().equals("VB") 
						|| finV.getPosValue().equals("VBN"))
						&& kp.getUnstemmed().contains(finV.getCoveredText().toLowerCase())
						&& kp.getUnstemmed().equalsIgnoreCase(finV.getCoveredText()))
				{
					for (String verb : verb_noun.keySet()){
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
//							   System.out.println("Replace verb_noun " +  kp.getUnstemmed() + " " + keyPhraseReplacee);
							   isKeyPhraseReplaced = isVerbReplacedWithNoun = true;
					    }
					}	
                if (isKeyPhraseReplaced == false){			
//					System.out.println("Hello, World FINITE " + kp.getUnstemmed().toString());
					isDeprecatedEndsWithFiniteVerb  = isKeyPhraseDeprecated = true;
                }
				}
			}	
			
			for (String adj : adj_noun.keySet()){
				//				if (kp.getUnstemmed().toLowerCase().equals(kp.getStemmed().toLowerCase() + "s")){
				//					System.out.println("Hello, World HACK");
				//				}
				if (adj.equalsIgnoreCase(kp.getUnstemmed())){ 
					keyPhraseReplacee = adj_noun.get(adj);
//					System.out.println("Hello, World Replace adj_noun " +  kp.getUnstemmed() + " " + keyPhraseReplacee);
					isKeyPhraseReplaced = true;
				}
				// HACK to account for Germans -> Germany, the adj_noun list contains only german germany.
				// if the stemmed and unstemmed version of the adj differs only with respect to a word-final s then
				// the mapping applies.
				// Using stemmed versions is too risky since stems are too general crofting -> croft should apply
				// but croft -> croft or should not apply

				else if (LANGUAGE == "en" && (kp.getUnstemmed().toLowerCase().equals(kp.getStemmed().toLowerCase() + "s")) && 
					adj.equalsIgnoreCase(kp.getStemmed())){
//					System.out.println("Hello, World HACK" + kp.getStemmed().toString() + " " + keyPhraseReplacee);
					keyPhraseReplacee = adj_noun.get(adj);
					isKeyPhraseReplaced = true;
				}
			}
			
			
			for (String country : country_region.keySet()){
				if (country.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = country_region.get(country);
					//SPneu
//					System.out.println("Hello, World Replace country_region " + keyPhraseReplacee + kp.getUnstemmed());
					isKeyPhraseEnriched = true;
					isEnrichedWithRegion = true;
				}
			}

			for (String city : city_country.keySet()){
				if (city.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = city_country.get(city);
//					System.out.println("Hello, World Replace city_country " + keyPhraseReplacee + kp.getUnstemmed());
					isKeyPhraseEnriched = true;
					isEnrichedWithCountry = true;
				}
			}

			for (String abbrev : abbrev_long.keySet()){
				if (abbrev.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = abbrev_long.get(abbrev);
//					System.out.println("Hello, World Replace long abbrev ");
					isKeyPhraseEnriched = true;
					isEnrichedWithLongForm = true;
				}
			}

			for (Entry<String, String> entry : abbrev_long.entrySet()){
				if (entry.getValue().equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = entry.getKey();
//					System.out.println("Hello, World Replace abbrev long ");
					isKeyPhraseEnriched = true;
					isEnrichedWithAbbreviation = true;
				}
			}

			if (isKeyPhraseDeprecated){
//				deprecatedKeyPhrases.add(kp); //ORI impossible to get string value??
//				System.out.println("DEPRECATED last " + isKeyPhraseDeprecated + " " + kp.getUnstemmed());
				deprecatedKeyPhrases.add(kp.getUnstemmed().toLowerCase()); //easier to store keyphrase-string
			}

			//SP error: matches German in Germany, word boundaries needed
//			Matcher matcher = Pattern.compile(kp.getUnstemmed()).matcher(
//					aJCas.getDocumentText());
			//SPnew token boundaries
			String patternString = String.format("(\\b)(%s)(\\b)", kp.getUnstemmed());
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(
					aJCas.getDocumentText());

			Collection<KeyPhraseAnnotation> addedKeyPhrases = select(aJCas, KeyPhraseAnnotation.class);

			//keyphrase is contained in other keyphrase and lower ranked than other (rank 0, 1, 2, ...)
			//exception for kp that is a correct NER
			if (kpIsOkNer == false){
	OTHER:		for (KeyPhraseAnnotation other : addedKeyPhrases) {
				
				if ( other.getKeyPhrase().toLowerCase().contains(kp.getUnstemmed().toLowerCase()) //ORI
						&&	!other.getKeyPhrase().equalsIgnoreCase(kp.getUnstemmed())	//ORI
						&& !deprecatedKeyPhrases.contains(other.getKeyPhrase().toLowerCase())){ 
					if (other.getRank() < kp.getRank()){
						isKeyPhraseDeprecated = isContainedInLargerKeyprase = true;
//						System.out.println("DEPRECATED kp isContainedInLargerKeyprase other: " + kp.getUnstemmed().toLowerCase());
						break OTHER;
						
					}
				}
			}
			}

			KeyPhraseAnnotation annotation;

			while (matcher.find()) {

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
				} else if (isKeyPhraseReplaced) {
//					System.out.println("REPLACED " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotationReplaced(aJCas);
					((KeyPhraseAnnotationReplaced) annotation)
				    .setIsAdjectiveReplacedWithNoun(isAdjectiveReplacedWithNoun); //SPnew
				((KeyPhraseAnnotationReplaced) annotation)  
				    .setIsVerbReplacedWithNoun(isVerbReplacedWithNoun);
				((KeyPhraseAnnotationReplaced) annotation)
					.setReplacee(keyPhraseReplacee);
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
					
				} else {
//					System.out.println("NORMAL " + kp.getUnstemmed());
					annotation = new KeyPhraseAnnotation(aJCas);
				}

				annotation.setKeyPhrase(kp.getUnstemmed());
				annotation.setProbability(kp.getProbability());
				annotation.setStem(kp.getStemmed());
				annotation.setRank(kp.getRank());
//				annotation.setBegin(matcher.start()); //ORI
//				annotation.setEnd(matcher.end());     //ORI
				annotation.setBegin(matcher.start(2));  //SPnew
				annotation.setEnd(matcher.end(2));      //SPnew
				annotation.setLanguage(LANGUAGE);
				annotation.addToIndexes();
				
			}
		}
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
}
