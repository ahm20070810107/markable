package com.hitales.markable.WebSocketInterceptor;


import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @desp websocket拦截器
 * @author huangming
 *
 */

public class WebSocketInterceptor implements HandshakeInterceptor{

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler handler, Exception exception) {

    }

    /**
     * @desp 将HttpSession中对象放入WebSocketSession中
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler handler,
                                   Map<String, Object> map){
        if(request instanceof ServletServerHttpRequest){
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpSession session = servletRequest.getServletRequest().getSession();
//            String path= servletRequest.getURI().getQuery();
//            if(path == null){
//                path="123";
//            }
//            if(session!=null){
//                //区分socket连接以定向发送消息
//                //     session.setAttribute("user",path);
//                map.put("user", path);
//            }

        }
        return true;
    }

}
