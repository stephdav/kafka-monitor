package org.kik.kafka.monitor;

import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import org.kik.kafka.monitor.api.ApiController;
import org.kik.kafka.monitor.api.service.KafkaService;
import org.kik.kafka.monitor.api.service.WebSocketHandlerService;
import org.kik.kafka.monitor.ui.UiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.kik.kafka.monitor" })
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		LOGGER.info("Starting Kafka monitor...");

		int port = 4567;
		if (args != null && args.length == 1) {
			port = Integer.parseInt(args[0]);
			LOGGER.info("port {} will be used", port);
		}
		port(port);

		// Assign folder for static files
		staticFiles.location("/public");
		staticFiles.expireTime(3600);

		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

		// Setup Websocket
		webSocket("/chat", ctx.getBean(WebSocketHandlerService.class));

		// Setup Kafka listener
		ctx.getBean(KafkaService.class);

		// Setup REST
		ctx.getBean(ApiController.class).startApiController();

		// Setup Web Pages
		ctx.getBean(UiController.class).startUiController();

		ctx.registerShutdownHook();

		LOGGER.info("Kafka monitor started");
	}
}