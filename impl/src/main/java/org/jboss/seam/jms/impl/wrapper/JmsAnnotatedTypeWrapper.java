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
package org.jboss.seam.jms.impl.wrapper;

import static org.jboss.seam.jms.impl.wrapper.JmsDestinationAnnotatedWrapper.needsDecorating;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.seam.jms.annotations.JmsDestination;

/**
 * Wraps {@link AnnotatedType}s with injection points that have transitive
 * annotations to {@link JmsDestination}. Only the
 * {@link javax.enterprise.inject.spi.Annotated} that define the transitive
 * annotations will be wrapped.
 * 
 * @author Jordan Ganoff
 */
public class JmsAnnotatedTypeWrapper<X> implements AnnotatedType<X>
{
   private AnnotatedType<X> decorated;
   private Set<AnnotatedField<? super X>> fields;
   private Set<AnnotatedMethod<? super X>> methods;
   private Set<AnnotatedConstructor<X>> constructors;

   public JmsAnnotatedTypeWrapper(AnnotatedType<X> decorated)
   {
      this.decorated = decorated;

      fields = new HashSet<AnnotatedField<? super X>>();
      for (AnnotatedField<? super X> f : decorated.getFields())
      {
         fields.add(decorate(f));
      }
      fields = Collections.unmodifiableSet(fields);

      methods = new HashSet<AnnotatedMethod<? super X>>();
      for (AnnotatedMethod<? super X> m : decorated.getMethods())
      {
         methods.add(decorate(m));
      }
      methods = Collections.unmodifiableSet(methods);

      constructors = new HashSet<AnnotatedConstructor<X>>();
      for (AnnotatedConstructor<X> c : decorated.getConstructors())
      {
         constructors.add(decorate(c));
      }
      constructors = Collections.unmodifiableSet(constructors);
   }

   /**
    * Decorates the type if any injection targets are transitively annotated
    * with {@link JmsDestination}.
    */
   public static <T> AnnotatedType<T> decorate(AnnotatedType<T> type)
   {
      for (AnnotatedField<? super T> f : type.getFields())
      {
         if (needsDecorating(f))
         {
            return new JmsAnnotatedTypeWrapper<T>(type);
         }
      }
      for (AnnotatedMethod<? super T> m : type.getMethods())
      {
         for (AnnotatedParameter<? super T> p : m.getParameters())
         {
            if (needsDecorating(p))
            {
               return new JmsAnnotatedTypeWrapper<T>(type);
            }
         }
      }
      for (AnnotatedConstructor<? super T> c : type.getConstructors())
      {
         for (AnnotatedParameter<? super T> p : c.getParameters())
         {
            if (needsDecorating(p))
            {
               return new JmsAnnotatedTypeWrapper<T>(type);
            }
         }
      }
      return type;
   }

   public <T> AnnotatedField<T> decorate(AnnotatedField<T> field)
   {
      return needsDecorating(field) ? new JmsDestinationFieldWrapper<T>(field) : field;
   }

   public <T> AnnotatedMethod<T> decorate(AnnotatedMethod<T> method)
   {
      for (AnnotatedParameter<T> p : method.getParameters())
      {
         if (needsDecorating(p))
         {
            return new JmsDestinationMethodWrapper<T>(method);
         }
      }
      return method;
   }

   public <T> AnnotatedConstructor<T> decorate(AnnotatedConstructor<T> constructor)
   {
      for (AnnotatedParameter<T> p : constructor.getParameters())
      {
         if (needsDecorating(p))
         {
            return new JmsDestinationConstructorWrapper<T>(constructor);
         }
      }
      return constructor;
   }

   public Set<AnnotatedField<? super X>> getFields()
   {
      return fields;
   }

   public Set<AnnotatedConstructor<X>> getConstructors()
   {
      return constructors;
   }

   public Set<AnnotatedMethod<? super X>> getMethods()
   {
      return methods;
   }

   public <T extends Annotation> T getAnnotation(Class<T> annotationType)
   {
      return decorated.getAnnotation(annotationType);
   }

   public Set<Annotation> getAnnotations()
   {
      return decorated.getAnnotations();
   }

   public Type getBaseType()
   {
      return decorated.getBaseType();
   }

   public Class<X> getJavaClass()
   {
      return decorated.getJavaClass();
   }

   public Set<Type> getTypeClosure()
   {
      return decorated.getTypeClosure();
   }

   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
   {
      return decorated.isAnnotationPresent(annotationType);
   }
}
