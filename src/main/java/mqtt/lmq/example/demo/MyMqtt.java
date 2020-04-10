package mqtt.lmq.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sws.base.dao.BaseDao;
import com.sws.base.dao.MongoDao;
import nio.Entity.DeviceEntity;
import nio.Entity.DeviceUnitEntity;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class MyMqtt {

    private final MongoDao mongoDao = new MongoDao();
    private final BaseDao baseDao = new BaseDao();

    String ret = "";
    private static List<DeviceUnitEntity> map;
    private static HashMap<String, Integer> r;

    private String host = "tcp://39.107.221.228:1883";
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

    public MyMqtt(String id, MqttCallback callback, boolean cleanSession) {
        try {
            //id应该保持唯一性
            client = new MqttClient(host, id, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(cleanSession);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(30);
            if (callback == null) {
                client.setCallback(new MqttCallback() {

                    @Override
                    public void connectionLost(Throwable arg0) {

                        System.out.println(id + " connectionLost " + arg0);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken arg0) {

                        System.out.println(id + " deliveryComplete " + arg0);
                    }

                    @Override
                    public void messageArrived(String arg0, MqttMessage arg1) throws Exception {

                        if (map == null) {
                            System.out.println("数据转换加载");
                            map = new ArrayList<DeviceUnitEntity>();
                            r = new HashMap();
                            DeviceUnitEntity due = null;
                            DeviceEntity de = new DeviceEntity();
                            de.setCode(id);
                            DeviceEntity deviceEntity = baseDao.queryOne(de, DeviceEntity.class);
                            if (deviceEntity != null) {
                                //以下代码代表设备类型字段会覆盖掉型号和版本的数据字段
                                due = new DeviceUnitEntity();
                                due.setDeviceTypeId(deviceEntity.getDeviceTypeId());
                                List<DeviceUnitEntity> deviceUnitEntities1 = baseDao.queryByCondition(due, DeviceUnitEntity.class, false);
                                due = new DeviceUnitEntity();
                                due.setDeviceModelId(deviceEntity.getDeviceModelId());
                                List<DeviceUnitEntity> deviceUnitEntities2 = baseDao.queryByCondition(due, DeviceUnitEntity.class, false);
                                due = new DeviceUnitEntity();
                                due.setDeviceEditionId(deviceEntity.getDeviceEditionId());
                                List<DeviceUnitEntity> deviceUnitEntities3 = baseDao.queryByCondition(due, DeviceUnitEntity.class, false);
                                map.addAll(deviceUnitEntities1);
                                map.addAll(deviceUnitEntities2);
                                map.addAll(deviceUnitEntities3);
                                for (DeviceUnitEntity deviceUnitEntity : map) {
                                    r.put(deviceUnitEntity.getDataName(), deviceUnitEntity.getMove());
                                }
                            }
                        }

                        JSONObject jso = new JSONObject();
                        JSONObject jsonObject1 = JSONObject.parseObject(arg1.toString());
                        jso.put("time", Calendar.getInstance().getTime().getTime());
                        System.out.println(jsonObject1.getString("time"));
                        JSONArray data = jsonObject1.getJSONArray("Data");
                        for (Object object : data) {
                            JSONObject jsonObject = JSONObject.parseObject(object.toString());
                            String name = jsonObject.getString("name");
                            if (r.containsKey(name)) {
                                jso.put(name, jsonObject.getDouble("value") / Math.pow(10,r.get(name)));
                            }
                        }
                        System.out.println(id + "--" + jso.toJSONString());
                        mongoDao.insert(jso, id);
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

    public void subscribe(String[] topicFilters, int[] qos) {
        try {
            client.subscribe(topicFilters, qos);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 订阅主题

    }

}