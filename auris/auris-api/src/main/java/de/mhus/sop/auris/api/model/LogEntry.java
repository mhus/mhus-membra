package de.mhus.sop.auris.api.model;

import java.util.Date;
import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.model.AttributeFeatureCut;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbType.TYPE;
import de.mhus.lib.basics.UuidIdentificable;
import de.mhus.lib.core.MDate;
import de.mhus.sop.auris.api.AurisApi;

public class LogEntry extends DbComfortableObject implements UuidIdentificable {

	public enum LEVEL {TRACE, DEBUG, UNKNOWN, INFO, WARN, ERROR, FATAL}
	
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
	private LEVEL logLevel;
	@DbPersistent(size=600,features=AttributeFeatureCut.NAME)
	private String logMessage0;
	@DbPersistent(size=600,features=AttributeFeatureCut.NAME)
	private String logMessage1;
	@DbPersistent(size=600,features=AttributeFeatureCut.NAME)
	private String logMessage2;
	@DbPersistent(size=600,features=AttributeFeatureCut.NAME)
	private String logMessage3;
	@DbPersistent(size=600,features=AttributeFeatureCut.NAME)
	private String logMessage4;
	@DbPersistent(size=600,features=AttributeFeatureCut.NAME)
	private String logMessage5;
	
	@DbPersistent(size=100,features=AttributeFeatureCut.NAME)
	private String logTrace;
	@DbPersistent(size=150,features=AttributeFeatureCut.NAME)
	private String logSource0;
	@DbPersistent(size=150,features=AttributeFeatureCut.NAME)
	private String logSource1;
	@DbPersistent(size=150,features=AttributeFeatureCut.NAME)
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

	public LEVEL getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LEVEL logLevel) {
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

	public String getValue(String field) {
		switch (field) {
		case AurisApi.MESSAGE0: return getLogMessage0();
		case AurisApi.MESSAGE1: return getLogMessage1();
		case AurisApi.MESSAGE2: return getLogMessage2();
		case AurisApi.MESSAGE3: return getLogMessage3();
		case AurisApi.MESSAGE4: return getLogMessage4();
		case AurisApi.MESSAGE5: return getLogMessage5();
		case AurisApi.SOURCE0: return getLogSource0();
		case AurisApi.SOURCE1: return getLogSource1();
		case AurisApi.SOURCE2: return getLogSource2();
		case AurisApi.TRACE: return getLogTrace();
		case AurisApi.LEVEL: return String.valueOf( getLogLevel() );
		case AurisApi.FORMATED_DATE: return MDate.toIso8601(new Date(getCreated()));
		}
		return null;
	}

}
