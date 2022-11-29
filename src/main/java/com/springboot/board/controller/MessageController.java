package com.springboot.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Controller
@ServerEndpoint("/websocket")
public class MessageController extends Socket {
    private static final List<Session> session = new ArrayList<Session>();

    @GetMapping("/chat/main")
    public String index(){
        return "chat/main";
    }

    @OnOpen
    public void open(Session newUser){
        System.out.println("Connected!!!");
        session.add(newUser);
        System.out.println(newUser.getId());
    }

    @OnMessage
    public void getMsg(Session receiveSession, String msg){

        for(int i = 0; i < session.size(); i++){
            if(!receiveSession.getId().equals(session.get(i).getId())){
                try {
                    session.get(i).getBasicRemote().sendText("상대 : " + msg);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else{
                try {
                    session.get(i).getBasicRemote().sendText("나 : " + msg);
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

}











