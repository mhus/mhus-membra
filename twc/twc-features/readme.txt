
feature:repo-add activemq
feature:repo-add cxf 2.7.11
feature:repo-add mvn:de.hfo.magic/magic-features/42-SNAPSHOT/xml/features

feature:install magic-mws
feature:install magic-bonitasoft
feature:install magic-mws-acs
feature:install driver-tr
feature:install driver-iccs


------------------------------------
feature:install webconsole
------------------------------------

bundle:persistentwatch add 

bundle:watch mhu-lib-jms
bundle:watch mhu-lib-karaf

------------------------------------

install -s mvn:de.hfo.magic.mws/mws-hfo-pub/42-SNAPSHOT
install -s mvn:de.hfo.magic.mws/mws-hfo-api/42-SNAPSHOT
install -s mvn:de.hfo.magic.mws/mws-hfo-impl/42-SNAPSHOT

install -s mvn:de.hfo.magic.mws/mws-hack-impl/42-SNAPSHOT
install -s mvn:de.hfo.magic.mws/mws-tr-impl/42-SNAPSHOT

------------------------------------

activemq over http:

feature:repo-add activemq
feature:install activemq-client

feature:install jdbc
feature:install openjpa
feature:install scr

install -s mvn:org.codehaus.jackson/jackson-core-asl/1.9.5
install -s mvn:org.codehaus.jackson/jackson-mapper-asl/1.9.5

install -s mvn:de.mhus.lib/mhu-lib-annotations
install -s mvn:de.mhus.lib/mhu-lib-core
install -s mvn:de.mhus.lib/mhu-lib-jms
install -s mvn:de.mhus.lib/mhu-lib-logging
install -s mvn:de.mhus.lib/mhu-lib-persistence
install -s mvn:de.mhus.lib/mhu-lib-karaf

install -s mvn:de.mhus.osgi/jms-commands

install -s mvn:org.apache.httpcomponents/httpcore-osgi/4.2.1
install -s mvn:org.apache.httpcomponents/httpclient-osgi/4.2.1
feature:install activemq

etc/activemq.xml:
<transportConnectors>
  <!-- DOS protection, limit concurrent connections to 1000 and frame size to 100MB -->
  <transportConnector name="http" uri="http://0.0.0.0:61617?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600&amp;wireFormat.maxInactivityDuration=0"/>
</transportConnectors>

direct-listen -u karaf -p karaf "failover:(http://localhost:61617)" test
direct-send -s -u karaf -p karaf http://localhost:61617 test test

url: http://localhost:8181/activemqweb


bundle:persistentwatch add mhu-lib-annotations
bundle:persistentwatch add mhu-lib-persistence
bundle:persistentwatch add mhu-lib-logging
bundle:persistentwatch add mhu-lib-karaf
bundle:persistentwatch add mhu-lib-jms
bundle:persistentwatch add mhu-lib-vaadin
bundle:persistentwatch add mhu-lib-forms
bundle:persistentwatch add mhu-lib-core
bundle:persistentwatch add jms-commands
bundle:persistentwatch add mws-core-api
bundle:persistentwatch add mws-acs-api
bundle:persistentwatch add mws-bpm-api
bundle:persistentwatch add mws-hfo-api
bundle:persistentwatch add mws-ngn-api
bundle:persistentwatch add mws-acs-impl
bundle:persistentwatch add mws-bpm-impl
bundle:persistentwatch add mws-core-impl
bundle:persistentwatch add mws-tool
bundle:persistentwatch add mds-tool
bundle:persistentwatch add mws-hfo-impl
bundle:persistentwatch add mws-ngn-impl
bundle:persistentwatch add mailosgi
bundle:persistentwatch add mailkaraf
bundle:persistentwatch add osgiquartz
bundle:persistentwatch add karafquartz
bundle:persistentwatch add driver-bonitasoft-rest
bundle:persistentwatch add lib-magic
bundle:persistentwatch add driver-tr
bundle:persistentwatch add driver-iccs
bundle:persistentwatch add sim-iccs
bundle:persistentwatch add mws-tr-impl
bundle:persistentwatch add sim-tr
bundle:persistentwatch add driver-qsc
bundle:persistentwatch add driver-colt
bundle:persistentwatch add driver-qsc-wham
bundle:persistentwatch add driver-dbd
bundle:persistentwatch add driver-dj
bundle:persistentwatch add driver-dj-tr

bundle:persistentwatch add gui-theme
bundle:persistentwatch add gui-api
bundle:persistentwatch add gui-core

