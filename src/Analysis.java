// 23050111029 Medine Merve MARAL

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Naive extends Solution {
	static {
		SUBCLASSES.add(Naive.class);
		System.out.println("Naive registered");
	}

	public Naive() {
	}

	@Override
	public String Solve(String text, String pattern) {
		List<Integer> indices = new ArrayList<>();
		int n = text.length();
		int m = pattern.length();

		for (int i = 0; i <= n - m; i++) {
			int j;
			for (j = 0; j < m; j++) {
				if (text.charAt(i + j) != pattern.charAt(j)) {
					break;
				}
			}
			if (j == m) {
				indices.add(i);
			}
		}

		return indicesToString(indices);
	}
}

class KMP extends Solution {
	static {
		SUBCLASSES.add(KMP.class);
		System.out.println("KMP registered");
	}

	public KMP() {
	}

	@Override
	public String Solve(String text, String pattern) {
		List<Integer> indices = new ArrayList<>();
		int n = text.length();
		int m = pattern.length();

		// Handle empty pattern - matches at every position
		if (m == 0) {
			for (int i = 0; i <= n; i++) {
				indices.add(i);
			}
			return indicesToString(indices);
		}

		// Compute LPS (Longest Proper Prefix which is also Suffix) array
		int[] lps = computeLPS(pattern);

		int i = 0; // index for text
		int j = 0; // index for pattern

		while (i < n) {
			if (text.charAt(i) == pattern.charAt(j)) {
				i++;
				j++;
			}

			if (j == m) {
				indices.add(i - j);
				j = lps[j - 1];
			} else if (i < n && text.charAt(i) != pattern.charAt(j)) {
				if (j != 0) {
					j = lps[j - 1];
				} else {
					i++;
				}
			}
		}

		return indicesToString(indices);
	}

	private int[] computeLPS(String pattern) {
		int m = pattern.length();
		int[] lps = new int[m];
		int len = 0;
		int i = 1;

		lps[0] = 0;

		while (i < m) {
			if (pattern.charAt(i) == pattern.charAt(len)) {
				len++;
				lps[i] = len;
				i++;
			} else {
				if (len != 0) {
					len = lps[len - 1];
				} else {
					lps[i] = 0;
					i++;
				}
			}
		}

		return lps;
	}
}

class RabinKarp extends Solution {
	static {
		SUBCLASSES.add(RabinKarp.class);
		System.out.println("RabinKarp registered.");
	}

	public RabinKarp() {
	}

	private static final int PRIME = 101; // A prime number for hashing

	@Override
	public String Solve(String text, String pattern) {
		List<Integer> indices = new ArrayList<>();
		int n = text.length();
		int m = pattern.length();

		// Handle empty pattern - matches at every position
		if (m == 0) {
			for (int i = 0; i <= n; i++) {
				indices.add(i);
			}
			return indicesToString(indices);
		}

		if (m > n) {
			return "";
		}

		int d = 256; // Number of characters in the input alphabet
		long patternHash = 0;
		long textHash = 0;
		long h = 1;

		// Calculate h = d^(m-1) % PRIME
		for (int i = 0; i < m - 1; i++) {
			h = (h * d) % PRIME;
		}

		// Calculate hash value for pattern and first window of text
		for (int i = 0; i < m; i++) {
			patternHash = (d * patternHash + pattern.charAt(i)) % PRIME;
			textHash = (d * textHash + text.charAt(i)) % PRIME;
		}

		// Slide the pattern over text one by one
		for (int i = 0; i <= n - m; i++) {
			// Check if hash values match
			if (patternHash == textHash) {
				// Check characters one by one
				boolean match = true;
				for (int j = 0; j < m; j++) {
					if (text.charAt(i + j) != pattern.charAt(j)) {
						match = false;
						break;
					}
				}
				if (match) {
					indices.add(i);
				}
			}

			// Calculate hash value for next window
			if (i < n - m) {
				textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;

				// Convert negative hash to positive
				if (textHash < 0) {
					textHash = textHash + PRIME;
				}
			}
		}

		return indicesToString(indices);
	}
}

/**
 * TODO: Implement Boyer-Moore algorithm This is a homework assignment for
 * students
 */
class BoyerMoore extends Solution {
	// private int NO_OF_CHARS = 256;

	static {
		SUBCLASSES.add(BoyerMoore.class);
		System.out.println("BoyerMoore registered");
	}

	public BoyerMoore() {
	}

	// create the bad character heuristic table, last index of the pattern's
	// characters
	private Map<Character, Integer> createBadCharTable(String pattern) {
		Map<Character, Integer> badCharTable = new HashMap<>();

		for (int j = 0; j < pattern.length(); j++) {
			badCharTable.put(pattern.charAt(j), j);
		}
		return badCharTable;
	}

	@Override
	public String Solve(String text, String pattern) {
		List<Integer> indices = new ArrayList<>();
		int n = text.length();
		int m = pattern.length();

		Map<Character, Integer> badCharTable = createBadCharTable(pattern);

		int shift = 0;

		while (shift <= (n - m)) {
			int j = m - 1;

			// if text and pattern matches, move to the next character.
			while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
				j--;
			}

			// if j < 0, it means pattern was found.
			if (j < 0) {
				indices.add(shift);

				// if there are characters left, shift using the table.
				if (shift + m < n) {
					char nextCharInText = text.charAt(shift + m);
					// if next character is not in the pattern, shift all
					// but if it is in the pattern, align character to the next.
					int badCharIndex = badCharTable.getOrDefault(nextCharInText, -1);
					shift += m - badCharIndex;
				} else {
					shift += 1;
				}
			} else {
				char badChar = text.charAt(shift + j);
				int badCharIndex = badCharTable.getOrDefault(badChar, -1);
				// if character is mismatched, shift from here.
				shift += Math.max(1, j - badCharIndex);
			}
		}

		return indicesToString(indices);
	}
}

/**
 * TODO: Implement your own creative string matching algorithm This is a
 * homework assignment for students Be creative! Try to make it efficient for
 * specific cases
 */
class GoCrazy extends Solution {
	static {
		SUBCLASSES.add(GoCrazy.class);
		System.out.println("GoCrazy registered");
	}

	public GoCrazy() {
	}

	@Override
	public String Solve(String text, String pattern) {
		// TODO: Students should implement their own creative algorithm here
		throw new UnsupportedOperationException("GoCrazy algorithm not yet implemented - this is your homework!");
	}
}
