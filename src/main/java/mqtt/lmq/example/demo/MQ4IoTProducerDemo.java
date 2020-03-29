package mqtt.lmq.example.demo;


import org.eclipse.paho.client.mqttv3.*;


public class MQ4IoTProducerDemo {

    private MqttClient client;
    private MqttConnectOptions options;
    private static String[] myTopics = { "Topic/flexem/fbox/300219070310/system/MonitorData" };
    private static int[] myQos = { 0 };

    public static void main(String[] args) {
        System.out.println("client start...");
        MyMqtt myMqtt = new MyMqtt("client");
        myMqtt.subscribe(myTopics, myQos);
    }
}
