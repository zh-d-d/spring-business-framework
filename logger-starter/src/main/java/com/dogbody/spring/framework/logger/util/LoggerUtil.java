package com.dogbody.spring.framework.logger.util;

import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

/**
 * @author zhangdd on 2024/4/2
 */
public class LoggerUtil {
    private static LoggingSystem loggingSystem;

    public static void init(LoggingSystem loggingSystem) {
        LoggerUtil.loggingSystem = loggingSystem;
    }


    public static void updateLoggerLevel(String loggerName, LogLevel logLevel) {
        loggingSystem.setLogLevel(loggerName, logLevel);
    }

//    public static List<Map<String, String>> getLoggerLevel(String loggerName){
//        // 获取到 LoggerContext
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//        // 获取系统中定义的所有 logger
//        List<Map<String, String>> loggers = loggerContext.getLoggerList().stream().map(logger -> {
//            // 映射为 Map，key 是 logger 名称，value 是其日志级别
//            // logger名称 = logger有效级别
//            return Collections.singletonMap(logger.getName(), logger.getEffectiveLevel().levelStr);
//        }).collect(Collectors.toList());
//    }
}
