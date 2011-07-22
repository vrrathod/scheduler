Google Chat Bot Reminder

v0.4.0: Latest update. (3/20/2011:23:00)

1. Renamed dispatcher servlet to GAEDispatch.
2. Added two verbs for chat. 
   'version' returns current version of bot.
   'help' returns help message.
   Both these verbs are case insensitive.
3. Added subscription handler. 
   At present it simply returns a help message.   
   

------------------------------------------------------------------------------
Description:
	This application provides a google based chat bot. The same code has been 
	deployed as vrr-schedular@appspot.com (FYI name has a typo). This application 
	accepts time in days / hours / minutes / seconds.

	Dude to size restriction, I am removing lib section from the war/WEB-INF.

Design:
	The application has been written using Java. It uses following services
	
	1. Application hosting on Google Appspot.

	2. XMPP services from Google Application Engine.
	   XMPP services provided by Google App Engine maps the /_ah/xmpp/message/chat/ [chat url] 
	   to Receiver servlet. Hence any message sent on xmpp will be redirected to 
	   the Receiver Servlet.
  
	3. Task Queue services from Google Application Engine.
	   Task Queue services provides a unique approach for using priority queues 
	   in App Engine. The queue entries could be processed on another URL endpoint 
	   and could be controlled on time domain.
     
     My initial approach was to use Priority Blocking Queues and threads to process 
     the entries in queue. Google App Engine does not allow any Java based threading 
     and forces to use Task Queues.

	   The application has been designed to have two components
	   1. Receiver: 
	      This component provides main logic of the application. It receives the message 
	      from originator. Then it parses the message to get [time] & [response message]
	      components. Using the Task Queue APIs for Java, then it enqueues all the message
	       on the [time] to dispatch. This implementation also uses the URL redirect 
	       [Dispatcher] & parameters [Originator, Response Message] from Task Queue APIs,
	       i.e. When time is ripe, it calls the Dispatcher with these two parameters.

	   2. Dispatcher: 
	      Functionality of dispatcher is relatively is easier compared to the Receiver. 
	      When the time is ripe, Task Queue would call dispatcher with Originator and 
	      Response message as parameters. Dispatcher will then generate a response 
	      message and send it.


Bottlenecks:
	When initial request is received a new process is created on google app engine 
	which uses about 1200 milliSec CPU. This seems to be acceptable however when 
	the request arrival / response dispatch has a time gap of more than 2 min, it 
	creates new processes. This consumes more CPU time.