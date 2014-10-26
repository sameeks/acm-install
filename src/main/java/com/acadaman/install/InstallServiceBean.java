package com.acadaman.install;

import org.springframework.stereotype.Service;

import com.acadaman.install.mvc.AcadamanSchool;
import com.acadaman.install.config.AppYamlProperties;

import java.util.*;

@Service
public class InstallServiceBean {

    private String serviceName;
    private String repositoryUrl;
    private String workingDirName; // dir to clone repo into.
    private String remoteConfigName;
    private List<String> openShiftCatridge;
    private String openShiftPlatform;
    private boolean openShiftAppScale;
    private AcadamanSchool acadamanSchool;
    private AppYamlProperties appYaml;

    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getWorkingDirName() {
        return workingDirName;
    }
    public void setWorkingDirName(String workingDirName) {
        this.workingDirName = workingDirName;
    }

    public String getRemoteConfigName() {
        return remoteConfigName;
    }
    public void setRemoteConfigName(String remoteConfigName) {
        this.remoteConfigName = remoteConfigName;
    }

    public List<String> getOpenShiftCatridge() {
        return openShiftCatridge;
    }
    public void setOpenShiftCatridge(List<String> openShiftCatridge) {
        this.openShiftCatridge = openShiftCatridge;
    }

    public String getOpenShiftPlatform() {
        return openShiftPlatform;
    }
    public void setOpenShiftPlatform(String openShiftPlatform) {
        this.openShiftPlatform = openShiftPlatform;
    }

    public boolean getOpenShiftAppScale() {
        return openShiftAppScale;
    }
    public void setOpenShiftAppScale(boolean openShiftAppScale) {
        this.openShiftAppScale = openShiftAppScale;
    }

    public AcadamanSchool getAcadamanSchool() {
        return acadamanSchool;
    }
    public void setAcadamanSchool(AcadamanSchool school) {
        this.acadamanSchool = acadamanSchool;
    }

    public AppYamlProperties getAppYaml() {
        return appYaml;
    }
    public void setAppYaml(AppYamlProperties appYaml) {
        this.appYaml = appYaml;
    }
}
