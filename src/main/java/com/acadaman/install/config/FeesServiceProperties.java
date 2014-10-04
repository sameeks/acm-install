package com.acadaman.install.config;

import java.util.List;

public class FeesServiceProperties {
 
    private String name;
    private String description;
    private String platform;
    private String repoUrl;
    private List<String> openShiftCatridge;
    private boolean scale;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getRepoUrl() {
        return repoUrl;
    }
    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public List<String> getOpenShiftCatridge() {
        return openShiftCatridge;
    }

    public void setOpenShiftCatridge(List<String> openShiftCatridge) {
        this.openShiftCatridge = openShiftCatridge;
    }
}
