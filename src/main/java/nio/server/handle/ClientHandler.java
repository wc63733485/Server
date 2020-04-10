package nio.server.handle;

import nio.Entity.DeviceEntity;
import nio.Entity.DeviceUnitEntity;
import nio.clink.utils.CloseUtils;
import com.alibaba.fastjson.JSONObject;
import com.sws.base.dao.BaseDao;
import com.sws.base.dao.MongoDao;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private final MongoDao md = new MongoDao();
    private final BaseDao baseDao = new BaseDao();
    private final SocketChannel socketChannel;
    private final ClientReadHandler readHandler;
    private final ClientWriteHandler writeHandler;
    private final ClientHandlerCallback clientHandlerCallback;
    private final String clientInfo;

    public ClientHandler(SocketChannel socketChannel, ClientHandlerCallback clientHandlerCallback) throws IOException {
        this.socketChannel = socketChannel;
        // 设置非阻塞模式
        socketChannel.configureBlocking(false);

        Selector readSelector = Selector.open();
        socketChannel.register(readSelector, SelectionKey.OP_READ);
        this.readHandler = new ClientReadHandler(readSelector);

        Selector writeSelector = Selector.open();
        socketChannel.register(writeSelector, SelectionKey.OP_WRITE);
        this.writeHandler = new ClientWriteHandler(writeSelector);


        this.clientHandlerCallback = clientHandlerCallback;
        this.clientInfo = socketChannel.getRemoteAddress().toString();
        System.out.println(Calendar.getInstance().getTime().toLocaleString()+"新客户端连接：" + clientInfo);
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void exit() {
        readHandler.exit();
        writeHandler.exit();
        CloseUtils.close(socketChannel);
        System.out.println("客户端已退出：" + clientInfo);
    }

    public void send(String str) {
        writeHandler.send(str);
    }

    public void readToPrint() {
        readHandler.start();
    }

    private void exitBySelf() {
        exit();
        clientHandlerCallback.onSelfClosed(this);
    }

    public interface ClientHandlerCallback {
        // 自身关闭通知
        void onSelfClosed(ClientHandler handler);

        // 收到消息时通知
//        void onNewMessageArrived(ClientHandler handler, String msg);
    }

    class ClientReadHandler extends Thread {
        private boolean done = false;
        private final Selector selector;
        private final ByteBuffer byteBuffer;

        ClientReadHandler(Selector selector) {
            this.selector = selector;
            this.byteBuffer = ByteBuffer.allocate(255);
        }

        @Override
        public void run() {
            super.run();
            String ret = "";
            JSONObject jsonObject;
            HashMap map = null;
            try {
                do {
                    // 客户端拿到一条数据
                    if (selector.select() == 0) {
                        if (done) {
                            break;
                        }
                        continue;
                    }

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        if (done) {
                            break;
                        }

                        SelectionKey key = iterator.next();
                        iterator.remove();

                        System.out.println("+++"+clientInfo);
                        if (key.isReadable()) {

                            SocketChannel client = (SocketChannel) key.channel();
                            // 清空操作
                            byteBuffer.clear();
                            // 读取
                            int read = client.read(byteBuffer);
                            if (read > 0) {
                                byte[] array = this.byteBuffer.array();

                                if (map == null) {
                                    System.out.println("数据转换加载");
                                    map = new HashMap();
                                    DeviceUnitEntity due = null;
                                    DeviceEntity de = new DeviceEntity();
                                    byte[] bytes = Arrays.copyOfRange(array, 0, 4);
                                    for (int i = 0; i < bytes.length; i++) {
                                        String hex = Integer.toHexString(bytes[i] & 0xFF);
                                        if (hex.length() == 1) {
                                            hex = '0' + hex;
                                        }
                                        ret += hex.toUpperCase();
                                    }
                                    de.setCode(ret);
                                    DeviceEntity deviceEntity = baseDao.queryOne(de, DeviceEntity.class);
                                    if (deviceEntity != null) {
                                        //以下代码代表设备类型字段会覆盖掉型号和版本的数据字段
                                        due = new DeviceUnitEntity();
                                        due.setDeviceTypeId(deviceEntity.getDeviceTypeId());
                                        List<DeviceUnitEntity> deviceUnitEntities1 = baseDao.queryByCondition(due, DeviceUnitEntity.class, false);
                                        due = new DeviceUnitEntity();
                                        due.setDeviceModelId(deviceEntity.getDeviceModelId());
                                        List<DeviceUnitEntity> deviceUnitEntities2 = baseDao.queryByCondition(due, DeviceUnitEntity.class, false);
                                        due = new DeviceUnitEntity();
                                        due.setDeviceEditionId(deviceEntity.getDeviceEditionId());
                                        List<DeviceUnitEntity> deviceUnitEntities3 = baseDao.queryByCondition(due, DeviceUnitEntity.class, false);
                                        for (DeviceUnitEntity deviceUnitEntity : deviceUnitEntities1) {
                                            if (deviceUnitEntity.getMessageNumber().contains("/")) {
                                                String[] split = deviceUnitEntity.getMessageNumber().split("/");
                                            }
                                            map.put(deviceUnitEntity.getMessageNumber(), deviceUnitEntity.getDataName());
                                        }
                                        for (DeviceUnitEntity deviceUnitEntity : deviceUnitEntities2) {
                                            map.put(deviceUnitEntity.getMessageNumber(), deviceUnitEntity.getDataName());
                                        }
                                        for (DeviceUnitEntity deviceUnitEntity : deviceUnitEntities3) {
                                            map.put(deviceUnitEntity.getMessageNumber(), deviceUnitEntity.getDataName());
                                        }
                                    }
                                }

//                                for (int i = 0; i < array.length; i++) {
//                                    String hex = Integer.toHexString(array[i] & 0xFF);
//                                    if (hex.length() == 1) {
//                                        hex = '0' + hex;
//                                    }
//                                    System.out.print(i + ":" + hex + " ");
//                                }
//                                System.out.println(" ");

                                if (checkHead(array)) {
                                    jsonObject = new JSONObject(true);
                                    jsonObject.put("time",Calendar.getInstance().getTimeInMillis());
                                    Set<Map.Entry<String, String>> entrys = map.entrySet();  //此行可省略，直接将map.entrySet()写在for-each循环的条件中
                                    for (Map.Entry<String, String> entry : entrys) {
                                        if (entry.getKey().contains("/")) {
                                            String[] split = entry.getKey().split("/");
                                            int r = 0;
                                            for (int i = 0; i < split.length; i++) {
                                                r += Integer.parseInt(Integer.toHexString(array[Integer.parseInt(split[i])] & 0xFF), 16) * (Math.pow(256, split.length - 1 - i));
                                            }
                                            jsonObject.put(entry.getValue(), r);
                                        } else {
                                            jsonObject.put(entry.getValue(), Integer.parseInt(Integer.toHexString(array[Integer.parseInt(entry.getKey())] & 0xFF), 16));
                                        }
                                    }
                                    md.insert(jsonObject, ret);
                                }


                                // 通知到TCPServer
//                                clientHandlerCallback.onNewMessageArrived(ClientHandler.this, str);
                            } else {
                                System.out.println("客户端已无法读取数据！");
                                // 退出当前客户端
                                ClientHandler.this.exitBySelf();
                                break;
                            }
                        }
                    }
                } while (!done);
            } catch (Exception e) {
                if (!done) {
                    e.printStackTrace();
                    System.out.println("连接异常断开");
                    ClientHandler.this.exitBySelf();
                }
            } finally {
                // 连接关闭
                CloseUtils.close(selector);
            }
        }

        void exit() {
            done = true;
            selector.wakeup();
            CloseUtils.close(selector);
        }
    }


//    protected String byteToJSONObject(byte[] b) {
//        String ret = "";
//        for (int i = 0; i < b.length; i++) {
//            String hex = Integer.toHexString(b[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            ret += hex.toUpperCase();
//        }
//        return ret;
//    }


    protected boolean checkHead(byte[] b) {
        byte[] array = Arrays.copyOfRange(b, 0, 4);
        byte[] array1 = new byte[]{5, 3, -56, 0};//0503C800
        return Arrays.equals(array, array1);
    }


    class ClientWriteHandler {
        private boolean done = false;
        private final Selector selector;
        private final ByteBuffer byteBuffer;
        private final ExecutorService executorService;

        ClientWriteHandler(Selector selector) {
            this.selector = selector;
            this.byteBuffer = ByteBuffer.allocate(512);
            this.executorService = Executors.newSingleThreadExecutor();
        }

        void exit() {
            done = true;
            CloseUtils.close(selector);
            executorService.shutdownNow();
        }

        void send(String str) {
            if (done) {
                return;
            }
            executorService.execute(new WriteRunnable(str));
        }

        class WriteRunnable implements Runnable {
            private final String msg;

            WriteRunnable(String msg) {
                this.msg = msg + '\n';
            }

            @Override
            public void run() {
                if (ClientWriteHandler.this.done) {
                    return;
                }

                byteBuffer.clear();
                byteBuffer.put(msg.getBytes());
                // 反转操作, 重点
                byteBuffer.flip();

                while (!done && byteBuffer.hasRemaining()) {
                    try {
                        int len = socketChannel.write(byteBuffer);
                        // len = 0 合法
                        if (len < 0) {
                            System.out.println("客户端已无法发送数据！");
                            ClientHandler.this.exitBySelf();
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
