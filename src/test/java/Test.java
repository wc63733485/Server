import Entity.DeviceEntity;
import com.alibaba.fastjson.JSONObject;
import com.sws.base.dao.MongoDao;
import com.sws.base.initializeLoad.InitializeConfig;

import java.util.HashMap;
import java.util.Map;

public class Test {

//    public static void main(String[] args) throws Exception {
//        BaseService bs = new BaseServiceImpl();
//
//        PumpHouseEntity pm = new PumpHouseEntity();
//        pm.setCode("hs2020001");
//        //1. 查询所有泵房信息
//        List<Object> code = bs.findAllByPage(pm, 0, 2, "code", -1);
//        for (Object s : code) {
//            System.out.println(s.toString());
//        }
//    }

    public static void main(String[] args) throws Exception {
        int c8 = Integer.parseInt("c8", 16);
        System.out.println("args = [" + c8 + "]");

    }
}
