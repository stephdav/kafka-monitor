package org.kik.kafka.monitor.api.service;

import org.kik.kafka.monitor.api.model.AppInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.properties")
public class Version {

    @Value("${application.name}")
    private String appName;

    @Value("${application.version}")
    private String appVersion;

    public Version() {
        // Empty constructor
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public AppInfo getAppInfo() {
        return new AppInfo(appName, appVersion);
    }
}
