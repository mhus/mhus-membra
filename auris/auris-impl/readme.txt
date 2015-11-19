
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


pre set hostmap class=de.mhus.sop.auris.impl.pre.HostMapping host_127.0.0.1=localhost
pre set ignore class=de.mhus.sop.auris.impl.pre.Ignore "condition=FrameworkEvent.*"
pre set tracemark class=de.mhus.sop.auris.impl.pre.MhuTraceMark

post set oom class=de.mhus.sop.auris.impl.post.OutOfMemoryMail to=mike@mhus.de subject=OutOfMemory

post console on FORMATED_DATE,SOURCE_HOST,LEVEL,MESSAGE0

