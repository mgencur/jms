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
package org.jboss.seam.jms.test.bridge;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jms.bridge.EventBridge;
import org.jboss.seam.jms.bridge.Route;
import org.jboss.seam.jms.bridge.RouteType;
import org.jboss.seam.jms.test.Util;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JmsEventBridgeTest
{

   @Deployment
   public static Archive<?> createDeployment()
   {
      return Util.createDeployment(JmsEventBridgeTest.class);
   }
   
   @Inject Instance<EventBridge> bridge;
   
   @Test
   public void injectBridge()
   {
      Assert.assertNotNull(bridge.get());
   }
   
   @Test
   public void createRoute()
   {
      EventBridge b = bridge.get();
      Route r = b.createRoute(RouteType.EGRESS, Object.class);
      Assert.assertNotNull(r);
      Assert.assertEquals(RouteType.EGRESS, r.getType());
      Assert.assertEquals(Object.class, r.getPayloadType());
   }
}
