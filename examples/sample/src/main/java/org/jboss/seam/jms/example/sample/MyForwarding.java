package org.jboss.seam.jms.example.sample;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Queue;
import javax.jms.Destination;
import javax.enterprise.util.AnnotationLiteral;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

import org.jboss.seam.jms.JmsForwarding;

@Named
@ApplicationScoped
public class MyForwarding implements JmsForwarding
{
   @Inject @MyQueue private Queue q;

   public Set<? extends Destination> getDestinations()
   {
      return Collections.singleton(q);
   }

   public Type getEventType()
   {
      return Order.class;
   }

   public Set<Annotation> getQualifiers()
   {
      return Collections.<Annotation> singleton(new AnnotationLiteral<Bridged>(){});
   }
}