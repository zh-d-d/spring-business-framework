package com.dogbody.spring.framework.logger;


import org.springframework.boot.logging.LoggingSystem;

public class LoggerAutoConfiguration {

    private final LoggingSystem loggingSystem;

    public LoggerAutoConfiguration(LoggingSystem loggingSystem) {
        this.loggingSystem = loggingSystem;
    }
}
