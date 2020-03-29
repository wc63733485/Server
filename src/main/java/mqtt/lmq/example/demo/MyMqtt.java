package mqtt.lmq.example.demo;

import mqtt.lmq.example.util.ConnectionOptionWrapper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyMqtt {
    private String host = "tcp://39.96.74.32:1883";
    private String userName = "wxc";
    private String passWord = "123123";
    private MqttClient client;
    private String id;
    private static MyMqtt instance; // = new MyMqtt();
    private MqttTopic mqttTopic;
//    private String myTopic = "Topics/htjs/serverToPhone";
    private MqttMessage message;
    public MyMqtt(String id) {
        this(id, null, false);
    }

    public MyMqtt(String id, MqttCallback callback, boolean cleanSession){
        try {
            //id应该保持唯一性
            client = new MqttClient(host, id, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(cleanSession);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            if (callback == null) {
                client.setCallback(new MqttCallback() {

                    @Override
                    public void connectionLost(Throwable arg0) {
                        // TODO 自动生成的方法存根
                        System.out.println(id + " connectionLost " + arg0);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken arg0) {
                        // TODO 自动生成的方法存根
                        System.out.println(id + " deliveryComplete " + arg0);
                    }

                    @Override
                    public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
                        // TODO 自动生成的方法存根
                        System.out.println(id + " messageArrived: " + arg1.toString());
                    }
                });
            } else {
                client.setCallback(callback);
            }
            client.connect(options);
        } catch (MqttException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

//    public void sendMessage(String msg) {
//        sendMessage(myTopic, msg);
//    }

    public void sendMessage(String topic, String msg){
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

    public void subscribe(String[] topicFilters, int[] qos) {
        try {
            client.subscribe(topicFilters, qos);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 订阅主题

    }

}