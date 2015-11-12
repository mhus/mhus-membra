
Auris:
------

install -s mvn:de.mhus.sop/sop-mfw-api/0.0.1-SNAPSHOT
install -s mvn:de.mhus.sop/sop-mfw-impl/0.0.1-SNAPSHOT
install -s mvn:de.mhus.sop/auris-api/0.0.1-SNAPSHOT
install -s mvn:de.mhus.sop/auris-impl/0.0.1-SNAPSHOT



bundle:persistentwatch add sop-mfw-api
bundle:persistentwatch add sop-mfw-impl
bundle:persistentwatch add auris-api
bundle:persistentwatch add auris-impl
