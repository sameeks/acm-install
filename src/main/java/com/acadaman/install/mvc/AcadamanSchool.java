package com.acadaman.install.mvc;

public class AcadamanSchool {

    private String schoolName;
    private String schoolType;
    private String packageType;
    private String dbName;
    private String dbUser;
    private String dbPass;
    private String dbHost;
    private String adminUser; // for remote management. Drupal's uid 1
    private String adminPass;
    private String hostName; // fqdn (Site URI)

    // schoolName
    public String getSchoolName() {
        return schoolName;
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    // schoolType
    public String getSchoolType() {
        return schoolType;
    }
    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    // packageType
    public String getPackageType() {
        return packageType;
    }
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }
    // dbName;
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    //dbUser;
    public String getDbUser() {
        return dbUser;
    }
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }
    //dbPass;
    public String getDbPass() {
        return dbPass;
    }
    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }
    //dbHost;
    public String getDbHost() {
        return dbHost;
    }
    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }
    //adminUser; // for remote management. Drupal's uid 1
    public String getAdminUser() {
        return adminUser;
    }
    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }
    //adminPass;
    public String getAdminPass() {
        return adminPass;
    }
    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }
    
    //hostName; // fqdn (Site URI)
    public String getHostname() {
        return hostName;
    }
    public void setHostname(String hostName) {
        this.hostName = hostName;
    }
}
