/*
 * // =========================================================================
 * // Copyright © 2019 T-Mobile USA, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * //    http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * // =========================================================================
 *
 */

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
