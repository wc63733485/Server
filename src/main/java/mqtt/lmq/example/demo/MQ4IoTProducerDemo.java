package mqtt.lmq.example.demo;


import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;


public class MQ4IoTProducerDemo {


    private static String[] topics = { "Topic/flexem/fbox/300020010108/system/MonitorData","Topic/flexem/fbox/300020010113/system/MonitorData",
            "Topic/flexem/fbox/300219070310/system/MonitorData",
            "Topic/flexem/fbox/300220030307/system/MonitorData"
    };
    private static int[] myQos = { 0,0,0,0 };

    public static void main(String[] args) {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://39.96.74.32:25412/hssws?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("ASDzxc1993.");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(ds);
        List<String> codelist = jdbcTemplate.queryForList("select distinct code from device", String.class);


        System.out.println("client start...");
        MyMqtt myMqtt = new MyMqtt("gateway",codelist);
        myMqtt.subscribe(topics, myQos);
        myMqtt.sendMessage("Topic/flexem/fbox/300220030307/system/MDataPubCycle","5");
        myMqtt.sendMessage("Topic/flexem/fbox/300219070310/system/MDataPubCycle","5");
        myMqtt.sendMessage("Topic/flexem/fbox/300020010108/system/MDataPubCycle","5");
        myMqtt.sendMessage("Topic/flexem/fbox/300020010113/system/MDataPubCycle","5");
    }
}
