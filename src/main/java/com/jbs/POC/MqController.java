package com.jbs.POC;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jms.Session;
import java.io.IOException;

@RestController
public class MqController {
    @Autowired
    MQ mqManager;
    @Autowired
    JMS mqManagerJMS;
    @PostMapping(value = "/write/IBM")
    public String writeToQueueIBM(@RequestBody String body) throws MQException, IOException {
        try {
            MQQueueManager qm = mqManager.prepareQm("QM2");
            mqManager.writeToQueue(qm, "inQ", body);
            mqManager.closeQmConnection(qm);
        } catch (Exception e) {
            return "Error: Message Wasn't Added To Queue: inQ \n" + e.getMessage();
        }
        return "Message Added To Queue: inQ";

    }

    @GetMapping(value = "/read/IBM")
    public String readFromQueueIBM() throws MQException, IOException {
        MQQueueManager qm = mqManager.prepareQm("QM2");
        String output = mqManager.readFromQueue(qm, "inQ");
        mqManager.closeQmConnection(qm);
        return output;
    }

    @PostMapping(value = "/write/JMS")
    public String writeToQueueJMS(@RequestBody String body){
        try {
            Session session = mqManagerJMS.prepareQueueManagerConnection();
            mqManagerJMS.writeToQueue(session,"inQ",body);
            mqManagerJMS.closeQmConnection(session);
        } catch (Exception e) {
            return "Error: Message Wasn't Added To Queue: inQ \n" + e.getMessage();
        }
        return "Message Added To Queue: inQ";

    }

    @GetMapping(value = "/read/JMS")
    public String readFromQueueJMS() throws Exception {
        String output=null;
        try {
            Session session = mqManagerJMS.prepareQueueManagerConnection();
            output = mqManagerJMS.readFromQueue(session, "inQ");
            mqManagerJMS.closeQmConnection(session);
        } catch (Exception e) {
            return "Error: Message Wasn't Added To Queue: inQ \n" + e.getMessage();
        }
        return output;
    }
}
