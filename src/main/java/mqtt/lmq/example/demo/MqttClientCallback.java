package mqtt.lmq.example.demo;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author
 */
public class MqttClientCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable arg0) {
        System.out.println("mqtt 失去了连接");
        MqttClientUtil.subscribeReconnect();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        System.out.println("mqtt 发送完成！");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        String content = new String(message.getPayload(), "utf-8");
        System.out.println("收到mqtt消息,topic: " + topic + " ,content: " + content);
        if (MqttClientUtil.executorService != null) {
            MqttClientUtil.executorService.execute(new HandlerThread(topic, content));
        }
    }
}