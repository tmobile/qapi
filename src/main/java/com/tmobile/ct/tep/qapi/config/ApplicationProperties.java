package com.tmobile.ct.tep.qapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties specific to Q API.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final SecretManagement secretManagement = new SecretManagement();
    private final Tvault tvault = new Tvault();
    private final QueryConfig queryConfig = new QueryConfig();

    public SecretManagement getSecretManagement() {
        return secretManagement;
    }

    public Tvault getTvault() {
        return tvault;
    }

    public QueryConfig getQueryConfig(){
        return this.queryConfig;
    }

    public static class SecretManagement {
        private String encryptionPassword;
        private Boolean tvaultEnabled;

        public String getEncryptionPassword() {
            return encryptionPassword;
        }

        public void setEncryptionPassword(String encryptionPassword) {
            this.encryptionPassword = encryptionPassword;
        }

        public Boolean getTvaultEnabled() {
            return tvaultEnabled;
        }

        public void setTvaultEnabled(Boolean tvaultEnabled) {
            this.tvaultEnabled = tvaultEnabled;
        }
    }

    public static class Tvault {
        private String roleId;
        private String secretId;
        private String tvaultHost;

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getSecretId() {
            return secretId;
        }

        public void setSecretId(String secretId) {
            this.secretId = secretId;
        }

        public String getTvaultHost() {
            return tvaultHost;
        }

        public void setTvaultHost(String tvaultHost) {
            this.tvaultHost = tvaultHost;
        }
    }

    public static class QueryConfig {

        private String location;
        private String repository;
        private String repoRoot;
        private String branch;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRepository() {
            return repository;
        }

        public void setRepository(String repository) {
            this.repository = repository;
        }

        public String getRepoRoot() {
            return repoRoot;
        }

        public void setRepoRoot(String repoRoot) {
            this.repoRoot = repoRoot;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }
    }
}
