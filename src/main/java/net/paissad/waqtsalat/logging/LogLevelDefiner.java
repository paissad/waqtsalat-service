package net.paissad.waqtsalat.logging;

import ch.qos.logback.core.PropertyDefinerBase;

public class LogLevelDefiner extends PropertyDefinerBase {

    private static String logLevel;

    @Override
    public String getPropertyValue() {
        return (logLevel != null) ? logLevel : "INFO";
    }

    /**
     * @param logLevel - The level's log (INFO / DEBUG / TRACE / WARN / ERROR)
     */
    public static synchronized void setLogLevel(String logLevel) {
        LogLevelDefiner.logLevel = logLevel;
    }
}