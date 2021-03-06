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
package org.jboss.seam.jms;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMember;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessProducer;

import org.jboss.logging.Logger;
import org.jboss.seam.jms.bridge.EgressRoutingObserver;
import org.jboss.seam.jms.bridge.Route;
import org.jboss.seam.jms.impl.wrapper.JmsAnnotatedTypeWrapper;

/**
 * Seam 3 JMS Portable Extension
 * 
 * @author Jordan Ganoff
 */
public class Seam3JmsExtension implements Extension
{
   private static final Logger log = Logger.getLogger(Seam3JmsExtension.class);
   
   private Set<AnnotatedMethod<?>> eventRoutingRegistry = new HashSet<AnnotatedMethod<?>>();
   
   public void buildRoutes(@Observes final AfterBeanDiscovery abd, final BeanManager bm)
   {
      for (AnnotatedMethod<?> m : eventRoutingRegistry)
      {
         Type beanType = m.getDeclaringType().getBaseType();
         Set<Bean<?>> configBeans = bm.getBeans(beanType);
         for (Bean<?> configBean : configBeans)
         {
            CreationalContext<?> context = bm.createCreationalContext(configBean);
            Object config = null;
            try
            {
               Object bean = bm.getReference(configBean, beanType, context);
               config = m.getJavaMember().invoke(bean);
            } catch (Exception ex)
            {
               abd.addDefinitionError(new IllegalArgumentException("Routing could not be configured from bean " + beanType + ": " + ex.getMessage(), ex));
            }
            log.debug("Building " + Route.class.getSimpleName() + "s from " + beanType);
            if (config != null)
            {
               if (Collection.class.isAssignableFrom(config.getClass()))
               {
                  @SuppressWarnings("unchecked")
                  Collection<Route> routes = Collection.class.cast(config);
                  for (Route route : routes)
                  {
                     if(route == null)
                     {
                        log.warn("No routes found for " + m);
                     }
                     createRoute(abd, bm, route);
                  }
               } else if(Route.class.isAssignableFrom(config.getClass()))
               {
                  createRoute(abd, bm, Route.class.cast(config));
               } else
               {
                  abd.addDefinitionError(new IllegalArgumentException("Unsupported route configuration type: " + config));
               }
            }
         }
      }
   }
   
   private void createRoute(final AfterBeanDiscovery abd, final BeanManager bm, final Route route)
   {
      switch(route.getType())
      {
         case EGRESS:
            abd.addObserverMethod(new EgressRoutingObserver(bm, route));
            log.debug("Built " + route);
            break;
         default:
            abd.addDefinitionError(new IllegalArgumentException("Unsupported routing type: " + route.getType()));
      }
   }
   
   public <X> void decorateAnnotatedType(@Observes ProcessAnnotatedType<X> pat)
   {
      /**
       * Flatten all @Annotated that define @JmsDestinations so that they may be injected  
       */
      pat.setAnnotatedType(JmsAnnotatedTypeWrapper.decorate(pat.getAnnotatedType()));
   }

   /**
    * Register method producers of {@link org.jboss.seam.jms.bridge.Route}s.
    */
   public void registerRouteCollectionProducer(@Observes ProcessProducer<?, ? extends Collection<Route>> pp) {
      registerRouteProducer(pp.getAnnotatedMember());
   }
   
   /**
    * Register method producers of a single {@link org.jboss.seam.jms.bridge.Route}.
    */
   public void registerRouteProducer(@Observes ProcessProducer<?, ? extends Route> pp) {
      registerRouteProducer(pp.getAnnotatedMember());
   }
   
   private boolean registerRouteProducer(AnnotatedMember<?> m) {
      if (AnnotatedMethod.class.isAssignableFrom(m.getClass())) {
         eventRoutingRegistry.add((AnnotatedMethod<?>) m);
         return true;
      } else {
         log.warnf("Producer of routes not registered. Must declare a method producer. (%s)", m);
         return false;
      }
   }
}
