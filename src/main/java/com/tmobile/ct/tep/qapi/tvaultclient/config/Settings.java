package com.tmobile.ct.tep.qapi.tvaultclient.config;

import com.tmobile.ct.tep.qapi.tvaultclient.TVaultAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    private static final Logger log = LoggerFactory.getLogger(Settings.class);
    private Properties properties = null;
    public Properties getProperties() {
        if (properties==null) {
            properties = new Properties();
            InputStream stream = TVaultAuthService.class.getClassLoader().getResourceAsStream("tvault-config.properties");
            try {
                properties.load(stream);
            } catch (IOException ioex) {
                log.error(ioex.getMessage());
            }
        }

        return properties;
    }
}
