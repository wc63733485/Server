package mqtt.lmq.example.demo;


/**
 * @author zx
 */
public class All {
    public static void main(String[] args){
        //mqtt服务启动
        MqttClientUtil.createClient();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    //释放工作
                    //...

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
