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

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import org.jboss.seam.jms.example.statuswatcher.qualifiers.StatusConnection;
import org.jboss.seam.jms.example.statuswatcher.qualifiers.StatusSession;


public class StatusResources
{

   @Resource(mappedName = "/ConnectionFactory")
   private ConnectionFactory connectionFactory;

//   @Resource(mappedName = "/jms/updateStatusQueue")
//   private Queue statusQueue;
   
   @Produces
   @StatusConnection
   @RequestScoped
   public Connection createStatusConnection() throws JMSException
   {
      return connectionFactory.createConnection();
   }
   
   public void closeStatusConnection(@Disposes @StatusConnection Connection connection) throws JMSException
   {
      connection.close();
   }
   
//   @Produces
//   @StatusQueue
//   public Queue createStatusQueue() throws JMSException
//   {
//      return statusQueue;
//   }
   
   @Produces
   @StatusSession
   public Session createStatusSession(@StatusConnection Connection connection) throws JMSException
   {
      return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
   }

   public void closeStatusSession(@Disposes @StatusSession Session session) throws JMSException
   {
      session.close();
   }

}