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
import com.google.appengine.api.xmpp.Subscription;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class GAESubscribe extends HttpServlet {
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
			_log.fine("inside Subscription servlet.");
			Subscription sub = xmpp.parseSubscription(req);
			
			JID from = sub.getFromJid(); 

			// and compose a response
			_log.fine("Constructing reply message");
			Message replyMsg = new MessageBuilder().withRecipientJids(from)
					.withBody(Constants.MsgHelp).build();

			// send the response message back
			SendResponse stat = xmpp.sendMessage(replyMsg);

			if (stat.getStatusMap().get(from) == SendResponse.Status.SUCCESS) {
				String szMsg = "Message is sent to " + from.getId();
				_log.info(szMsg);
			} else {
				_log.info("response failed :" + from.getId());
			}

		} catch (Exception e) {
			_log.severe(e.getMessage());
		}
	}
}
