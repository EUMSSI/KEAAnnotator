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
import kea.stopwords.StopwordsEnglish;
import kea.stopwords.StopwordsFrench;
import kea.stopwords.StopwordsGerman;
import kea.stopwords.StopwordsSpanish;

import org.apache.uima.UimaContext;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
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

public class KeyPhraseAnnotator extends JCasAnnotator_ImplBase {

	KEAKeyphraseExtractor ke = new KEAKeyphraseExtractor();

	private static String KEA_HOME = System.getProperty("KEA_HOME");

	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue = "en")
	private String LANGUAGE;

	public static final String PARAM_KEYPHRASE_RATIO = "ratioOfKeyPhrases";
	@ConfigurationParameter(name = PARAM_KEYPHRASE_RATIO, defaultValue = "50")
	private int KEAPHRASE_RATIO;

	public static final String PARAM_ADJ_NOUN_LIST = "adjNounList";
	@ConfigurationParameter(name = PARAM_ADJ_NOUN_LIST, defaultValue = "/wordlists/ADJ-NOUN-relation.final")
	private String ADJ_NOUN_LIST;

	private Hashtable<String, String> adj_noun;

	public static final String PARAM_CITY_COUNTRY_LIST = "cityCountryList";
	@ConfigurationParameter(name = PARAM_CITY_COUNTRY_LIST,  mandatory=false, defaultValue = "/wordlists/city-country-relation.final")
	private String CITY_COUNTRY_LIST;

	private Hashtable<String, String> city_country;

	public static final String PARAM_COUNTRY_REGION_LIST = "countryRegionList";
	@ConfigurationParameter(name = PARAM_COUNTRY_REGION_LIST,  mandatory=false, defaultValue = "/wordlists/country-region-relation.final")
	private String COUNTRY_REGION_LIST;

	private Hashtable<String, String> country_region;

	public static final String PARAM_ABBREV_LONG_LIST = "abbrevLongList";
	@ConfigurationParameter(name = PARAM_ABBREV_LONG_LIST, mandatory=false ,defaultValue = "/wordlists/abbrev_long.final")
	private String ABBREV_LONG_LIST;

	private Hashtable<String, String> abbrev_long;

	private Stemmer getStemmer(String lang) {
		return lang.equals("es") ? new SpanishStemmer()
				: lang.equals("fr") ? new FrenchStemmer()
						: lang.equals("en") ? new PorterStemmer() : lang
								.equals("de") ? new GermanStemmer() : null;
	}

	private Stopwords getStopwords(String lang) {
		return lang.equals("es") ? new StopwordsSpanish()
				: lang.equals("fr") ? new StopwordsFrench()
						: lang.equals("en") ? new StopwordsEnglish() : lang
								.equals("de") ? new StopwordsGerman() : null;
	}

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);

		BufferedReader read_adj_noun;
		BufferedReader read_city_country;
		BufferedReader read_country_region;
		BufferedReader read_abbrev_long;

		String line;

		try {
			
			read_adj_noun = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(ADJ_NOUN_LIST)));
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
		// 8. Stopwords
		ke.setStopwords(getStopwords(LANGUAGE));

		// 10. Set to true, if you want to compute global dictionaries from the
		// test collection
		ke.setBuildGlobal(false);

		ke.setModelName(KEA_HOME + "/data/models/" + LANGUAGE + "/model");

		try {
			ke.loadModel();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {

		int numOfwords = aJCas.getDocumentText().split("[^\\s]").length;

		ke.setNumPhrases(numOfwords / KEAPHRASE_RATIO);

		ArrayList<KeyPhrase> keyPhrases = ke.extractKeyphrasesToList(aJCas
				.getDocumentText());
		
		Collections.sort(keyPhrases, new KeyPhraseComparator());
		
		HashSet<KeyPhrase> deprecatedKeyPhrases = new HashSet<KeyPhrase>();
		
		for (KeyPhrase kp : keyPhrases) {
			
			boolean containsStopword = false;
			boolean isContainedInLargerKeyprase = false;
			boolean isContainedInDeprecatedKeyPhrase = false;
			boolean isDeprecatedEndsWithAdjective = false;
			boolean isDeprecatedEndsWithFiniteVerb = false;
			/* Depricated keyphrase */
			boolean isKeyPhraseDeprecated = false;
			/* Replaced keyphrase */
			boolean isKeyPhraseReplaced = false;
			/* Enhriched Keyphrase */
			boolean isKeyPhraseEnriched = false;
			
			boolean isEnrichedWithAbbreviation = false;
			boolean isEnrichedWithCountry = false;
			boolean isEnrichedWithLongForm = false;
			boolean isEnrichedWithRegion = false;
			
			String keyPhraseReplacee = kp.getUnstemmed();

			String[] split = kp.getUnstemmed().split(" ");
			for (String sw : split) {
				if (ke.getStopwords().isStopword(sw))
					isKeyPhraseDeprecated = containsStopword = true;
			}
			
			Collection<ADJ> adjs = select(aJCas, ADJ.class);
			Collection<V> verbs = select(aJCas, V.class);
			Collection<ADV> adverbs = select(aJCas, ADV.class);
			
			HashSet<ADJ> adjsList = new HashSet<ADJ>(adjs);
			HashSet<V> verbsList = new HashSet<V>(verbs);
			HashSet<ADV> adverbsList = new HashSet<ADV>(adverbs);
			
			for (N noun : select(aJCas, N.class)) {
				for (ADJ adj : adjs)
					if (!noun.getCoveredText().equalsIgnoreCase(
							adj.getCoveredText()))
						adjsList.remove(adj);
				for (V verb : verbs)
					if (!noun.getCoveredText().equalsIgnoreCase(
							verb.getCoveredText()))
						verbsList.remove(verb);
				for (ADV adv : adverbs)
					if (!noun.getCoveredText().equalsIgnoreCase(
							adv.getCoveredText()))
						adverbsList.remove(adv);
			}

			for (ADJ adj : adjsList)
				if ((adj.getPosValue().equals("JJ")
						|| adj.getPosValue().equals("JJS") || adj.getPosValue()
						.equals("JJR"))
						&& kp.getUnstemmed().endsWith(adj.getCoveredText().toLowerCase())
						&& !kp.getUnstemmed().equalsIgnoreCase(adj.getCoveredText()))
					isDeprecatedEndsWithAdjective = isKeyPhraseDeprecated = true;
			for (ADV adv : adverbsList)
				if (kp.getUnstemmed().endsWith(adv.getCoveredText().toLowerCase())
						&& !kp.getUnstemmed().equalsIgnoreCase(adv.getCoveredText()))
					isDeprecatedEndsWithAdjective = isKeyPhraseDeprecated = true;
			for (V finV : verbsList)
				if ((finV.getPosValue().equals("VBD")
						|| finV.getPosValue().equals("VBZ")
						|| finV.getPosValue().equals("VBP")
						|| finV.getPosValue().equals("VB") || finV
						.getPosValue().equals("VBN"))
						&& kp.getUnstemmed().contains(finV.getCoveredText().toLowerCase())
						&& kp.getUnstemmed().equalsIgnoreCase(finV.getCoveredText()))
					isDeprecatedEndsWithFiniteVerb  = isKeyPhraseDeprecated = true;
			
			for (String adj : adj_noun.keySet())
				if (adj.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = adj_noun.get(adj);
					isKeyPhraseReplaced = true;
				}

			for (String country : country_region.keySet())
				if (country.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = country_region.get(country);
					isKeyPhraseEnriched = true;
					isEnrichedWithRegion = true;
				}
			
			for (String city : city_country.keySet())
				if (city.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = city_country.get(city);
					isKeyPhraseEnriched = true;
					isEnrichedWithCountry = true;
				}

			for (String abbrev : abbrev_long.keySet())
				if (abbrev.equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = abbrev_long.get(abbrev);
					isKeyPhraseEnriched = true;
					isEnrichedWithLongForm = true;
				}
			
			for (Entry<String, String> entry : abbrev_long.entrySet())
				if (entry.getValue().equalsIgnoreCase(kp.getUnstemmed())){
					keyPhraseReplacee = entry.getKey();
					isKeyPhraseEnriched = true;
					isEnrichedWithAbbreviation = true;
				}
			
			if (isKeyPhraseDeprecated)
				deprecatedKeyPhrases.add(kp);
			
			Matcher matcher = Pattern.compile(kp.getUnstemmed()).matcher(
					aJCas.getDocumentText());
			
			Collection<KeyPhraseAnnotation> addedKeyPhrases = select(aJCas, KeyPhraseAnnotation.class);
			
			for (KeyPhraseAnnotation other : addedKeyPhrases) {
				if ( other.getKeyPhrase().toLowerCase().contains(kp.getUnstemmed().toLowerCase())
					&&	!other.getKeyPhrase().equalsIgnoreCase(kp.getUnstemmed())){
//					&& 	!isKeyPhraseEnriched && !isKeyPhraseReplaced){
//						if (deprecatedKeyPhrases.contains(other))
//							isKeyPhraseDeprecated = isContainedInDeprecatedKeyPhrase = true;
						 if (other.getRank() < kp.getRank())
							isKeyPhraseDeprecated = isContainedInLargerKeyprase = true;
						 else
							 continue;
				}
				else if ( kp.getUnstemmed().toLowerCase().contains(other.getKeyPhrase().toLowerCase())
						&&	!kp.getUnstemmed().equalsIgnoreCase(other.getKeyPhrase())){
					if (other.getRank() > kp.getRank())
						isKeyPhraseDeprecated = isContainedInLargerKeyprase = true;
					 else
						 continue;
				}
			}
			
			KeyPhraseAnnotation annotation;

			while (matcher.find()) {

				if (isKeyPhraseDeprecated) {
					annotation = new KeyPhraseAnnotationDeprecated(aJCas);
					((KeyPhraseAnnotationDeprecated) annotation)
							.setContainsStopword(containsStopword);
					((KeyPhraseAnnotationDeprecated) annotation)
							.setEndsWithAdjective(isDeprecatedEndsWithAdjective);
					((KeyPhraseAnnotationDeprecated) annotation)
							.setEndsWithFiniteVerb(isDeprecatedEndsWithFiniteVerb);
					((KeyPhraseAnnotationDeprecated) annotation)
							.setEndsWithAdverb(isDeprecatedEndsWithAdjective);
					((KeyPhraseAnnotationDeprecated) annotation)
						.setIsContainedInLongerKeyPhrase(isContainedInLargerKeyprase);
					((KeyPhraseAnnotationDeprecated) annotation)
						.setIsContainedInDeprecatedKeyPhrase(isContainedInDeprecatedKeyPhrase);
				} else if (isKeyPhraseReplaced) {
					annotation = new KeyPhraseAnnotationReplaced(aJCas);
					((KeyPhraseAnnotationReplaced) annotation)
						.setIsAdjectiveReplacedWithNoun(true);
					((KeyPhraseAnnotationReplaced) annotation)
						.setReplacee(keyPhraseReplacee);
				} else if (isKeyPhraseEnriched) {
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
				} else
					annotation = new KeyPhraseAnnotation(aJCas);
				
				annotation.setKeyPhrase(kp.getUnstemmed());
				annotation.setProbability(kp.getProbability());
				annotation.setStem(kp.getStemmed());
				annotation.setRank(kp.getRank());
				annotation.setBegin(matcher.start());
				annotation.setEnd(matcher.end());
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
