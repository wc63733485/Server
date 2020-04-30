package mqtt.lmq.example.demo;


import com.alibaba.fastjson.JSONObject;

/**
 * @author
 */
public class HandlerThread implements Runnable {

    private String topic;
    private String content;

    public HandlerThread(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }

    @Override
    public void run() {

        if (this.content.length() > 0) {
            //注: 下面逻辑处理，仅仅是我提供的简单案例，需要你们根据自己实际项目框架处理
            //最好是用工厂模式创建每个接口对应的处理对象，或调用每个接口对应的处理方法.
            try {
//                JSONObject json = new JSONObject(content);
//                String cmd_id = json.optString("cmd");
                String cmd_id="";

                if(topic.startsWith(MqttClientUtil.TOPIC_PREFIX_DEVICE)){
                    //来自device的消息
                    //...
                }else if(topic.startsWith(MqttClientUtil.TOPIC_PREFIX_APP)){
                    //来自app的消息
                    if("/test".equals(cmd_id)){
                        //从top 获取app 用户的 id
                        String id = topic.substring( topic.indexOf("app/") + 4 , topic.lastIndexOf("/client") );

                        //获取 发送给 指定id 的app用户的topic
                        String topic = MqttClientUtil.getAppPublishTopic(id);

                        //要发送消息的内容
                        JSONObject content = new JSONObject();
                        content.put("cmd",cmd_id+"/response");
                        content.put("ret","0");
                        content.put("msg","测试消息已经处成功处理");

                        //发布消息
                        MqttClientUtil.publish_common(topic,content.toString());
                    }
                }
            } catch (Throwable e) {
                System.out.println("mqtt HandlerThread run");
                e.printStackTrace();
            }
        }
    }

}