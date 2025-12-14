// 23050111029 Medine Merve MARAL

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * PreAnalysis interface for students to implement their algorithm selection
 * logic
 * 
 * Students should analyze the characteristics of the text and pattern to
 * determine which algorithm would be most efficient for the given input.
 * 
 * The system will automatically use this analysis if the chooseAlgorithm method
 * returns a non-null value.
 */
public abstract class PreAnalysis {

	/**
	 * Analyze the text and pattern to choose the best algorithm
	 * 
	 * @param text    The text to search in
	 * @param pattern The pattern to search for
	 * @return The name of the algorithm to use (e.g., "Naive", "KMP", "RabinKarp",
	 *         "BoyerMoore", "GoCrazy") Return null if you want to skip pre-analysis
	 *         and run all algorithms
	 * 
	 *         Tips for students: - Consider the length of the text and pattern -
	 *         Consider the characteristics of the pattern (repeating characters,
	 *         etc.) - Consider the alphabet size - Think about which algorithm
	 *         performs best in different scenarios
	 */
	public abstract String chooseAlgorithm(String text, String pattern);

	/**
	 * Get a description of your analysis strategy This will be displayed in the
	 * output
	 */
	public abstract String getStrategyDescription();
}

/**
 * Default implementation that students should modify This is where students
 * write their pre-analysis logic
 */
class StudentPreAnalysis extends PreAnalysis {

	@Override
	public String chooseAlgorithm(String text, String pattern) {
		// TODO: Students should implement their analysis logic here
		//
		// Example considerations:
		// - If pattern is very short, Naive might be fastest
		// - If pattern has repeating prefixes, KMP is good
		// - If pattern is long and text is very long, RabinKarp might be good
		// - If alphabet is small, Boyer-Moore can be very efficient
		//
		// For now, this returns null which means "run all algorithms"
		// Students should replace this with their logic
		if (pattern.length() <= 5 || text.length() <= 50) { // for short pattern or text
			return "Naive";
		} else if (isRepetitive(text)) { // if characters are repetitive
			if (isAlphabetBig(text)) {
				return "RabinKarp";
			} else {
				return "KMP";
			}
		} else { // None of them (probably long text, middle-length pattern, not repetitive
					// characters)
			return "BoyerMoore";
		}

		// return null; // Return null to run all algorithms, or return algorithm name
		// to use pre-analysis
	}

	// selects a random character from text and checks if this character is highly
	// repeated.
	private boolean isRepetitive(String text) {
		Random random = new Random();
		char ch = text.charAt(random.nextInt(text.length()));
		int count = 0;
		int i = 0;
		while (i < text.length()) {
			if (text.charAt(i) == ch) {
				count++;
			}
			i++;
		}

		// %20+ of the text is the same character:
		if (count >= text.length() / 5) {
			return true;
		}

		return false;
	}

	// select random characters, add them into a map
	// if there are multiple same char, it means alphabet is probably small.
	private boolean isAlphabetBig(String text) {
		Map<Character, Integer> chars = new HashMap<>();
		int charNum = text.length() / 5;
		for (int i = 0; i < charNum; i++) {
			char ch = text.charAt(i);
			if (chars.containsKey(ch)) {
				chars.replace(ch, chars.get(ch) + 1);
			} else {
				chars.put(ch, 1);
			}
		}

		int sum = 0;
		for (int val : chars.values()) {
			System.out.println(val);
			sum += val;
		}

		double avgRepeat = sum / text.length() / 5;

		if (avgRepeat >= charNum / 5) {
			return false;
		}

		return true;
	}

	@Override
	public String getStrategyDescription() {
		return "Default strategy - no pre-analysis implemented yet (students should implement this)";
	}
}

/**
 * Example implementation showing how pre-analysis could work This is for
 * demonstration purposes
 */
class ExamplePreAnalysis extends PreAnalysis {

	@Override
	public String chooseAlgorithm(String text, String pattern) {
		int textLen = text.length();
		int patternLen = pattern.length();

		// Simple heuristic example
		if (patternLen <= 3) {
			return "Naive"; // For very short patterns, naive is often fastest
		} else if (hasRepeatingPrefix(pattern)) {
			return "KMP"; // KMP is good for patterns with repeating prefixes
		} else if (patternLen > 10 && textLen > 1000) {
			return "RabinKarp"; // RabinKarp can be good for long patterns in long texts
		} else {
			return "Naive"; // Default to naive for other cases
		}
	}

	private boolean hasRepeatingPrefix(String pattern) {
		if (pattern.length() < 2)
			return false;

		// Check if first character repeats
		char first = pattern.charAt(0);
		int count = 0;
		for (int i = 0; i < Math.min(pattern.length(), 5); i++) {
			if (pattern.charAt(i) == first)
				count++;
		}
		return count >= 3;
	}

	@Override
	public String getStrategyDescription() {
		return "Example strategy: Choose based on pattern length and characteristics";
	}
}

/**
 * Instructor's pre-analysis implementation (for testing purposes only) Students
 * should NOT modify this class
 */
class InstructorPreAnalysis extends PreAnalysis {

	@Override
	public String chooseAlgorithm(String text, String pattern) {
		// This is a placeholder for instructor testing
		// Students should focus on implementing StudentPreAnalysis
		return null;
	}

	@Override
	public String getStrategyDescription() {
		return "Instructor's testing implementation";
	}
}

