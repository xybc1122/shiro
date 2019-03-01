package com.dt.user.config;

import com.alibaba.fastjson.JSON;
import com.dt.user.model.Timing;
import com.dt.user.utils.CrrUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {
    //接收sid
    private Long uId = null;
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        this.uId = 1L;
        System.out.println("连接成功");
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 封装处理上传页面的百分比进度
     * @param intMap
     * @param currentCount
     * @param timSet
     * @param timing
     * @param uid
     */
    public void schedule(Map<String, Integer> intMap, int currentCount, ThreadLocal<Set<Timing>> timSet, Timing timing, Long uid) {
        if (intMap.size() == 0) {
            intMap.put("current", currentCount);
        }
        //如果值不一样 发送webSocket给前端
        if (intMap.get("current") != currentCount) {
            sendInfo(JSON.toJSONString(CrrUtils.inCreateSet(timSet, timing)), uid);
            intMap.put("current", currentCount);
        }
    }

    public void sendInfo(String message, Long uId) {
        for (WebSocketServer item : webSocketSet) {
            //这里可以设定只推送给这个sid的，为null则全部推送
            try {
                //这里可以设定只推送给这个uid的，为null则全部推送
                if (uId == null) {
                    item.sendMessage(message);
                } else if (item.uId.equals(uId)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        System.out.println("[WebSocket消息]连接断开，总数:{}" + webSocketSet.size());
    }

}
