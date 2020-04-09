package util;

import com.mongodb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MongoDBUtil {

    private static final String DBMP = "dbmap";

    private static ConcurrentHashMap<String, MongoClient> concurrentHashMap = new ConcurrentHashMap<>();

    public MongoClient mongoClient = this.getInstance();


    public static MongoClient getInstance() {

        if (concurrentHashMap.get(DBMP) != null){
            return concurrentHashMap.get(DBMP);
        }

        List<String> strings = Arrays.asList("39.96.74.32:27837".split(","));

        List<ServerAddress> listHost = new ArrayList<ServerAddress>();
        ServerAddress sa = null;
        for (String str : strings) {
            sa = new ServerAddress(str);
            listHost.add(sa);
        }
        MongoClient mongoClient = new MongoClient(listHost);
        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        // options.autoConnectRetry(true);// 自动重连true
        // options.maxAutoConnectRetryTime(10); // the maximum auto connect retry time
        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
        options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
        options.maxWaitTime(5000); //
        options.socketTimeout(0);// 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        options.writeConcern(WriteConcern.SAFE);//
        MongoClientOptions build = options.build();
        MongoCredential mongoCredential = MongoCredential.createCredential("root", "admin", "ASDzxc1993".toCharArray());
        return new MongoClient(listHost, mongoCredential, build);

    }
}
