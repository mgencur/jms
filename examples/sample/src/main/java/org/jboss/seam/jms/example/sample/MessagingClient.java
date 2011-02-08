package org.jboss.seam.jms.example.sample;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@RequestScoped
@Named
public class MessagingClient 
{
	@Inject @Bridged Event<Order> event;

	private Order myOrder;
	
	public String getMessage() throws Exception
	{
		myOrder = new Order("New bicycle");
		
		event.fire(myOrder);


/**
 *  =========== If you uncomment this, it will work ! ==============		
 *
		InitialContext ic = null;
		Connection connection = null;

		try 
		{
			ic = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory)ic.lookup("/ConnectionFactory");
			Queue orderQueue = (Queue)ic.lookup("/jms/Q");
			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(orderQueue);
			connection.start();
			ObjectMessage message = session.createObjectMessage(myOrder);
			producer.send(message);
		} 
		catch (JMSException e) 
		{
			e.printStackTrace();
		}
		finally
	    {
	         if (ic != null)
	         {
	            ic.close();
	         }
	         if (connection != null)
	         {
	            connection.close();
	         }
	    }
 */
		
		
		return "First output";
	}
}
