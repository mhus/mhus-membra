package de.mhus.sop.auris.api.model;

import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.model.AttributeFeatureCut;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbType.TYPE;
import de.mhus.lib.basics.UuidIdentificable;

public class LogEntry extends DbComfortableObject implements UuidIdentificable {

	@DbPrimaryKey
	private UUID id;
	@DbPersistent(size=30,features=AttributeFeatureCut.NAME)
	private String sourceHost;
	@DbPersistent(size=10,features=AttributeFeatureCut.NAME)
	private String sourceConnector;
	@DbPersistent(size=25,features=AttributeFeatureCut.NAME)
	private String sourceConnectorType;
	
	@DbPersistent
	private long created;

	@DbPersistent
	private int logLevel;
	@DbPersistent(size=50,features=AttributeFeatureCut.NAME)
	private String logMessage0;
	@DbPersistent(size=50,features=AttributeFeatureCut.NAME)
	private String logMessage1;
	@DbPersistent(size=50,features=AttributeFeatureCut.NAME)
	private String logMessage2;
	@DbPersistent(size=50,features=AttributeFeatureCut.NAME)
	private String logMessage3;
	@DbPersistent(size=50,features=AttributeFeatureCut.NAME)
	private String logMessage4;
	@DbPersistent(size=50,features=AttributeFeatureCut.NAME)
	private String logMessage5;
	
	@DbPersistent(size=10,features=AttributeFeatureCut.NAME)
	private String logTrace;
	@DbPersistent(size=30,features=AttributeFeatureCut.NAME)
	private String logSource0;
	@DbPersistent(size=30,features=AttributeFeatureCut.NAME)
	private String logSource1;
	@DbPersistent(size=30,features=AttributeFeatureCut.NAME)
	private String logSource2;

	@DbPersistent(type=TYPE.BLOB)
	private String fullMessage;

	public String getSourceHost() {
		return sourceHost;
	}

	public void setSourceHost(String sourceHost) {
		this.sourceHost = sourceHost;
	}

	public String getSourceConnector() {
		return sourceConnector;
	}

	public void setSourceConnector(String sourceConnector) {
		this.sourceConnector = sourceConnector;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogMessage0() {
		return logMessage0;
	}

	public void setLogMessage0(String logMessage0) {
		this.logMessage0 = logMessage0;
	}

	public String getLogMessage1() {
		return logMessage1;
	}

	public void setLogMessage1(String logMessage1) {
		this.logMessage1 = logMessage1;
	}

	public String getLogMessage2() {
		return logMessage2;
	}

	public void setLogMessage2(String logMessage2) {
		this.logMessage2 = logMessage2;
	}

	public String getLogMessage3() {
		return logMessage3;
	}

	public void setLogMessage3(String logMessage3) {
		this.logMessage3 = logMessage3;
	}

	public String getLogMessage4() {
		return logMessage4;
	}

	public void setLogMessage4(String logMessage4) {
		this.logMessage4 = logMessage4;
	}

	public String getLogMessage5() {
		return logMessage5;
	}

	public void setLogMessage5(String logMessage5) {
		this.logMessage5 = logMessage5;
	}

	public String getLogTrace() {
		return logTrace;
	}

	public void setLogTrace(String logTrace) {
		this.logTrace = logTrace;
	}

	public String getLogSource0() {
		return logSource0;
	}

	public void setLogSource0(String logSource0) {
		this.logSource0 = logSource0;
	}

	public String getLogSource1() {
		return logSource1;
	}

	public void setLogSource1(String logSource1) {
		this.logSource1 = logSource1;
	}

	public String getLogSource2() {
		return logSource2;
	}

	public void setLogSource2(String logSource2) {
		this.logSource2 = logSource2;
	}

	public String getFullMessage() {
		return fullMessage;
	}

	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	public UUID getId() {
		return id;
	}

	public String getSourceConnectorType() {
		return sourceConnectorType;
	}

	public void setSourceConnectorType(String sourceConnectorType) {
		this.sourceConnectorType = sourceConnectorType;
	}

	
}
