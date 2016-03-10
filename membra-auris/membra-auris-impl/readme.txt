
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


auris:connector set simple class=de.mhus.sop.auris.impl.logging.SimpleTcpReceiver
auris:connector set log4j class=de.mhus.sop.auris.impl.logging.Log4JTcpReceiver port=4561
auris:connector set jl class=de.mhus.sop.auris.impl.logging.JavaLoggerTcpReceiver port=4562


auris:pre set hostmap class=de.mhus.sop.auris.impl.pre.HostMapping host_/127.0.0.1=localhost
auris:pre set ignore class=de.mhus.sop.auris.impl.pre.Ignore "condition=FrameworkEvent.*"
auris:pre set tracemark class=de.mhus.sop.auris.impl.pre.MhuTraceMark

auris:post set oom class=de.mhus.sop.auris.impl.post.OutOfMemoryMail to=mike@mhus.de subject=OutOfMemory

auris:post console on FORMATED_DATE,SOURCE_HOST,LEVEL,MESSAGE0

