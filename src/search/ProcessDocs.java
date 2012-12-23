package search;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import snowball.SnowballStemmer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class ProcessDocs {
	/**
	 * Tokenize string.
	 * 
	 * @param source
	 * @return
	 */
	public static ArrayList<String> tokenize(String source) {
		Reader reader = new StringReader(source);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		PTBTokenizer ptbTokenizer = new PTBTokenizer(reader,
				new CoreLabelTokenFactory(),
				"normalizeParentheses=false, normalizeOtherBrackets=false");

		ArrayList<String> tokens = new ArrayList<String>();

		for (CoreLabel label; ptbTokenizer.hasNext();) {
			label = (CoreLabel) ptbTokenizer.next();
			String tempWord = label.value().toLowerCase();

			if (!isPunctuation(tempWord)) {
				tokens.add(tempWord);
			}
		}

		return tokens;
	}

	/**
	 * Stem string.
	 * 
	 * @param tokens
	 * @return
	 */
	public static ArrayList<String> stemming(ArrayList<String> tokens) {
		SnowballStemmer stemmer = null;

		try {
			Class<?> stemClass = Class.forName("snowball.englishStemmer");
			stemmer = (SnowballStemmer) stemClass.newInstance();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" Error when initializing Stemmer! ");
			System.exit(1);
		}

		try {
			for (int i = 0; i < tokens.size(); i++) {
				stemmer.setCurrent(tokens.get(i));
				stemmer.stem();
				tokens.set(i, stemmer.getCurrent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tokens;
	}

	private static boolean isPunctuation(String word) {
		String punctuations[] = { "~", "`", "!", "@", "#", "$", "%", "^", "&",
				"*", "(", ")", "_", "+", "-", "=", "{", "}", "|", "[", "]",
				"\\", ":", "\"", ";", "'", "<", ">", "?", ",", ".", "/", "``",
				"''", "--", "\\/", "?!", "__", "...", "™", "\\*\\*\\*\\*",
				"\\*\\*\\*", "\\*", "¯" };

		Set<String> punctuationsSet = new HashSet<String>(
				Arrays.asList(punctuations));

		return punctuationsSet.contains(word);
	}
}
