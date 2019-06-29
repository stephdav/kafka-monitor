package org.kik.kafka.monitor.ui;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.redirect;

import java.util.HashMap;
import java.util.Map;

import org.kik.kafka.monitor.api.service.Version;
import org.kik.kafka.monitor.ui.utils.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import spark.ModelAndView;
import spark.Request;
import spark.template.pebble.PebbleTemplateEngine;

@Controller
public class UiController {

    private static final Logger LOG = LoggerFactory.getLogger(UiController.class);

    private static final String APP_VERSION = "appVersion";

    @Autowired
    private Version svcVersion;

    public UiController() {
    }

    public void startUiController() {
        setupFilters();
        setupRoutes();
    }
    private void setupFilters() {
        LOG.info("[ui] initialize filters");
        before("/ui/*", Filters.ADD_TRAILING_SLASHES);
        after("/ui/*", Filters.ADD_GZIP_HEADER);
    }

    private void setupRoutes() {
        LOG.info("[ui] initialize routes");

        // Redirect on index
        redirect.get("/", "/ui/");

        for (PageEnum page : PageEnum.values()) {
            get(page.getPath(), (request, response) -> {
                response.status(200);
                response.type("text/html");
                Map<String, Object> attributes = initAttributes(request);

                return new ModelAndView(attributes, page.getTemplate());
            } , new PebbleTemplateEngine());
        }

        exception(Exception.class, (exception, request, response) -> LOG.warn(exception.getMessage(), exception));
    }

    private Map<String, Object> initAttributes(final Request req) {

        Map<String, Object> attributes = new HashMap<>();

        // Application version
        attributes.put(APP_VERSION, tulipVersion());

        // href
        String url = req.url();
        int idx = url.indexOf("/ui/");
        if (idx != -1) {
            attributes.put("href", url.substring(0, idx));
        }

        return attributes;
    }

    private String tulipVersion() {
        return String.format("%s v%s", svcVersion.getAppName(), svcVersion.getAppVersion());
    }
}
