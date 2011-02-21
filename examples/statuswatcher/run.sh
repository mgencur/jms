rm -rf /home/mgencur/Java/jboss6/jboss6final/jboss-6.0.0.Final/server/default/deploy/statuswatcher.war
#rm -rf /home/mgencur/Java/jboss6/jboss6final/jboss-6.0.0.Final/server/default/deploy/sample-ds.xml
#rm -rf /home/mgencur/Java/jboss6/jboss6final/jboss-6.0.0.Final/server/default/deploy/sample-hornetq-jms.xml
mvn clean package
cp  target/statuswatcher.war /home/mgencur/Java/jboss6/jboss6final/jboss-6.0.0.Final/server/default/deploy
cp  src/main/resources-jboss6/statuswatcher-hornetq-jms.xml src/main/resources-jboss6/statuswatcher-ds.xml /home/mgencur/Java/jboss6/jboss6final/jboss-6.0.0.Final/server/default/deploy

