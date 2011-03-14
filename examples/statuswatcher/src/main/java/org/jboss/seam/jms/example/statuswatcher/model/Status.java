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
package org.jboss.seam.jms.example.statuswatcher.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class Status implements Serializable
{

   private static final long serialVersionUID = 1L;

   private static final long MS_PER_SECOND = 1000;
   private static final long MS_PER_MINUTE = 60 * MS_PER_SECOND;
   private static final long MS_PER_HOUR = 60 * MS_PER_MINUTE;
   private static final long MS_PER_DAY = 24 * MS_PER_HOUR;

   private static final SimpleDateFormat df = new SimpleDateFormat("d MMM");

   @Id
   @GeneratedValue(strategy = AUTO)
   @Column(name = "id")
   private int id;

   private String user;

   private String statusMessage;

   @Temporal(TemporalType.TIMESTAMP)
   private Date datetime;

   public Status()
   {
      this.statusMessage = "Enter a new status message...";
   }

   public Status(String user, String message)
   {
      this.user = user;
      this.statusMessage = message;
   }

   public String getStatusMessage()
   {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage)
   {
      this.statusMessage = statusMessage;
   }

   public int getId()
   {
      return this.id;
   }

   public void setsId(int id)
   {
      this.id = id;
   }

   public String getUser()
   {
      return user;
   }

   public void setUser(String user)
   {
      this.user = user;
   }

   public Date getDatetime()
   {
      return datetime;
   }

   public void setDatetime(Date datetime)
   {
      this.datetime = datetime;
   }

   public String getFriendlyDate()
   {
      if (getDatetime() == null)
         return "unknown";

      Date now = new Date();

      long age = now.getTime() - getDatetime().getTime();

      long days = (long) Math.floor(age / MS_PER_DAY);
      age -= (days * MS_PER_DAY);
      long hours = (long) Math.floor(age / MS_PER_HOUR);
      age -= (hours * MS_PER_HOUR);
      long minutes = (long) Math.floor(age / MS_PER_MINUTE);

      if (days < 7)
      {
         StringBuilder sb = new StringBuilder();

         if (days > 0)
         {
            sb.append(days);
            sb.append(days > 1 ? " days " : " day ");
         }

         if (hours > 0)
         {
            sb.append(hours);
            sb.append(hours > 1 ? " hrs " : " hr ");
         }

         if (minutes > 0)
         {
            sb.append(minutes);
            sb.append(minutes > 1 ? " minutes " : " minute ");
         }

         if (hours == 0 && minutes == 0)
         {
            sb.append("just now");
         }
         else
         {
            sb.append("ago");
         }

         return sb.toString();
      }
      else
      {
         return df.format(getDatetime());
      }
   }

   public String toString()
   {
      return "User: " + this.user + ", Time: " + this.getFriendlyDate() + ", Status: " + this.statusMessage;
   }

}
