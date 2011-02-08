package org.jboss.seam.jms.example.sample;

import static org.jboss.seam.jms.bridge.RouteType.EGRESS;

import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.jms.Queue;

import org.jboss.seam.jms.annotations.JmsDestination;
import org.jboss.seam.jms.bridge.EventBridge;
import org.jboss.seam.jms.bridge.Route;

public class RouteConfig
{
   @Inject EventBridge bridge;

   @Inject @MyQueue Queue q;
   
//   private static final AnnotationLiteral<BridgedViaCollection> BRIDGED_VIA_COLLECTION = new AnnotationLiteral<BridgedViaCollection>()
//   {
//      private static final long serialVersionUID = 1L;
//   };

   private static final AnnotationLiteral<BridgedViaRoute> BRIDGED_VIA_ROUTE = new AnnotationLiteral<BridgedViaRoute>()
   {
      private static final long serialVersionUID = 1L;
   };

//   @Produces
//   public Collection<Route> getRoutes()
//   {
//      return Arrays.asList(bridge.createRoute(EGRESS, String.class).addQualifiers(BRIDGED_VIA_COLLECTION).connectTo(Queue.class, q));
//   }
   
   @Produces
   public Route getRoute()
   {
      return bridge.createRoute(EGRESS, Order.class).addQualifiers(BRIDGED_VIA_ROUTE).connectTo(Queue.class, q);
   }
}
