package com.jbs.POC;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MQ {


    public MQQueueManager prepareQm(String queueManagerName) throws MQException {
        MQQueueManager qm;
        qm = new MQQueueManager(queueManagerName); // Connection Made

        return qm;
    }

    public void writeToQueue(MQQueueManager qm, String queueName, String messagePayload) throws MQException, IOException {

        MQQueue queue = qm.accessQueue(queueName, CMQC.MQOO_OUTPUT); // Define Queue
        //
        MQMessage message = new MQMessage(); // Prepare Message
        message.writeBytes(messagePayload);
        //
        queue.put(message);

    }

    public String readFromQueue(MQQueueManager qm, String queueName) throws MQException, IOException {
        MQQueue queue = qm.accessQueue(queueName, CMQC.MQOO_INPUT_SHARED);
        //
        MQMessage message = new MQMessage(); // Prepare Message
        queue.get(message);
        // Extract the message data
        int stringLength = message.getMessageLength();
        byte[] stringData = new byte[stringLength];
        message.readFully(stringData,0,stringLength);
        return new String(stringData,0);
    }

    public void closeQmConnection(MQQueueManager qm) throws MQException {

        qm.disconnect(); // Connection Closed

    }
}
