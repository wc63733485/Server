package mqtt;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;


public class MqttStart {


    public static void main(String[] args) {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://39.96.74.32:25412/hssws?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("ASDzxc1993.");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(ds);
        List<String> codelist = jdbcTemplate.queryForList("select distinct IOT_code from device", String.class);

        for (String str : codelist) {
            MyMqtt myMqtt = new MyMqtt(str);
//        myMqtt.sendMessage("Topic/flexem/fbox/300220030307/system/MDataPubCycle","5");
        }

    }
}
