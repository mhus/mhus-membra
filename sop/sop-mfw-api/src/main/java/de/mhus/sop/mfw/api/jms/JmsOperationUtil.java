package de.mhus.sop.mfw.api.jms;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.jms.ClientJms;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.MJms;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.MfwApi;
import de.mhus.sop.mfw.api.aaa.AaaContext;
import de.mhus.sop.mfw.api.util.TicketUtil;

public class JmsOperationUtil {

	private static Log log = Log.getLog(JmsOperationUtil.class);
	
	public static OperationResult doExecuteOperation(JmsConnection con, String queueName, String operationName, IProperties parameters, AaaContext user, boolean needAnswer ) throws Exception {
		MfwApi api = Mfw.getApi(MfwApi.class);
		String ticket = api.createTrustTicket(user);
		return doExecuteOperation(con, queueName, operationName, parameters, ticket, MTimeInterval.MINUTE_IN_MILLISECOUNDS / 2, needAnswer);
	}
	
	public static OperationResult doExecuteOperation(JmsConnection con, String queueName, String operationName, IProperties parameters, String ticket, long timeout, boolean needAnswer ) throws Exception {

		if (con == null) throw new JMSException("connection is null");
		ClientJms client = new ClientJms(con.createQueue(queueName));
		
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "de,java,org,com"); //HACK! !!!!
		
		boolean needObject = false;
		for (Entry<String, Object> item : parameters) {
			Object value = item.getValue();
			if (! (value == null || value.getClass().isPrimitive() || value instanceof String ) ) {
				needObject = true;
				break;
			}
		}
		
		Message msg = null;
		if (needObject) {
			msg = con.createObjectMessage((MProperties)parameters);
		} else {
			msg = con.createMapMessage();
			for (Entry<String, Object> item : parameters) {
				//String name = item.getKey();
				//if (!name.startsWith("_"))
				((MapMessage)msg).setObject(item.getKey(), item.getValue()); //TODO different types, currently it's only String ?!
			}
			((MapMessage)msg).getMapNames();
		}
		
		msg.setStringProperty(Mfw.PARAM_OPERATION_PATH, operationName);


		msg.setStringProperty(Mfw.PARAM_AAA_TICKET, ticket );
		client.setTimeout(timeout);
    	// Send Request
    	
    	log.d(operationName,"sending Message", queueName, msg, needAnswer);
    	
    	if (!needAnswer) {
    		client.sendJmsOneWay(msg);
    		return null;
    	}
    	
    	Message answer = client.sendJms(msg);

    	// Process Answer
    	
    	OperationResult out = new OperationResult();
    	out.setOperationPath(operationName);
		if (answer == null) {
			log.d(queueName,operationName,"answer is null");
			out.setSuccessful(false);
			out.setMsg("answer is null");
			out.setReturnCode(OperationResult.INTERNAL_ERROR);
		} else {
			boolean successful = answer.getBooleanProperty(Mfw.PARAM_SUCCESSFUL);
			out.setSuccessful(successful);
			
			if (!successful)
				out.setMsg(answer.getStringProperty(Mfw.PARAM_MSG));
			out.setReturnCode(answer.getLongProperty(Mfw.PARAM_RC));
			
			if (successful) {
				
				if (answer instanceof MapMessage) {
					MapMessage mapMsg = (MapMessage)answer;
					out.setResult(MJms.getMapProperties(mapMsg));
				} else
				if (answer instanceof TextMessage) {
					out.setMsg(((TextMessage)answer).getText());
					out.setResult(out.getMsg());
				} else
				if (answer instanceof BytesMessage) {
					long len = ((BytesMessage)answer).getBodyLength();
					if (len > Mfw.MAX_MSG_BYTES) {
						out.setMsg("answer bytes too long " + len);
						out.setSuccessful(false);
						out.setReturnCode(OperationResult.INTERNAL_ERROR);
					} else {
						byte[] bytes = new byte[(int) len];
						((BytesMessage)answer).readBytes(bytes);
						out.setResult(bytes);
					}
				} else
				if (answer instanceof ObjectMessage) {
					Serializable obj = ((ObjectMessage)answer).getObject();
					if (obj == null) {
						out.setResult(null);
					} else {
						out.setResult(obj);
					}
				}
			}	
		}
		
		
		client.close();
		
		return out;
	}
	
	/**
	 * Return a list of possible operations from the other side. Could return null if a error occured.
	 * @param con
	 * @param queueName
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static List<String>	doGetOperationList(JmsConnection con, String queueName, AaaContext user) throws Exception {
		IProperties pa = new MProperties();
		OperationResult ret = doExecuteOperation(con, queueName, "_list", pa, user, true);
		if (ret.isSuccessful()) {
			Object res = ret.getResult();
			if (res != null && res instanceof MProperties) {
				String[] list = String.valueOf( ((MProperties)res).getString("list","") ).split(",");
				LinkedList<String> out = new LinkedList<String>();
				for (String item : list) out.add(item);
				return out;
			}
		}
		return null;
	}

}
