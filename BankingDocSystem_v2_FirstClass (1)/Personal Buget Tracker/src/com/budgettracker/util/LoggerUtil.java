package com.budgettracker.util;

import java.util.logging.Logger;
import java.util.logging.Level;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    public static void logError(String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }

    public static void logInfo(String message) {
        logger.info(message);
    }
}
