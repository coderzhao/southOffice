package cn.anytec.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Component
public class WsMessStore {
    private static final Logger logger = Logger.getLogger(WsMessStore.class);

    private static volatile Map<String,WebSocketSession> sessionsMap = new HashMap<>();
    private static ArrayDeque<Object> messages = new ArrayDeque<>();
    private Thread pushMessageThread;
    private static WsMessStore wsMessStore = new WsMessStore();

    public static WsMessStore getInstance(){
        return wsMessStore;
    }

    public void addSession(WebSocketSession session){

        sessionsMap.put(session.getId(),session);
    }

    public void removeSession(WebSocketSession session){
        logger.debug("removeWebSocketSession");
        sessionsMap.remove(session.getId());
    }
    public void addMessage(Object data){

        synchronized (this){
            messages.add(data);
            logger.debug("添加一条数据成功，唤醒推送线程");
            this.notifyAll();
        }

    }
    public void startPushMessThread(){
        if(null!=pushMessageThread&&pushMessageThread.isAlive()){
            logger.debug("推送线程活跃中。。。");
            return;
        }
        pushMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    pushMessage();
                }
            }
        });

        pushMessageThread.start();

        logger.debug("推送线程启动，开始推送数据");
    }

    private void pushMessage(){

        try {
            synchronized (this) {
                while (messages.size() == 0) {
                    try {
                        logger.debug("没有数据推送，等待中。。。");
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Object object = messages.poll();
                if(object instanceof String){
                    String base64dData = (String) object;
                    logger.debug("数据长度："+base64dData.length());
                    for(String wssId : sessionsMap.keySet()){
                        WebSocketSession session = sessionsMap.get(wssId);
                        session.sendMessage(new TextMessage(base64dData));
                    }
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
