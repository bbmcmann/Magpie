public class Magpie4 {
	
	public String getGreeting() {
		return "Hello, let's talk.";
	}

	
	public String getResponse(String statement) {
		String response = "";
		if (statement.length() == 0) {
			response = "Say something, please.";
		}

		else if (findKeyword(statement, "no") >= 0) {
			response = "Why so negative?";
		} else if (findKeyword(statement, "mother") >= 0
				|| findKeyword(statement, "father") >= 0
				|| findKeyword(statement, "sister") >= 0
				|| findKeyword(statement, "brother") >= 0) {
			response = "Tell me more about your family.";
		}

		// Responses which require transformations
		else if (findKeyword(statement, "I want to", 0) >= 0) {
			response = transformIWantToStatement(statement);
		}

		else {
			// Look for a two word (you <something> me)
			// pattern
			int psn = findKeyword(statement, "you", 0);

			if (psn >= 0 && findKeyword(statement, "me", psn) >= 0) {
				response = transformYouMeStatement(statement);
			} else {
				response = getRandomResponse();
			}
		}
		return response;
	}

	/**
	 * Take a statement with "I want to <something>." and transform it into
	 * "What would it mean to <something>?"
	 * 
	 * @param statement
	 *            the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String transformIWantToStatement(String statement) {
		// Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement.length() - 1);
		if (lastChar.equals(".")) {
			statement = statement.substring(0, statement.length() - 1);
		}
		int psn = findKeyword(statement, "I want to", 0);
		String restOfStatement = statement.substring(psn + 9).trim();
		return "What would it mean to " + restOfStatement + "?";
	}

	/**
	 * Take a statement with "you <something> me" and transform it into
	 * "What makes you think that I <something> you?"
	 * 
	 * @param statement
	 *            the user statement, assumed to contain "you" followed by "me"
	 * @return the transformed statement
	 */
	private String transformYouMeStatement(String statement) {
		// Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement.length() - 1);
		if (lastChar.equals(".")) {
			statement = statement.substring(0, statement.length() - 1);
		}

		int psnOfYou = findKeyword(statement, "you", 0);
		int psnOfMe = findKeyword(statement, "me", psnOfYou + 3);

		String restOfStatement = statement.substring(psnOfYou + 3, psnOfMe)
				.trim();
		return "What makes you think that I " + restOfStatement + " you?";
	}

	
	private int findKeyword(String statement, String goal, int startPos) {
		String phrase = statement.trim().toLowerCase();
		goal = goal.toLowerCase();

		// The only change to incorporate the startPos is in
		// the line below
		int psn = phrase.indexOf(goal, startPos);

		// Refinement--make sure the goal isn't part of a
		// word
		while (psn >= 0) {
			// Find the string of length 1 before and after
			// the word
			String before = " ", after = " ";
			if (psn > 0) {
				before = phrase.substring(psn - 1, psn);
			}
			if (psn + goal.length() < phrase.length()) {
				after = phrase.substring(psn + goal.length(),
						psn + goal.length() + 1);
			}

			// If before and after aren't letters, we've
			// found the word
			if (((before.compareTo("a") < 0) || (before.compareTo("z") > 0)) && ((after.compareTo("a") < 0) || (after.compareTo("z") > 0))) {
				return psn;
			}

			// The last position didn't work, so let's find
			// the next, if there is one.
			psn = phrase.indexOf(goal, psn + 1);

		}

		return -1;
	}

	
	private int findKeyword(String statement, String goal) {
		return findKeyword(statement, goal, 0);
	}

	private String getRandomResponse() {
		final int NUMBER_OF_RESPONSES = 4;
		double r = Math.random();
		int whichResponse = (int) (r * NUMBER_OF_RESPONSES);
		String response = "";

		if (whichResponse == 0) {
			response = "Interesting, tell me more.";
		} else if (whichResponse == 1) {
			response = "Hmmm.";
		} else if (whichResponse == 2) {
			response = "Do you really think so?";
		} else if (whichResponse == 3) {
			response = "You don't say.";
		}

		return response;
	}

}
