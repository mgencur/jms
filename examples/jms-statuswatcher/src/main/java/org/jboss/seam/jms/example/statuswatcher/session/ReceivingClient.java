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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import org.jboss.seam.jms.example.statuswatcher.model.Status;
import org.jboss.seam.jms.example.statuswatcher.qualifiers.StatusTopic;


@SessionScoped
@Named
public class ReceivingClient implements Serializable
{

   private static final long serialVersionUID = 1L;
   private static final int TIMEOUT = 20;
   private static int uniqueId = 1;
   
   @Inject 
   private StatusManager manager;
   private LinkedList<Status> receivedStatuses;
   
   private boolean followAll = false;
   private String clientSubscription;
   private String clientId;
   
   @Resource(mappedName = "/ConnectionFactory")
   private ConnectionFactory connectionFactory;

   private transient Connection connection;
   private transient Session session;
   private transient TopicSubscriber subscriber;
   
   @Inject 
   @StatusTopic
   private Topic statusTopic;
   
   @PostConstruct
   public void initialize()
   {
      this.receivedStatuses = new LinkedList<Status>();
      ++uniqueId;
      clientSubscription = "subscription" + uniqueId;
      clientId = "client" + uniqueId; 
   }

   @PreDestroy
   public void cleanup() throws Exception
   {
      if (connection != null)
      {
         connection.close();
         connection = null;
      }
   }
   
   public void receive() throws Exception
   {
      ObjectMessage msg;
      Message response;
      if (followAll)
      {
         while ((response = subscriber.receive(TIMEOUT)) != null)
         {
            msg = (ObjectMessage) response;
            receivedStatuses.offerFirst((Status) msg.getObject());
         }
      }
   }

   public void changeFollowing(ValueChangeEvent e) throws Exception
   {
      followAll = (Boolean) e.getNewValue();
      if (followAll)
      {
         connection = connectionFactory.createConnection();
         connection.setClientID(clientId);
         session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         subscriber = session.createDurableSubscriber(statusTopic, clientSubscription);
         connection.start();
         receive();
      }
      else
      {
         subscriber.close();
         session.unsubscribe(clientSubscription);
         if (connection != null)
         {
            connection.close();
         }
      }
   }

   public void history()
   {
      receivedStatuses = new LinkedList<Status>(manager.getAllStatuses());
   }
   
   public List<Status> getReceivedStatuses() throws Exception
   {
      if (followAll)
      {
         receive();
      }
      return receivedStatuses;
   }

   public void setReceivedStatuses(LinkedList<Status> receivedStatuses)
   {
      this.receivedStatuses = receivedStatuses;
   }

   public boolean isFollowAll()
   {
      return this.followAll;
   }

   public void setFollowAll(boolean followAll)
   {
      this.followAll = followAll;
   }
   
}
