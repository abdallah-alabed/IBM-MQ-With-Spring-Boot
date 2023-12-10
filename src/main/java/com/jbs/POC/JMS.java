package com.jbs.POC;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.IOException;

@Component
public class JMS {

    public Session prepareQueueManagerConnection() throws JMSException {
        MQConnectionFactory factory = new MQConnectionFactory();
        factory.setQueueManager("QM2");
        factory.setHostName("localhost");
        factory.setPort(1414);
        factory.setChannel("Channel1");
        Session session;
        Connection conn = factory.createConnection();
        session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
        conn.start();
        return session;
    }

    public void writeToQueue(Session session, String queueName, String messagePayload) throws JMSException {
        Destination inQ = session.createQueue("inQ");// Define Queue
        //
        MessageProducer producer = session.createProducer(inQ); // Create Message Producer
        TextMessage outMessage = session.createTextMessage(messagePayload); // Create Message
        producer.send(outMessage); // Send Message In Session
    }

    public String readFromQueue(Session session, String queueName) throws JMSException {
        Queue inQ = session.createQueue(queueName);// Define Queue
        //
        MessageConsumer consumer = session.createConsumer(inQ);
        Message inMessage = consumer.receive(12000);
        if(inMessage instanceof javax.jms.TextMessage){
            return ((TextMessage) inMessage).getText();
        }
           return "Error Retrieving Message From Queue";
    }

    public void closeQmConnection(Session session) throws JMSException {
        session.close();
    }

}
