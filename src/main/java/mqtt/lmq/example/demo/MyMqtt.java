package mqtt.lmq.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sws.base.annotations.Entity;
import com.sws.base.util.JavaBeanUtil;
import com.sws.base.util.SqlUtil;
import com.sws.base.util.TimeUtil;
import nio.Entity.DeviceEntity;
import nio.Entity.DeviceUnitEntity;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.*;

public class MyMqtt {

    private static final SqlUtil sqlUtil = new SqlUtil();

    private String dbName = "device";

    private String[] uri = {"39.96.74.32:27837"};

    private String username = "root";

    private String password = "ASDzxc1993";

    String ret = "";
    private static List<DeviceUnitEntity> map;
    private static HashMap<String, Integer> r;
    String time = "";
    JSONArray array = null;
    private String host = "tcp://39.107.221.228:1883";
    private String userName = "wxc";
    private String passWord = "123123";
    private MqttClient client;
    private String id;
    private static MyMqtt instance; // = new MyMqtt();
    private MqttTopic mqttTopic;
    private MqttMessage message;

    public MyMqtt(String id) {
        this(id, null, false);
    }

    public MyMqtt(String id, MqttCallback callback, boolean cleanSession) {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://39.96.74.32:25412/hssws?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("ASDzxc1993.");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(ds);

        MongoClientOptions.Builder moptions = new MongoClientOptions.Builder();
        moptions.connectionsPerHost(200);// 连接池设置为300个连接,默认为100
        moptions.connectTimeout(30000);// 连接超时，推荐>3000毫秒
        moptions.maxWaitTime(50000); //
        moptions.socketTimeout(50000);// 套接字超时时间，0无限制
        moptions.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        moptions.writeConcern(WriteConcern.SAFE);//
        MongoClientOptions build = moptions.build();
        MongoCredential mongoCredential = MongoCredential.createCredential(username, "admin", password.toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("39.96.74.32:27837"), mongoCredential, build);
        MongoDatabase device = mongoClient.getDatabase("device");


//        List<String> codelist = jdbcTemplate.queryForList("select distinct code from device", String.class);
//
//        for (String s : codelist) {
//            MongoCollection<Document> collection = device.getCollection(s);
//            BasicDBObject bso = new BasicDBObject();
//            bso.append("$gt", TimeUtil.getLastDayStartMills());
//            bso.append("$lt", TimeUtil.getTodayStartMills());
//            long l = collection.countDocuments(bso);
//        }


        try {
            //id应该保持唯一性
            client = new MqttClient(host, id, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(cleanSession);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(10);
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

                            String sql = sqlUtil.BaseQueryNoPage(de, false, "id", 1);
                            System.out.println(sql);
                            RowMapper<DeviceEntity> rm = BeanPropertyRowMapper.newInstance(DeviceEntity.class);
                            DeviceEntity deviceEntity = (DeviceEntity) jdbcTemplate.queryForObject(sql, rm);

                            if (deviceEntity != null) {

                                //以下代码代表设备类型字段会覆盖掉型号和版本的数据字段
                                due = new DeviceUnitEntity();
                                due.setDeviceTypeId(deviceEntity.getDeviceTypeId());
                                List<DeviceUnitEntity> deviceUnitEntities1 = this.queryByCondition(due, DeviceUnitEntity.class, false, "id", 1);
                                due = new DeviceUnitEntity();
                                due.setDeviceModelId(deviceEntity.getDeviceModelId());
                                List<DeviceUnitEntity> deviceUnitEntities2 = this.queryByCondition(due, DeviceUnitEntity.class, false, "id", 1);
                                due = new DeviceUnitEntity();
                                due.setDeviceEditionId(deviceEntity.getDeviceEditionId());
                                List<DeviceUnitEntity> deviceUnitEntities3 = this.queryByCondition(due, DeviceUnitEntity.class, false, "id", 1);
                                map.addAll(deviceUnitEntities1);
                                map.addAll(deviceUnitEntities2);
                                map.addAll(deviceUnitEntities3);
                                for (DeviceUnitEntity deviceUnitEntity : map) {
                                    r.put(deviceUnitEntity.getDataName(), deviceUnitEntity.getMove());
                                }
                            }
                        }

                        if (new String(arg1.getPayload()).equals("")||arg1==null){
                            return;
                        }
                        System.out.println("out"+new String(arg1.getPayload()));
                        Document jso = new Document();
                        JSONObject jsonObjectRec = JSONObject.parseObject(new String(arg1.getPayload()));

                        String dataTime = jsonObjectRec.getString("time");
                        jso.put("time", Calendar.getInstance().getTime().getTime());
                        jso.put("date", dataTime);

                        JSONArray data = jsonObjectRec.getJSONArray("Data");
                        if (data.size() < 50) {
                            String time1 = jsonObjectRec.getString("time");
                            MyMqtt.this.time = dataTime;
                            for (Object object : data) {
                                JSONObject jsonObject = JSONObject.parseObject(object.toString());
                                String name = jsonObject.getString("name");
                                if (r.containsKey(name)) {
                                    jso.put(name, jsonObject.getDouble("value") / Math.pow(10, r.get(name)));
                                }
                            }
                            MongoCollection<Document> collection = device.getCollection(id);
                            collection.insertOne(jso);
                        } else {
                            if (MyMqtt.this.time.equals(dataTime)) {
                                array.addAll(data);
                                MyMqtt.this.time = dataTime;
                                for (Object object : array) {
                                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                                    String name = jsonObject.getString("name");
                                    if (r.containsKey(name)) {
                                        jso.put(name, jsonObject.getDouble("value") / Math.pow(10, r.get(name)));
                                    }
                                    jso.put(name, jsonObject.getDouble("value"));
                                }
                                MongoCollection<Document> collection = device.getCollection(id);
                                collection.insertOne(jso);
                            } else {
                                MyMqtt.this.time = dataTime;
                                array = data;
                            }
                        }
                    }

                    public <T> List<T> queryByCondition(Object obj, Class<T> clazz, boolean vague, String sort, int i) {
                        String sql = sqlUtil.BaseQueryNoPage(obj, vague, sort, i);
                        List<T> entities = new ArrayList<>();
                        for (Map<String, Object> map : jdbcTemplate.queryForList(sql)) {
                            T t = (T) JavaBeanUtil.mapToObject(map, clazz);
                            entities.add(t);
                        }
                        return entities;
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