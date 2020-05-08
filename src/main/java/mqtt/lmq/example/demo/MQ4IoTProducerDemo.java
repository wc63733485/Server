package mqtt.lmq.example.demo;


import org.eclipse.paho.client.mqttv3.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;


public class MQ4IoTProducerDemo {
    private MqttClient client;
    private MqttConnectOptions options;
    //    private static String[] myTopics1 = { "Topic/flexem/fbox/300220030307/system/MonitorData"  };
    private static String[] myTopics2 = {"Topic/flexem/fbox/300219070310/system/MonitorData"};
    private static int[] myQos = {0};

    public static void main(String[] args) {

        ConcurrentHashMap<String, MyMqtt> map = new ConcurrentHashMap<>();

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://39.96.74.32:25412/hssws?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("ASDzxc1993.");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(ds);


        Runnable runnable = new Runnable() {
            CopyOnWriteArrayList list = new CopyOnWriteArrayList();


            public void run() {
                List<String> codelist = jdbcTemplate.queryForList("select distinct code from device", String.class);

                for (String code:codelist){
                    if (!map.containsKey(code)){
                        MyMqtt myMqtt = new MyMqtt(code);
                        String[] myTopics = {"Topic/flexem/fbox/"+code+"/system/MonitorData"};
                        myMqtt.subscribe(myTopics, myQos);
                        map.put(code,myMqtt);
                    }
                }
            };
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 60, TimeUnit.SECONDS);


//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println();
//            }
//        });
//        System.out.println("client start...");
//        MyMqtt myMqtt = new MyMqtt("300219070310");
//        myMqtt.subscribe(myTopics1, myQos);
//        myMqtt.subscribe(myTopics2, myQos);
//        myMqtt.sendMessage("Topic/flexem/fbox/300219070310/system/MDataPubCycle","60");
    }
}
