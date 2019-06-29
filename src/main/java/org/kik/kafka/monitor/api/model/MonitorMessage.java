package org.kik.kafka.monitor.api.model;

public class MonitorMessage {
	private long ts;
	private String topic;
	private String message;

	public MonitorMessage() {
		// Empty constructor
	}

	public MonitorMessage(long ts, String topic, String message) {
		this.ts = ts;
		this.topic = topic;
		this.message = message;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
