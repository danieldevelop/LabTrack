package com.labtrack.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesUtil {

    private static final String FILE_NAME = "application.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try(InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (in != null) {
                PROPERTIES.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            } else {
                throw new RuntimeException("Properties file not found: " + FILE_NAME);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + FILE_NAME, e);
        }
    }

    private PropertiesUtil() {
        // Private constructor to prevent instantiation
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
