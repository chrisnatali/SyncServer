package com.syncserver.common;

import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;

/**
 * <b>ConfigManager</b> is the wrapper for apache's commons configuration framework.
 */
public class ConfigManager {
    private static Configuration config;

    static {
        String defaultConfigFile = System.getProperty("project.config.file");

        if ( defaultConfigFile == null || "".equals(defaultConfigFile.trim()) ) {
            defaultConfigFile = "project.xml";
        }

        ConfigurationFactory factory = new ConfigurationFactory();
        URL configURL = ConfigManager.class.getResource("/" + defaultConfigFile);
        factory.setConfigurationURL(configURL);

        try {
            config = factory.getConfiguration();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Configuration getConfiguration() {
        return config;
    }
}
