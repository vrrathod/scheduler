package vrr.Cloudspokes;

/**
 * @author Viral
 * 
 */
public class Constants {

	// verbs for chat
	public static final String verbHelp = "help";
	public static final String verbVersion = "version";

	// Version info
	public static final String VERSION = "0.4.0";

	// Help text
	public static final String MsgHelp = "Welcome to the *Google Chat Bot Reminder* service. "
			+ "\n\n This chat bot shall accept messages in <time> <message> format. "
			+ "\n\n It shall return the <message> contents after <time> time. "
			+ "\n\n i.e. '5m try calling back' shall program the chat bot to send "
			+ "'try calling back' message to you after 5 minutes. "
			+ "\n\n Acceptable parameters for time are d = days, h = hours, m = minutes, s = seconds";

	// Logging constants
	public static final String LOGGERCLASS = GAEBot.class.getName();

	// string constants
	// POST parameter to identify contents
	public static final String PARAM_CONTENTS = "body";
	// POST parameter to identify user id
	public static final String PARAM_USERID = "jid";
	// URL to be POSTed with above parameters
	public static final String URL_EXEC_SERVLET = "/dispatch";
	// Name of the queue
	// NOTE: change here should also be reflected in WAR/WEB-INF/queue.xml
	public static final String QUEUE_NAME = "TimedMessageQueue";

	// REGEX Patterns
	/*
	 * Note: Ideally the pattern should have started with ^ to make sure the
	 * <time> <message> format. Just to make things more interesting, I have
	 * prepared patterns without ^.
	 * 
	 * Following patterns parses "5s test" & "hello 5s test" to reply after 5s.
	 */
	// Regular expression to identify day
	public static final String RegexDay = "[0-9]+d ";
	// Regular expression to identify hours
	public static final String RegexHr = "[0-9]+h ";
	// Regular expression to identify minutes
	public static final String RegexMin = "[0-9]+m ";
	// Regular expression to identify minutes
	public static final String RegexSec = "[0-9]+s ";

	// Default parameters
	// the typo, in the following line, is intended :)
	public static final String GetMessage = "hey its working! try adding vrr-schedular@appspot.com to your gtalk";
	public static final String XmppMessage = "5s App instantiated";
	public static final String ID = "vrrathod01@gmail.com";

	// numeric constants
	// Note: used with default queue, currently unused
	// public static final int RETRY_MAX_COUNT = 3; // 3 retries maximum
	// public static final int RETRY_MIN_BACKOFF = 1; // 1 SECOND
	// public static final int RETRY_MAX_BACKOFF = 60 ; // 1 MINUTE

}
