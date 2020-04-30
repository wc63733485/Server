package mqtt.lmq.example.demo;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.io.UnsupportedEncodingException;

/**
 * @author
 */
public class PublishThread implements Runnable {
    @Override
    public void run()
    {
        String[] obj = null;
        System.out.println("mqtt publish thread start");
        while (true)
        {
            obj = MqttClientUtil.poll();
            if (obj != null) {
                String topic = obj[0];
                String content = obj[1];
                System.out.println("mqtt从队列取出topic:"+topic+",content:"+content);
                byte[] array = null;
                try {
                    array = content.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                MqttMessage message2 = new MqttMessage(array);
                message2.setQos(1);
                try
                {
                    MqttClientUtil.mqttPublishClient.publish(topic, message2);
                    System.out.println("发送mqtt消息,topic: "+topic+" ,content: "+content);
                } catch (MqttPersistenceException e) {
                    System.out.println("发消息给设备,topic:"+topic+",content:"+content);
                    e.printStackTrace();
                } catch (MqttException e) {
                    System.out.println("发消息给设备,topic:"+topic+",content:"+content);
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("发消息给设备,topic:"+topic+",content:"+content);
                    e.printStackTrace();
                }
            }
            else{
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}