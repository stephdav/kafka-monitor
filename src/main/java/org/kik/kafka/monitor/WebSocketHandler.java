package org.kik.kafka.monitor;

import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class WebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    public WebSocketHandler() {
        schedule();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        LOGGER.info("onConnect from {}", session.getRemoteAddress());
        sessions.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        LOGGER.info("onClose from {}", session.getRemoteAddress());
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        // Ignore client messages
    }

    // Sends a message from one user to all users, along with a list of current
    // usernames
    public void broadcastMessage(String message) {
        sessions.stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(message);
            } catch (Exception e) {
                LOGGER.error("Failed to sed message to {}: {}", session.getRemoteAddress(), e.getMessage());
            }
        });
    }

    public void schedule() {
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        final Runnable maTache = new Runnable() {
            public void run() {
                broadcastMessage(new Date().toString());
            }
        };

        executor.scheduleAtFixedRate(maTache, 1, 5, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                executor.shutdown();
            }
        });
    }
}
