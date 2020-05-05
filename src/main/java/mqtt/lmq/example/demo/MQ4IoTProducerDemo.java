package mqtt.lmq.example.demo;


import org.eclipse.paho.client.mqttv3.*;


public class MQ4IoTProducerDemo {

    private MqttClient client;
    private MqttConnectOptions options;
//    private static String[] myTopics1 = { "Topic/flexem/fbox/300220030307/system/MonitorData"  };
    private static String[] myTopics2 = { "Topic/flexem/fbox/300219070310/system/MonitorData" };
    private static int[] myQos = { 0 };

    public static void main(String[] args) {
        System.out.println("client start...");
        MyMqtt myMqtt = new MyMqtt("300219070310");
//        myMqtt.subscribe(myTopics1, myQos);
        myMqtt.subscribe(myTopics2, myQos);
//        myMqtt.sendMessage("Topic/flexem/fbox/300219070310/system/MDataPubCycle","60");
    }
}
