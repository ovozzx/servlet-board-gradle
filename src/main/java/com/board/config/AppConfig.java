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
}
