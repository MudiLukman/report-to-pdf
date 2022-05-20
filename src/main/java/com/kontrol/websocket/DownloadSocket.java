package com.kontrol.websocket;

import com.kontrol.websocket.codecs.FileDownloadEncoder;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/download", encoders = FileDownloadEncoder.class)
public class DownloadSocket {

    private static Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    public static void send(FileDownload fileDownload) {
        sessions.forEach(session -> session.getAsyncRemote().sendObject(fileDownload));
    }

}
