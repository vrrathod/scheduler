/**
 * 
 */
package vrr.Cloudspokes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Viral
 * 
 */
public class InputParser {

	// Time granularity,
	// Currently we are supporting DAY, Hour, Minutes, seconds
	public static enum TIME_GRANULARITY {
		DAY, HOUR, MINUTE, SEC;
	};

	// Compiled pattern for identification of Days
	public static Pattern oRegexDay = Pattern.compile(Constants.RegexDay);
	// Compiled pattern for identification of Hours
	public static Pattern oRegexHr = Pattern.compile(Constants.RegexHr);
	// Compiled pattern for identification of Minutes
	public static Pattern oRegexMin = Pattern.compile(Constants.RegexMin);
	// Compiled pattern for identification of Minutes
	public static Pattern oRegexSec = Pattern.compile(Constants.RegexSec);

	/**
	 * get() : Given input string and granularity, this function returns the
	 * time integer.
	 * 
	 * @param szInput
	 *            Input string, the message
	 * @param t
	 *            Time granularity [Day, Hour, Minute]
	 * @return integer : time amount
	 */
	protected static int get(String szInput, TIME_GRANULARITY t) {
		int nRet = 0;
		// use pattern based on selected granularity
		Pattern p = null;
		// switch for the pattern
		switch (t) {
		default:
			// by default try to find day
			// TODO: should we throw exception?
		case DAY:
			p = oRegexDay;
			break;
		case HOUR:
			p = oRegexHr;
			break;
		case MINUTE:
			p = oRegexMin;
			break;
		case SEC:
			p = oRegexSec;
			break;
		}
		// Use the pattern and find matches
		Matcher m = p.matcher(szInput);
		// we want only first occurrence, if found
		if (m.find()) {
			String sRet = "";
			// get the match
			sRet = m.group();
			// remove non numbers from the match
			sRet = sRet.replaceAll("\\D", "");
			// get the integer part
			nRet = Integer.parseInt(sRet);
		} else {
			// return 0 if no match found.
			nRet = 0;
		}

		return nRet;
	}

	// method to get Day component
	public static int getDay(String szInput) {
		return get(szInput, TIME_GRANULARITY.DAY);
	}

	// method to get Hour component
	public static int getHour(String szInput) {
		return get(szInput, TIME_GRANULARITY.HOUR);
	}

	// method to get Minute component
	public static int getMins(String szInput) {
		return get(szInput, TIME_GRANULARITY.MINUTE);
	}

	// method to get Seconds component
	public static int getSec(String szInput) {
		return get(szInput, TIME_GRANULARITY.SEC);
	}

	/**
	 * @param szInput
	 *            - input message. it will be parsed and time will be fetched.
	 * @return number of milliseconds before launching the tasks
	 */
	public static long getTimeToLaunch(String szInput) {
		// compute seconds to launch
		int nMs = InputParser.getDay(szInput) * 24 * 3600;
		nMs += InputParser.getHour(szInput) * 3600;
		nMs += InputParser.getMins(szInput) * 60;
		nMs += InputParser.getSec(szInput);
		// convert into miliSeconds
		return nMs * 1000;
	}

	/**
	 * Trim() : trim the message to remove the time component
	 * 
	 * @param szInput
	 *            : input message
	 * @return : trimmed string
	 */
	public static String getMessage(String szInput) {
		return szInput.replaceFirst(Constants.RegexDay, "")
				.replaceFirst(Constants.RegexHr, "")
				.replaceFirst(Constants.RegexMin, "")
				.replaceFirst(Constants.RegexSec, "");
	}
	
	
	/**
	 * checks to see if the string is version.
	 * @param Message
	 * @return true if Message is "version"
	 */
	public static boolean IsVersion(String Message) {
		if (Message == Constants.verbVersion ){
			return true;
		} else
			return false;
	}


}
