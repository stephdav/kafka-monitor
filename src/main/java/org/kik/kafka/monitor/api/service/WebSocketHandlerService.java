package org.kik.kafka.monitor.api.service;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebSocket
@Component
public class WebSocketHandlerService {
	@Autowired
	private BroadcastService broadcast;

	public WebSocketHandlerService() {
		// Empty constructor
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		broadcast.registerSession(session);
		broadcast.reSendLast10(session);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		broadcast.removeSession(session, statusCode, reason);
	}
}
