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
package org.jboss.seam.jms.example.statuswatcher.messagedriven;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import org.jboss.seam.jms.example.statuswatcher.model.Status;
import org.jboss.seam.jms.example.statuswatcher.session.StatusManager;

@MessageDriven(name = "OrderProcessor", activationConfig = { 
      @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
      @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/updateStatusQueue") })
public class ServerMDB implements MessageListener
{

   @Inject
   private StatusManager manager;

   @Resource(mappedName = "/ConnectionFactory")
   private ConnectionFactory connectionFactory;

   @Resource(mappedName = "/jms/statusInfoTopic")
   private Topic statusTopic;

   @Override
   public void onMessage(Message message)
   {
      Connection connection = null;
      try
      {
         ObjectMessage om = (ObjectMessage) message;
         Status status = (Status) om.getObject();
         status = manager.addStatusMessage(status);
         connection = connectionFactory.createConnection();
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         TopicPublisher publisher = ((TopicSession) session).createPublisher(statusTopic);
         ObjectMessage update = session.createObjectMessage(status);
         publisher.send(update);
      }
      catch (JMSException e)
      {
         e.printStackTrace();
      }
      finally
      {
         if (connection != null)
         {
            try
            {
               connection.close();
            }
            catch (JMSException e)
            {
               e.printStackTrace();
            }
         }
      }
   }

}
