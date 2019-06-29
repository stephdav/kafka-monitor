package org.kik.kafka.monitor.api;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;

import org.kik.kafka.monitor.api.service.Version;
import org.kik.kafka.monitor.api.utils.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private Version svcVersion;

    public ApiController() {
    }

    public void startApiController() {
        setupFilters();
        setupRoutes();
    }
    private void setupFilters() {
        LOG.info("[api] initialize filters");
        before("/api/*", (request, response) -> {
            StringBuilder sb = new StringBuilder();
            for (String param : request.queryParams()) {
                if (sb.length() == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(param).append("=").append(request.queryParams(param));
            }
            LOG.debug("{} {}{}", request.requestMethod(), request.uri(), sb);
        });
    }

    private void setupRoutes() {
        LOG.info("[api] initialize routes");

        path("/api", () -> {

            get("/version", (req, res) -> {
                return svcVersion.getAppInfo();
            } , new JsonTransformer());

        });
    }

}