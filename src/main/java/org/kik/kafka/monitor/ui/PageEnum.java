package org.kik.kafka.monitor.ui;

public enum PageEnum {

    INDEX("/kafka-monitor/", "templates/base.peb");

    private String path;
    private String template;

    private PageEnum(String path, String template) {
        this.path = path;
        this.template = template;
    }

    public String getPath() {
        return path;
    }

    public String getTemplate() {
        return template;
    }

}
