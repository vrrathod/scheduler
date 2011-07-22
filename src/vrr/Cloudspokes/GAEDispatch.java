package vrr.Cloudspokes;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

/**
 * MsgDispatcher class, helps dispatching the responses. this servlet is called
 * from the GAETestServlet at ETA. The call is a POST method. Call also contains
 * the parameters that specifies the receiving user & JID
 * 
 * @author Viral
 * 
 */
@SuppressWarnings("serial")
public class GAEDispatch extends HttpServlet {
	// logger object
	public static Logger _log = null;

	// XMPP service
	public static XMPPService xmpp = null;

	/*
	 * (non-Javadoc) initialize the required objects
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
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		try {
			// get the parameters from req object
			_log.fine("inside dispatcher servlet.");
			String body = req.getParameter("body");
			_log.fine("body =" + body);
			String From = req.getParameter("jid");
			_log.fine("jid =" + From);
			JID from = new JID(From);
			_log.fine("To: " + From + ": Message : " + body);

			// check if to send version information / help information.
			if (body.compareToIgnoreCase(Constants.verbVersion) == 0) {
				body = Constants.VERSION;
				_log.fine("Sending version info.");
			} else if (body.compareToIgnoreCase(Constants.verbHelp) == 0) {
				body = Constants.MsgHelp;
				_log.fine("Sending help message.");
			}

			// and compose a response
			_log.fine("Constructing reply message");
			Message replyMsg = new MessageBuilder().withRecipientJids(from)
					.withBody(body).build();

			// check status
			if (xmpp.getPresence(from).isAvailable() == false) {
				_log.fine("User is offline, the message shall go in offline chats.");
			} else {
				_log.fine("Sending Reply message...");
			}

			// send the response message back
			SendResponse stat = xmpp.sendMessage(replyMsg);

			if (stat.getStatusMap().get(from) == SendResponse.Status.SUCCESS) {
				String szMsg = "Message is sent to " + From;
				_log.info(szMsg);
			} else {
				_log.info("response failed :" + From);
			}

		} catch (Exception e) {
			_log.severe(e.getMessage());
		}
	}
}
