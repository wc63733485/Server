package mqtt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import nio.Entity.DeviceWarnEntity;
import nio.Entity.DeviceWarnLogEntity;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyMqttT {


    String id = null;

    private String host = "tcp://127.0.0.1:1883";
    private String userName = "wxc";
    private String passWord = "123123";
    private MqttClient client;

    public MyMqttT(String id) {
        this(id, null, false);
        this.id = id;
    }

    public MyMqttT(String id, MqttCallback callback, boolean cleanSession) {
        System.out.println(id + "is run");

        try {
            client = new MqttClient(host, id, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);

        if (callback == null) {
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable arg0) {

                    System.out.println("recontent:" + arg0);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken arg0) {

                    System.out.println(id + "deliveryComplete:" + arg0);
                }

                @Override
                public void messageArrived(String arg0, MqttMessage arg1) throws Exception {

                    System.out.println(arg1.getPayload());
                }
            });
        } else {
            client.setCallback(callback);
        }
        try {
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    private MqttTopic mqttTopic;
    private MqttMessage message;

    public void sendMessage(String topic, String msg) {
        try {
            message = new MqttMessage();
            message.setQos(1);
            message.setRetained(true);
            message.setPayload(msg.getBytes());
            mqttTopic = client.getTopic(topic);
            MqttDeliveryToken token = mqttTopic.publish(message);//发布主题
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (MqttException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }
}