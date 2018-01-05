package org.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.tape.QueueFile;

import java.io.*;
import java.util.List;

public class DiskBackedQueueBehaviour {
//    private static final Logger logger = LoggerFactory.getLogger(DiskBackedQueueBehaviour.class);
    public QueueFile events;

    public DiskBackedQueueBehaviour(File f){
        try {
            events = new QueueFile(f);
        } catch (Exception ex) {
            ex.printStackTrace();
//            logger.error("Got Exception while Creating DiskBacked QueueFile " + ex.toString());
        }
    }


    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    private static byte[] jsonSerialize(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(obj).getBytes().length);
        return mapper.writeValueAsString(obj).getBytes();
    }

    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public Integer size() {
        return events.size();
    }

    public synchronized void add(Object ev) {
        try {
            events.add(jsonSerialize(ev));
            events.remove(); // COz size of file increase too much after some time and it give exceptions
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void drainTo(List<Object> eventSet, int batchSize) {
        byte[] data = null;
        int cnt = 0;
        while (events.isEmpty() == false && cnt < batchSize) {
            try {
                data = events.peek();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                            System.out.println(new String(data));
            try {
                eventSet.add((Event) deserialize(data));
            }catch (IOException ex){
                ex.printStackTrace();
            }catch(ClassNotFoundException ex){
                ex.printStackTrace();
            }

            try {
                events.remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cnt += 1;
        }
    }
}
