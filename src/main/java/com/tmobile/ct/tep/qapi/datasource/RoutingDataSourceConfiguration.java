package com.tmobile.ct.tep.qapi.datasource;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.tmobile.ct.tep.qapi.config.ApplicationProperties;
import com.tmobile.ct.tep.qapi.exceptions.ApiFailureException;
import com.tmobile.ct.tep.qapi.exceptions.DbConnectionException;
import com.tmobile.ct.tep.qapi.tvaultclient.TVaultAuthService;
import com.tmobile.ct.tep.qapi.tvaultclient.domain.AppRoleCredentials;
import com.tmobile.ct.tep.qapi.tvaultclient.domain.Secrets;
import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

@Configuration
public class RoutingDataSourceConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private TVaultAuthService tVaultAuthService;
    private static Logger logger = LoggerFactory.getLogger(RoutingDataSourceConfiguration.class);
    private HashMap<String,DataSource> dataSources = new HashMap<>();

    private static HashMap<String, Session> sessions = new HashMap<>();

    private HashMap<String,String> getSecrets(String env, String l1) throws ApiFailureException {
        tVaultAuthService.initConfig();
        AppRoleCredentials appRoleCredentials = new AppRoleCredentials(applicationProperties.getConfiguration().getRoleId(),
                applicationProperties.getConfiguration().getSecretId());
        String token = tVaultAuthService.auth(appRoleCredentials);
        String safepath = "shared/" +l1 +"/" +env;
        Secrets secrets = tVaultAuthService.getSecrets(token,safepath);
        return secrets.getData();
    }

    public Object getConnection(String env,String l1) throws ApiFailureException{
        String l1_env = l1 +"/" +env;
        if (dataSources.get(l1_env) != null){
            logger.info("DataSource already exists. Using existing source.");
            return new JdbcTemplate(dataSources.get(l1_env));
        } else if (sessions.get(l1_env) != null){
            logger.info("DataSource already exists (Cassandra). Using existing source.");
            return sessions.get(l1_env);
        }
        else{
            logger.info("before getting secrets");
            HashMap<String, String> received = getSecrets(env,l1);
            logger.info("received secrets from vault");
            String[] keys = received.keySet().toArray(new String[received.size()]);
            boolean isCassandra = false;
            boolean isOracle = false;
            boolean isSqlServer = false;
            for (int i = 0;i<keys.length;i++){
                if (keys[i].contains("cassandra")){
                    isCassandra = true;
                    break;
                } else if (keys[i].contains("oracle")){
                    isOracle = true;
                    break;
                }  else if (keys[i].contains("sqlserver")){
                    isSqlServer = true;
                    break;
                }
            }
            if (isCassandra){

                Cluster cluster = null;
                SocketOptions socketOptions = new SocketOptions().setReadTimeoutMillis(20000);
                cluster = Cluster.builder().addContactPoint(received.get("cassandra_host"))
                        .withCredentials(received.get("cassandra_user"),received.get("cassandra_password"))
                        .withSocketOptions(socketOptions).withLoadBalancingPolicy(new RoundRobinPolicy()).build();
                Session session = cluster.connect(received.get("cassandra_keyspace"));
                sessions.put(l1_env,session);
                return session;
            }
            else if (isOracle){
                try {
                    OracleDataSource dataSource = new OracleDataSource();
                    dataSource.setUser(received.get("oracle_user"));
                    dataSource.setPassword(received.get("oracle_password"));
                    dataSource.setURL("jdbc:" +received.get("oracle_url"));
                    dataSource.setFastConnectionFailoverEnabled(true);
                    dataSources.put(l1_env,dataSource);
                    return new JdbcTemplate(dataSources.get(l1_env));
                } catch (SQLException e) {
                    logger.error("Oracle SQL Exception: [{}]",e.getMessage());
                    throw new DbConnectionException("Failed to connect to database specified. Verify connection configuration is correct on T-Vault.");
                }
            } else if (isSqlServer){
                SQLServerDataSource dataSource = new SQLServerDataSource();
                dataSource.setUser(received.get("sqlserver_user"));
                dataSource.setPassword(received.get("sqlserver_password"));
                dataSource.setURL("jdbc:" +received.get("sqlserver_url"));
               dataSources.put(l1_env,dataSource);
               return new JdbcTemplate(dataSources.get(l1_env));

            }
            return null;
        }
    }

    public Object reconnect(String env,String l1){
        String l1_env = l1 +"/" +env;
        remove(l1_env);
        return getConnection(env,l1);
    }

    public void remove(String l1_env){
        if (dataSources.get(l1_env) != null){
            try {
                dataSources.get(l1_env).getConnection().close();
            } catch (SQLException e) {
                logger.error("SQL Exception thrown: [{}] ", e.getMessage());
            }
            dataSources.remove(l1_env);
        }
        else if (sessions.get(l1_env) != null){
            sessions.remove(l1_env);
        }
    }
}
