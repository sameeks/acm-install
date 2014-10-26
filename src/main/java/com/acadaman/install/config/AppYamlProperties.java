package com.acadaman.install.config;

//import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

//@Component
//@ConfigurationProperties
public class AppYamlProperties {

    private List<Properties> spring = new ArrayList<Properties>();
    public List<Properties> getSpring() {
        return spring;
    }
    public void setSpring(List<Properties> spring) {
        this.spring = spring;
    }

    public class Properties {
    private String profile;
    //private List<Application> application = new ArrayList<Application>();
    //private App app;
    //private List<App> application = new ArrayList<App>();
    private String name;
    //private List<Datasource> datasource = new ArrayList<Datasource>();
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int dbPort;
    private String dbHost;
    //private List<Config> config = new ArrayList<Config>();
    private String schoolName;
    private String fqdn;
    private boolean initialized;
    private String platformVersion;
    private String awsBucketName;
    private String filesystemPath;

    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }

//    public List<Application> getApplication() {
//        return application;
//    }
//    public void setApplication(List<Application> application) {
//        this.application = application;
//    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
//    public List<Datasource> getDataSource() {
//        return datasource;
//    }
//    public void setDataSource(List<Datasource> datasource) {
//        this.datasource = datasource;
//    }
    public String getDriverClassName() {
        return driverClassName;
    }
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getDbPort() {
        return dbPort;
    }
    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }
    public String getDbHost() {
        return dbHost;
    }
    public void setDbHost(String dbHost){
        this.dbHost = dbHost;
    }

//    public List<Config> getConfig() {
//        return config;
//    }
//    public void setConfig(List<Config> config) {
//        this.config = config;
//    }
    public String getSchoolName() {
        return schoolName;
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public String getFqdn() {
        return fqdn;
    }
    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }
    public boolean getInitialized() {
        return initialized;
    }
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    public String getPlatformVersion() {
        return platformVersion;
    }
    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }
    public String getFilesystemPath() {
        return filesystemPath;
    }
    public void setFilesystemPath(String filesystemPath) {
        this.filesystemPath = filesystemPath;
    }

    public String getAwsBucketName() {
        return awsBucketName;
    }
    public void setAwsBucketName(String awsBucketName) {
        this.awsBucketName = awsBucketName;
    }

//    public List<App> getApps() {
//        return apps;
//    }
//    public void setApps(List<App> apps) {
//        this.apps = apps;
//    }
//    public App getApp() {
//        return app;
//    }
//    public void setApp(App app) {
//        this.app = app;
//    }
    }
    
    public class Application {
    
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
        
            this.name = name;
        }
    }

    public class Datasource {
    
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private int dbPort;
        private String dbHost;

        public String getDriverClassName() {
            return driverClassName;
        }
        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        public int getDbPort() {
            return dbPort;
        }
        public void setDbPort(int dbPort) {
            this.dbPort = dbPort;
        }

        public String getDbHost() {
            return dbHost;
        }
        public void setDbHost(String dbHost) {
            this.dbHost = dbHost;
        }

    }

    public class Config {
   
        private String schoolName;
        private String fqdn;
        private boolean initialized;
        private String platformVersion;
        private String filesystemPath;
        private String awsBucketName;

        public String getSchoolName() {
            return schoolName;
        }
        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getFqdn() {
            return fqdn;
        }
        public void setFqdn(String fqdn) {
            this.fqdn = fqdn;
        }

        public boolean getInitialized() {
            return initialized;
        }
        public void setInitialized(boolean initialized) {
            this.initialized = initialized;
        }

        public String getPlatformVersion() {
            return platformVersion;
        }
        public void setPlatformVersion(String platformVersion) {
            this.platformVersion = platformVersion;
        }

        public String getFilesystemPath() {
            return filesystemPath;
        }
        public void setFilesystemPath(String filesystemPath) {
            this.filesystemPath = filesystemPath;
        }

        public String getAwsBucketName() {
            return awsBucketName;
        }
        public void setAwsBucketName(String awsBucketName) {
            this.awsBucketName = awsBucketName;
        }
    }
}
