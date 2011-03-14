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

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.seam.jms.example.statuswatcher.model.Status;

@Stateless
public class StatusManagerImpl implements StatusManager
{
   @PersistenceContext
   private EntityManager em;

   private int MAX_RESULTS = 50;

   public Status addStatusMessage(Status status)
   {
      if (status.getDatetime() == null)
      {
         status.setDatetime(new Date());
      }
      em.merge(status);
      return status;
   }

   public List<Status> getAllStatuses()
   {
      Query q = em.createQuery("SELECT s from Status s ORDER BY datetime DESC");
      q.setMaxResults(MAX_RESULTS);
      @SuppressWarnings("unchecked")
      List<Status> stats = q.getResultList();
      return stats;
   }
}
