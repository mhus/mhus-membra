
Basis:
------

feature:repo-add activemq 5.12.1
feature:repo-add cxf 2.7.11

feature:install jdbc
feature:install openjpa
feature:install scr

feature:install activemq-broker

#  http://localhost:8185/activemqweb


install -s mvn:mysql/mysql-connector-java/5.1.18

install -s mvn:org.codehaus.jackson/jackson-core-asl/1.9.5
install -s mvn:org.codehaus.jackson/jackson-mapper-asl/1.9.5

install -s mvn:de.mhus.lib/mhu-lib-annotations/3.3.0-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-core/3.3.0-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-jms/3.3.0-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-persistence/3.3.0-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-karaf/3.3.0-SNAPSHOT

install -s mvn:de.mhus.osgi/jms-commands/1.3.0-SNAPSHOT
install -s mvn:de.mhus.osgi/mhus-osgi-commands/1.3.0-SNAPSHOT

install -s mvn:org.apache.httpcomponents/httpcore-osgi/4.2.1
install -s mvn:org.apache.httpcomponents/httpclient-osgi/4.2.1

install -s mvn:de.mhus.osgi/mailosgi/1.3.0-SNAPSHOT
install -s mvn:de.mhus.osgi/mailkaraf/1.3.0-SNAPSHOT

install -s mvn:de.mhus.sop/sop-api/0.0.1-SNAPSHOT
install -s mvn:de.mhus.sop/sop-impl/0.0.1-SNAPSHOT


---

bundle:persistentwatch add mhu-lib-annotations
bundle:persistentwatch add mhu-lib-persistence
bundle:persistentwatch add mhu-lib-logging
bundle:persistentwatch add mhu-lib-karaf
bundle:persistentwatch add mhu-lib-jms
bundle:persistentwatch add mhu-lib-vaadin
bundle:persistentwatch add mhu-lib-forms
bundle:persistentwatch add mhu-lib-core
bundle:persistentwatch add jms-commands
bundle:persistentwatch add mailosgi
bundle:persistentwatch add mailkaraf
bundle:persistentwatch add osgiquartz
bundle:persistentwatch add karafquartz

