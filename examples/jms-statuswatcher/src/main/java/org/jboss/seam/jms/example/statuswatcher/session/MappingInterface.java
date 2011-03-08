package org.jboss.seam.jms.example.statuswatcher.session;

import javax.enterprise.event.Observes;
import javax.jms.Queue;
import javax.jms.Topic;
import org.jboss.seam.jms.annotations.JmsDestination;
import org.jboss.seam.jms.annotations.Routing;
import org.jboss.seam.jms.bridge.RouteType;
import org.jboss.seam.jms.example.statuswatcher.model.Status;

public interface MappingInterface
{
   @Routing(RouteType.EGRESS)
   public void mapStatusToQueue(@Observes Status s, @JmsDestination(jndiName = "/jms/updateStatusQueue") Queue q);

//   @Routing(RouteType.INGRESS)
//   public void mapStatusToTopic(@Observes Status l, @JmsDestination(jndiName = "/jms/statusInfoTopic") Topic t);
}
