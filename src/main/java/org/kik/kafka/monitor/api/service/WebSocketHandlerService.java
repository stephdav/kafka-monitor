package org.kik.kafka.monitor.api.service;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebSocket
@Component
public class WebSocketHandlerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandlerService.class);

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

	@OnWebSocketError
	public void onError(Session session, Throwable exc) {
		LOGGER.warn("error within session with {}: {}", session.getRemoteAddress(), exc.getMessage());
	}
}
