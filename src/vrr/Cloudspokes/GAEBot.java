package vrr.Cloudspokes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

/**
 * Google App Engine Servlet. This servlet responds to messages to
 * /_ah/xmpp/message/chat/. These messages are received as POSTs. This servlet
 * handles the GET & POST. GET to check its functionality via URL. POST to check
 * the chat messages.
 * 
 * @author Viral
 */
@SuppressWarnings("serial")
public class GAEBot extends HttpServlet {
	// logger object
	public static Logger _log = null;

	// queue object
	private Queue queue = null;

	// XMPP service
	public static XMPPService xmpp = null;

	/*
	 * initialize the required objects
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		// setup log
		_log = Logger.getLogger(Constants.LOGGERCLASS);
		// setup xmpp object
		xmpp = XMPPServiceFactory.getXMPPService();
		// setup queue
		queue = QueueFactory.getQueue(Constants.QUEUE_NAME);
	}

	/*
	 * Added this method to check via browser if the service is up and running
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			PrintWriter pw = resp.getWriter();
			pw.println(Constants.GetMessage);

			// for testing, lets just post it from here.
//			String body = Constants.XmppMessage;
			String body = "helP";
			// add it to the queue
			// 1. Create a task options object
			TaskOptions o = withDefaults();
			// 1.1. set parameters
			// 1.2. set forwarding URL
			// 1.3. set method
			// 1.4. set service interval
			o = o.param(Constants.PARAM_CONTENTS, InputParser.getMessage(body))
					.param(Constants.PARAM_USERID, Constants.ID)
					.url(Constants.URL_EXEC_SERVLET).method(Method.POST)
					.countdownMillis(InputParser.getTimeToLaunch(body));

			_log.info("task options created, adding into queue...");
			// 2. add it into queue
			queue.add(o);
			_log.fine("done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method handles the queuing logic. 1. receive the request 2. parse it
	 * to check the time requirements. 3. create a Task option object 3.1. set
	 * the parameters [contents, user id] 3.2. set the URL to be used when
	 * processing the object. 3.3. set the time to process, using received
	 * message. 4. add it to the queue
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			// get the incoming message from the Request object i.e. req
			_log.fine("Trying to parse message");
			Message msg = xmpp.parseMessage(req);
			if (msg == null)
				throw new IOException("Could not parse req object into Message");

			_log.fine("Message Parsed successfully");
			// interpret it
			JID from = msg.getFromJid();
			String body = msg.getBody();
			_log.info("Received a message from " + from.getId()
					+ " with contents = " + body);

			TaskOptions o = withDefaults();

			// add it to the queue
			// 1. Create a task options object
			// 1.1. set parameters
			// 1.2. set forwarding URL
			// 1.3. set method
			// 1.4. set service interval
			o = o.param(Constants.PARAM_CONTENTS, InputParser.getMessage(body))
					.param(Constants.PARAM_USERID, from.getId())
					.url(Constants.URL_EXEC_SERVLET).method(Method.POST)
					.countdownMillis(InputParser.getTimeToLaunch(body));

			_log.info("task options created, adding into queue...");
			// 2. add it into queue
			queue.add(o);
			_log.fine("done");

		} catch (Exception e) {
			_log.severe(e.getMessage());
		}
	}
}
