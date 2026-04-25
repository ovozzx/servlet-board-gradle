package com.board.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();
    static {
        try (InputStream is = AppConfig.class.getResourceAsStream("/application.properties")) {
            props.load(is);
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    public static String get(String key) { return props.getProperty(key); }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) return defaultValue;
        return Integer.parseInt(v.trim());
    }
}
