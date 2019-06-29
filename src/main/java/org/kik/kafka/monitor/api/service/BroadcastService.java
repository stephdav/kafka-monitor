package org.kik.kafka.monitor.api.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.eclipse.jetty.websocket.api.Session;
import org.kik.kafka.monitor.api.model.MonitorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class BroadcastService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastService.class);

	private static final Queue<Session> SESSIONS = new ConcurrentLinkedQueue<>();
	private static final Gson GSON = new Gson();

	private CircularFifoQueue<String> last10;

	public BroadcastService() {
		last10 = new CircularFifoQueue<>(10);
	}

	public void registerSession(Session session) {
		LOGGER.debug("register session for {}", session.getRemoteAddress());
		SESSIONS.add(session);
	}

	public void removeSession(Session session, int statusCode, String reason) {
		LOGGER.debug("remove session for {}", session.getRemoteAddress());
		SESSIONS.remove(session);
	}

	public void broadcastMessage(String topic, String message, long ts) {
		LOGGER.debug("broadcast message from {}: {}", topic, message);
		String mm = GSON.toJson(new MonitorMessage(ts, topic, message));
		last10.add(mm);
		broadcastMessage(mm);
	}

	public void reSendLast10(Session session) {
		LOGGER.info("resend last messages to '{}'", session.getRemoteAddress());
		for (String msg : last10) {
			sendMessage(session, msg);
		}
	}

	private void broadcastMessage(String message) {
		LOGGER.info("broadcast '{}'", message);
		SESSIONS.stream().filter(Session::isOpen).forEach(session -> {
			sendMessage(session, message);
		});
	}

	private void sendMessage(Session session, String message) {
		try {
			session.getRemote().sendString(message);
		} catch (Exception e) {
			LOGGER.error("Failed to send message to {}: {}", session.getRemoteAddress(), e.getMessage());
		}
	}

}
