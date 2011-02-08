package org.jboss.seam.jms.example.sample;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;

@MessageDriven(name="OrderProcessor", 
			   activationConfig = {
			   @ActivationConfigProperty(propertyName="destinationType",
					   					 propertyValue="javax.jms.Queue"),
			   @ActivationConfigProperty(propertyName="destination",
					   					 propertyValue="/jms/Q")})
public class OrderProcessor implements MessageListener 
{
	@Override
	public void onMessage(Message message) 
	{
		try
		{
			ObjectMessage om = (ObjectMessage) message;
			Order order = (Order) om.getObject();
			System.out.println("Product name: " + order.getProduct());
		}
		catch(JMSException e)
		{
			System.out.println("Didn't work out");
		}
	}
}
