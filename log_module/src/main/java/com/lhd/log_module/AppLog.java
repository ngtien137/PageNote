package com.lhd.log_module;

import java.util.ArrayList;

public class AppLog {

    private static final ArrayList<LogEngine> logEngines = new ArrayList<>();

    public static void addLogEngine(LogEngine logEngine) {
        logEngines.add(logEngine);
    }

    public static void clearLogEngines() {
        logEngines.clear();
    }


    public static void initialize() {
        if (!logEngines.isEmpty())
            for (int i = 0; i < logEngines.size(); i++)
                logEngines.get(i).initialize();
    }

    public static void log(String message) {
        if (!logEngines.isEmpty())
            for (int i = 0; i < logEngines.size(); i++)
                logEngines.get(i).log(message);
    }
}
