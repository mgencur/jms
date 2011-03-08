/*
* JBoss, Home of Professional Open Source
* Copyright 2010, Red Hat, Inc., and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.seam.jms.example.statuswatcher.session;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import org.jboss.seam.jms.example.statuswatcher.model.Status;
import org.jboss.seam.jms.example.statuswatcher.qualifiers.BridgedViaRoute;
import org.jboss.seam.jms.example.statuswatcher.qualifiers.StatusQueue;
import org.jboss.seam.jms.example.statuswatcher.qualifiers.StatusSession;


@RequestScoped
@Named
public class SendingClient
{
	@Inject 
	@BridgedViaRoute 
	Event<Status> statusEvent;
	
//	@Inject @MyTopic 
//	private TopicSubscriber ts;
	
//	@Inject @MyTopic 
//	private Topic t;
	
//	@Inject @StatusQueue
//	private Queue q;
//	
//	@Inject @JmsSessionSelector(transacted=true, acknowledgementMode=Session.AUTO_ACKNOWLEDGE)
//	private Session s;
	
	private Status status;
	
	@Inject 
	@StatusSession
	private Session session;
	
	@Inject 
	@StatusQueue
	private Queue statusQueue;
	
   @PostConstruct
	public void initialize()
	{
	   this.status = new Status();
	}
	
	public String sendStatusUpdate() throws Exception
	{
		statusEvent.fire(status);
		
//		MessageProducer mp = s.createProducer(q);
//		ObjectMessage message = s.createObjectMessage(statusUpdate);
//		mp.send(message);
				
//		InitialContext ic = null;
//		Connection connection = null;

//		try 
//		{
//			ic = new InitialContext();
//			ConnectionFactory cf = (ConnectionFactory)ic.lookup("/ConnectionFactory");
//			connection = cf.createConnection();
//			connection.start();
//			Queue statusQueue = (Queue)ic.lookup("/jms/updateStatusQueue");
//			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//			MessageProducer producer = session.createProducer(statusQueue);
			
			ObjectMessage message = session.createObjectMessage(status);
			MessageProducer producer = session.createProducer(statusQueue);
			producer.send(message);
			
//		} 
//		catch (JMSException e) 
//		{
//			e.printStackTrace();
//		}
//		finally
//	    {
//	         if (ic != null)
//	         {
//	            ic.close();
//	         }
//	         if (connection != null)
//	         {
//	            connection.close();
//	         }
//	    }
		
		this.status = null;
		
		return null;
	}
	
	public Status getStatus()
   {
      return status;
   }

   public void setStatus(Status status)
   {
      this.status = status;
   }
	
}
