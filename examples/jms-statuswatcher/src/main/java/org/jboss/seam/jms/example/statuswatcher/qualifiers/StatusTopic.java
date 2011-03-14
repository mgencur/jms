package org.jboss.seam.jms.example.statuswatcher.qualifiers;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import org.jboss.seam.jms.annotations.JmsDestination;

@Qualifier
@Retention(RUNTIME)
@JmsDestination(jndiName = "/jms/statusInfoTopic")
public @interface StatusTopic
{
}