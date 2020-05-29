package mqtt;

import com.sws.base.util.JavaBeanUtil;
import com.sws.base.util.SqlUtil;
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
import java.util.concurrent.ConcurrentHashMap;


public class MqttStart {

    public static ConcurrentHashMap<String, HashMap> deviceInfo = new ConcurrentHashMap<String, HashMap>();
    public static final SqlUtil sqlUtil = new SqlUtil();
    public static final String MOVE = "MOVE";
    public static final String WARN = "WARN";
    public static JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {


        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://39.96.74.32:25412/hssws?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("ASDzxc1993.");

        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(ds);

        String sql = sqlUtil.BaseQueryAll(new DeviceEntity(), "id", 1);
        System.out.println(sql);
        List<DeviceEntity> deviceEntity = new ArrayList<>();
        for (Map<String, Object> map : jdbcTemplate.queryForList(sql)) {
            DeviceEntity t = JavaBeanUtil.mapToObject(map, DeviceEntity.class);
//            if (t.getIOTCode().equals("300220030307")) {

            deviceEntity.add(t);
//            }
        }


        HashMap<String, Integer> move;
        HashMap<String, DeviceWarnEntity> warn;
        DeviceUnitEntity due;
        DeviceTypeBindEntity deviceTypeBindEntity;
        DeviceWarnEntity dwe;
        move = new HashMap<>();
        due = new DeviceUnitEntity();

        List<Map<String, Object>> id = jdbcTemplate.queryForList(sqlUtil.BaseQueryAll(due, "id", 1));

        for (Map<String, Object> map : id) {
            DeviceUnitEntity t = JavaBeanUtil.mapToObject(map, DeviceUnitEntity.class);
            if (null != t.getMove() && 0 != t.getMove()) {
                move.put(t.getDataName(), t.getMove());
            }
        }
        deviceInfo.put(MOVE, move);
        for (DeviceEntity device : deviceEntity) {
            warn = new HashMap<>();
            if (!device.getIOTCode().equals("") && null != device.getIOTCode()) {
                String iotCode = device.getIOTCode();
                if (null != device.getDeviceTypeId()) {


                    due = new DeviceUnitEntity();
                    due.setIsAlarmData("1");

                    for (Map<String, Object> map : jdbcTemplate.queryForList(sqlUtil.BaseQueryNoPage(due, false, "id", 1))) {
                        DeviceUnitEntity deviceUnitEntity = JavaBeanUtil.mapToObject(map, DeviceUnitEntity.class);
                        DeviceWarnEntity t = new DeviceWarnEntity();
                        t.setIOTCode(iotCode);
                        t.setDataName(deviceUnitEntity.getDataName());
                        t.setLevel("3");
                        t.setNumber(1.0);
                        t.setRemake(deviceUnitEntity.getName());
                        t.setSign("3");
                        t.setSource("device");
                        t.setWarnTimeInterval(900000.0);
                        warn.put(deviceUnitEntity.getDataName(), t);
                    }


                    dwe = new DeviceWarnEntity();
                    dwe.setIOTCode(iotCode);
                    for (Map<String, Object> map : jdbcTemplate.queryForList(sqlUtil.BaseQueryNoPage(dwe, false, "id", 1))) {
                        DeviceWarnEntity t = JavaBeanUtil.mapToObject(map, DeviceWarnEntity.class);
                        warn.put(t.getDataName(), t);
                    }


                    deviceInfo.put(iotCode + WARN, warn);
                    //有类型 网关才开始接受分析数据
                    new MyMqtt(iotCode);
                    iotCode = null;
//                    myMqtt.sendMessage("Topic/flexem/fbox/300220030307/system/MDataPubCycle","10");
//                    myMqtt.sendMessage("Topic/flexem/fbox/300219070310/system/MDataPubCycle","10");
//                    myMqtt.sendMessage("Topic/flexem/fbox/300020010108/system/MDataPubCycle","10");
//                    myMqtt.sendMessage("Topic/flexem/fbox/300020010113/system/MDataPubCycle","10");

                }
            }
        }
    }
}
