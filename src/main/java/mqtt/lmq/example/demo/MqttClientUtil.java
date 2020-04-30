package mqtt.lmq.example.demo;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zx
 */
public class MqttClientUtil {

    public static MqttAsyncClient mqttClient;
    public static MqttAsyncClient mqttPublishClient;
    public static String TOPIC_PREFIX_DEVICE = "device";
    public static String TOPIC_PREFIX_APP = "app";
    private static MqttClientCallback mqttClientCallback = new MqttClientCallback();

    /**
     * 要发布的消息队列
     */
    private static LinkedBlockingQueue<String[]> queue = new LinkedBlockingQueue<String[]>();
    public static ExecutorService executorService = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors() + 1);

    /**
     * mqtt broker 连接配置,填自己的mqtt地址,及账号密码
     */
    private static String broker = "tcp://39.107.221.228:1883";
    private static String username = "wxc";
    private static String password = "123123";

    static {

    }

    public static void createClient() {
        System.out.println("mqtt createClient, " + broker);
        String clientId = "mqttserver" + String.valueOf(System.currentTimeMillis());
        System.out.println("mqtt sub_clientId="+clientId+",pub_clientId="+clientId+"-publish");
        try {
            //subscribe client
            mqttClient = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
            mqttClient.setCallback(mqttClientCallback);


            //publish client
            mqttPublishClient = new MqttAsyncClient(broker, clientId + "-publish", new MemoryPersistence());
            mqttPublishClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable arg0) {
                    MqttClientUtil.publishReconnect();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken arg0) {
                }

                @Override
                public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
                    System.out.println("123123123");
                }
            });
            subscribeReconnect();
            publishReconnect();
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

        //启动发布消息的线程, 循环从队列queue 获取要发布的消息 发布。也就是说要发消息，只要将消息写入队列queue
        new Thread(new PublishThread()).start();
    }

    /**
     * 发布消息
     * @param topic
     * @param content
     */
    public static void publish_common(String topic, String content) {
        String[] array = { topic, content };
        //要发布的消息，入队列。
        //PublishThread 会循环从这个队列获取消息发布.
        queue.offer(array);
    }

    public static String[] poll() {
        return (String[]) queue.poll();
    }

    public static String getDeviceIdByTopic(String topic) {
        String[] array = topic.split("/");
        if (array.length >= 2) {
            return array[1];
        }
        return "";
    }

    public static String getDevicePublishTopic(String deviceId) {
        return TOPIC_PREFIX_DEVICE + "/" + deviceId + "/server";
    }

    public static String getAppPublishTopic(String deviceId) {
        return TOPIC_PREFIX_APP + "/" + deviceId + "/server";
    }

    /**
     * 订阅连接 重连
     */
    public static void subscribeReconnect() {
        if (mqttClient != null) {
            System.out.println("mqtt subscribeReconnect");
            try {
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                connOpts.setKeepAliveInterval(5);
                connOpts.setMaxInflight(100000);
                if (username != null && !"".equals(username)) {
                    connOpts.setUserName(username);
                    connOpts.setPassword(password.toCharArray());
                }

                mqttClient.connect(connOpts, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        try {
                            System.out.println("mqtt 已连接!");
//                            MqttClientUtil.mqttClient.subscribe(MqttClientUtil.TOPIC_PREFIX_DEVICE + "/+/client", 0);
                            MqttClientUtil.mqttClient.subscribe("Topic/flexem/fbox/300219070310/system/MonitorData", 0);
//                            MqttClientUtil.mqttClient.subscribe(MqttClientUtil.TOPIC_PREFIX_APP + "/+/client", 0);

                            System.out.println("订阅主题:"+MqttClientUtil.TOPIC_PREFIX_DEVICE + "/+/client");
                            System.out.println("订阅主题:"+MqttClientUtil.TOPIC_PREFIX_APP + "/+/client");
                        } catch (MqttException me) {
                            System.out.println("reason " + me.getReasonCode());
                            System.out.println("msg " + me.getMessage());
                            System.out.println("loc " + me.getLocalizedMessage());
                            System.out.println("cause " + me.getCause());
                            System.out.println("excep " + me);
                            me.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        System.out.println("mqtt 没有连接上:" + exception.getMessage());
                        try {
                            Thread.sleep(60000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MqttClientUtil.subscribeReconnect();
                    }
                });
            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            }
        }
    }

    /**
     * 发布连接 重连
     */
    public static void publishReconnect() {
        if (mqttPublishClient != null) {
            System.out.println("mqtt publishReconnect");
            try {
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                connOpts.setMaxInflight(100000);
                if (username != null && !"".equals(username)) {
                    connOpts.setUserName(username);
                    connOpts.setPassword(password.toCharArray());
                }
                mqttPublishClient.connect(connOpts, null, new IMqttActionListener() {
                    @Override
                    public void onFailure(IMqttToken arg0, Throwable arg1) {
                        System.out.println("mqtt publish client connect is failed" + arg1.getMessage());
                        try {
                            Thread.sleep(30000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MqttClientUtil.publishReconnect();
                    }

                    @Override
                    public void onSuccess(IMqttToken arg0) {
                        System.out.println("mqtt publish client is connected");
                    }
                });
            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            }
        }
    }
}