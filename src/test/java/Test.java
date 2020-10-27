import com.sws.base.util.JavaBeanUtil;
import mqtt.MyMqttT;
import nio.Entity.DeviceEntity;
import nio.Entity.DeviceTypeBindEntity;
import nio.Entity.DeviceUnitEntity;
import nio.Entity.DeviceWarnEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {
        MyMqttT myMqtt = new MyMqttT("123321");
        myMqtt.sendMessage("aaa","啊实打实发放");

    }
}
