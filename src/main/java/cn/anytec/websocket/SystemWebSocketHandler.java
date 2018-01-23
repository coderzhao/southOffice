package cn.anytec.websocket;


import cn.anytec.util.WsMessStore;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

@Component
public class SystemWebSocketHandler implements WebSocketHandler {


    private static final Logger logger = Logger.getLogger(SystemWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try{
            logger.debug("用户："+session.getId()+"连接Server成功");
            WsMessStore.getInstance().addSession(session);
                //session.sendMessage(new TextMessage("服务端链接成功！"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        logger.info("send message to keep connected");
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if(wss.isOpen()){
            wss.close();
        }
        WsMessStore.getInstance().removeSession(wss);
//        System.out.println("WebSocket出错！");
        logger.debug("ERROR! 用户："+wss.getId()+"从Server断开");



    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        WsMessStore.getInstance().removeSession(wss);
        logger.debug("用户："+wss.getId()+"从Server断开");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}