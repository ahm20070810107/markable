package com.hitales.markable.SocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hitales.markable.model.FileData;
import com.hitales.markable.util.CommonTools;
import com.hitales.markable.util.ExcelTools;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @desp Socket处理类
 * @author huangming
 *
 */
@Service
@Slf4j
public class SocketHandler implements WebSocketHandler{

//    private static final ArrayList<WebSocketSession> users;
//
//    static{
//        users = new ArrayList<>();
//    }
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("成功建立socket连接");
//        users.add(session);
//        String username = session.getAttributes().get("user").toString();
//        if(username!=null){
//            session.sendMessage(new TextMessage("我们已经成功建立socket通信了"));
//        }

    }

    @Override
    public void handleMessage(WebSocketSession arg0, WebSocketMessage<?> arg1) {
        // TODO Auto-generated method stub
        //log.info(arg0.getAttributes().get("user")+"的message："+arg1.getPayload());
        Document document ;
        try {
            document = (Document) arg1.getPayload();
            if (document != null) {
                FileData fileData = new FileData();
                fileData.setId(CommonTools.getJSonValue(document, "id"));
                fileData.setFileName(CommonTools.getJSonValue(document, "fileName"));
                document.remove("id");
                document.remove("fileName");
                Map<String, Object> mapValues = new HashMap<>();
                for (Document.Entry<String, Object> doc : document.entrySet()) {
                    mapValues.put(doc.getKey(), doc.getValue());
                }
                fileData.setDatas(mapValues);
                mongoTemplate.save(fileData, "fileData");
            }
        }catch (Exception e){
            log.info(arg1.getPayload().toString());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable error)
            throws Exception {
        if(session.isOpen()){
            session.close();
        }
        log.error("连接出现错误:"+error.toString());
//        users.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) {
        log.info("连接已关闭");
//        users.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

//    /**
//     * 给所有在线用户发送消息
//     *
//     * @param message
//     */
//    public void sendMessageToUsers(TextMessage message) {
//        for (WebSocketSession user : users) {
//            try {
//                if (user.isOpen()) {
//                    user.sendMessage(message);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    /**
//     * 给某个用户发送消息
//     *
//     * @param userName
//     * @param message
//     */
//    public void sendMessageToUser(String userName, TextMessage message) {
//        for (WebSocketSession user : users) {
//            if (user.getAttributes().get("user").equals(userName)) {
//                try {
//                    if (user.isOpen()) {
//                        user.sendMessage(message);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//        }
//    }

}