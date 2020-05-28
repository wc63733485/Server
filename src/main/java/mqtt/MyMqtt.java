package mqtt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import nio.Entity.DeviceWarnEntity;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyMqtt {

    private static String topic;
    private static int myQo = 0;

    private String username = "root";

    private String password = "ASDzxc1993";
    private Map move = new HashMap<String, Integer>();
    private Map warnMap = new HashMap<String, Long>();
    private Map warn = new HashMap<String, DeviceWarnEntity>();
    String id = null;
    String time = "";
    JSONArray array;
    private String host = "tcp://39.107.221.228:1883";
    private String userName = "wxc";
    private String passWord = "123123";
    private MqttClient client;

    public MyMqtt(String id) {
        this(id, null, false);
        this.id = id;
    }

    public MyMqtt(String id, MqttCallback callback, boolean cleanSession) {
        System.out.println(id + "is run");

        MongoClientOptions.Builder moptions = new MongoClientOptions.Builder();
        moptions.connectionsPerHost(200);// 连接池设置为300个连接,默认为100
        moptions.connectTimeout(60000);// 连接超时，推荐>3000毫秒
        moptions.maxWaitTime(60000); //
        moptions.socketTimeout(60000);// 套接字超时时间，0无限制
        moptions.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        moptions.writeConcern(WriteConcern.SAFE);//
        MongoClientOptions build = moptions.build();
        MongoCredential mongoCredential = MongoCredential.createCredential(username, "admin", password.toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("39.96.74.32:27837"), mongoCredential, build);
        MongoDatabase device = mongoClient.getDatabase("device");
        MongoDatabase warnLog = mongoClient.getDatabase("warnLog");

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

                    System.out.println(Calendar.getInstance().getTime().toLocaleString()+"recontent：" + arg0);
                    topic = "Topic/flexem/fbox/" + id + "/system/MonitorData";
                    System.out.println("client start...");
                    new MyMqtt(id);
                    arg0.printStackTrace();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken arg0) {

                    System.out.println(id + " deliveryComplete " + arg0);
                }

                @Override
                public void messageArrived(String arg0, MqttMessage arg1) throws Exception {

                    String id = arg0.split("/")[3];
                    if (move.size() == 0) {
                        move = MqttStart.deviceInfo.get(id + MqttStart.MOVE);
                    }
                    if (warn.size() == 0) {
                        warn = MqttStart.deviceInfo.get(id + MqttStart.WARN);
                    }

                    if (new String(arg1.getPayload()).equals("") || arg1 == null || new String(arg1.getPayload()).contains("error")) {
                        return;
                    }
                    Document jso = new Document();
                    Document jso1 = new Document();

                    JSONObject jsonObjectRec = JSONObject.parseObject(new String(arg1.getPayload()));

                    String dataTime = jsonObjectRec.getString("time");
                    jso.put("time", Calendar.getInstance().getTime().getTime());
                    jso.put("date", dataTime);

                    JSONArray data = jsonObjectRec.getJSONArray("Data");

                    if (data.size()==50){
                        MyMqtt.this.time = dataTime;
                        array = data;
                    }else {
                        if (MyMqtt.this.time.equals(dataTime)&&null!=array) {
                            array.addAll(data);
                            MyMqtt.this.time = dataTime;
                            for (Object object : array) {
                                JSONObject jsonObject = JSONObject.parseObject(object.toString());
                                String name = jsonObject.getString("name");
                                if (move.containsKey(name)) {
                                    jso.put(name, jsonObject.getDouble("value") / Math.pow(10, Double.valueOf(move.get(name).toString())));
                                } else {
                                    jso.put(name, jsonObject.getDouble("value"));
                                }

                                if (warn.containsKey(name)) {
                                    DeviceWarnEntity deviceWarnEntity = (DeviceWarnEntity) warn.get(name);
                                    String warnCode = deviceWarnEntity.getIOTCode() + deviceWarnEntity.getDataName();
                                    if (warnMap.containsKey(warnCode)) {
                                        if (Calendar.getInstance().getTime().getTime() - (long) warnMap.get(warnCode) > deviceWarnEntity.getWarnTimeInterval()) {
                                            warnSort(deviceWarnEntity,jso,name,jso1, warnCode,warnLog);
                                        }
                                    }else {
                                        warnSort(deviceWarnEntity,jso,name,jso1, warnCode,warnLog);
                                    }
                                }

                            }
                        }else{
                            MyMqtt.this.time = dataTime;
                            for (Object object : data) {
                                JSONObject jsonObject = JSONObject.parseObject(object.toString());
                                String name = jsonObject.getString("name");
                                if (move.containsKey(name)) {
                                    jso.put(name, jsonObject.getDouble("value") / Math.pow(10, (Integer) move.get(name)));
                                } else {
                                    jso.put(name, jsonObject.getDouble("value"));
                                }

                                if (warn.containsKey(name)) {
                                    DeviceWarnEntity deviceWarnEntity = (DeviceWarnEntity) warn.get(name);
                                    String warnCode = deviceWarnEntity.getIOTCode() + deviceWarnEntity.getDataName();
                                    if (warnMap.containsKey(warnCode)) {
                                        if (Calendar.getInstance().getTime().getTime() - (long) warnMap.get(warnCode) > deviceWarnEntity.getWarnTimeInterval()) {
                                            warnSort(deviceWarnEntity,jso,name,jso1, warnCode,warnLog);
                                        }
                                    }else {
                                        warnSort(deviceWarnEntity,jso,name,jso1, warnCode,warnLog);
                                    }
                                }
                            }
                        }
                        device.getCollection(id).insertOne(jso);

                    }
                }
            });
        } else {
            client.setCallback(callback);
        }
        try {
            topic = "Topic/flexem/fbox/" + id + "/system/MonitorData";
            client.connect(options);
            client.subscribe(topic, myQo);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void warnSort(DeviceWarnEntity deviceWarnEntity,Document jso,String name, Document jso1,String warnCode,MongoDatabase warnLog) {
        if (deviceWarnEntity.getSign().equals("1")) {
            if (jso.getDouble(name) - deviceWarnEntity.getNumber()>0) {
                warnHandle(deviceWarnEntity,jso.get(name),jso1, warnCode,warnLog);
            }
        }else if (deviceWarnEntity.getSign().equals("2")) {
            if (jso.getDouble(name)-deviceWarnEntity.getNumber()<0) {
                warnHandle(deviceWarnEntity,jso.get(name),jso1, warnCode,warnLog);
            }
        }else if (deviceWarnEntity.getSign().equals("3")) {
            if (jso.getDouble(name)-deviceWarnEntity.getNumber()==0) {
                warnHandle(deviceWarnEntity,jso.get(name),jso1, warnCode,warnLog);
            }
        }
    }

    public void warnHandle(DeviceWarnEntity deviceWarnEntity,Object object, Document jso1,String warnCode,MongoDatabase warnLog) {
        ZonedDateTime today = ZonedDateTime.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        jso1.put("level", deviceWarnEntity.getLevel());
        jso1.put("warnNumber", deviceWarnEntity.getNumber());
        jso1.put("remake", deviceWarnEntity.getRemake());
        jso1.put("source", deviceWarnEntity.getSource());
        jso1.put("number", object);
        jso1.put("Date", today.format(formatters));
        jso1.put("sign", deviceWarnEntity.getSign());
        jso1.put("IOTCode", deviceWarnEntity.getIOTCode());
        jso1.put("dataName", deviceWarnEntity.getDataName());
        jso1.put("time", Calendar.getInstance().getTime().getTime());
        jso1.put("status", 0);
        warnMap.put(warnCode, Calendar.getInstance().getTime().getTime());
        warnLog.getCollection("warnLog").insertOne(jso1);
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