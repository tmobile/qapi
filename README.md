# qAPI - Translating database queries into API calls

qAPI is a JHipster project created by T-Mobile. qAPI allows users to transfer database connections and queries from their tests to an API service, which testers can integrate to retrieve and validate data from a database through API calls.

## Getting Started

Below are instructions that will help you get started in getting the qAPI up and running.

### Prerequisites

| Prerequisite | Command to verify | Description |
| ------------ | -------------- | -----------  |
| Java Runtime Environment (JRE) 1.8.x | java -version | As it is a Java project, Java need to be installed locally.
| Maven | mvn --version | Maven is needed to download project dependencies.
| T-Vault | N/A | The project uses T-mobiles open sourced secret management tool T-Vault, for maintaining all database credentials. Please refer to https://github.com/tmobile/t-vault for more information on T-Vault.
| Oracle JDBC driver | N/A | The oracle JDBC driver needs to be retrieved from the Oracle website, and once accepting the license terms, the jar can be saved on your local machine/artifactory. Oracle website: https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html Instructions to have pom dependency working: https://www.mkyong.com/maven/how-to-add-oracle-jdbc-driver-in-your-maven-local-repository/

### Configuration

Below are configurations that need to be set before starting up qAPI.

#### File: resources\config\application.yml

At the bottom of the file, you will see a section for application -> configuration
 
Here, you will configure the following values:
- roleId: Tvault parameter to authenticate via approle
- secretId: TVault parameter to authenticate via approle
- tvaultHost: Host url to your t-vault instance. Example: https://myt-vault.myorg.com
- repoLocation: Repository where the queries are configured
- repoRoot: Root folder/folders to where the config files live in the repository
- branch: The branch of the config repository to be used

Note, that using T-vault is not mandatory, but is the default way of managing secrets in qAPI. If you choose to implement another secret management tool, changes will have to be made in the RoutingDataSourceConfiguration.java class.

Additionally, in the repository configured under 'repoLocation', you must have at least one config file.
For more instructions on how to write a config file, follow this wiki step: 

#### Files: resources\config\application.dev.yml, resources\config\application-prod.yml

Here, you will configure your eureka service url under eureka -> client -> defaultzone 

You can also configure your secret token that will be registered on your jhipster registry under jhipster -> security -> base64-secret

#### Files: resources\config\bootstrap.yml, resources\config\bootstrap-prod.yml

Here, you will configure the jhipster registry password, as well as the cloud config uri.

#### File: resources\trust.jks

If you need a java key store file to connect to your t-vault instance, place the keystore file here, with the name "trust.jks"

Once all these are configured, you can start running qAPI locally.



