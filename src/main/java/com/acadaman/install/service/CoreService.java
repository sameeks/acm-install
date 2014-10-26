package com.acadaman.install.service;

import com.acadaman.install.mvc.*;
import com.acadaman.install.InstallServiceBean;
import com.acadaman.install.ServiceHelper;
import com.acadaman.install.config.*;
import com.acadaman.install.mvc.AcadamanSchool;

import java.util.*;
import java.util.concurrent.*;

public class CoreService implements Callable<String> {

    private String name;
    private String description;
    private static final String CORE_PLATFORM = "java";
    private static final String CORE_GIT_REMOTE_URL = "https://github.com/charyorde/acadaman-parent.git";
    private static final List<String> CORE_CATRIDGE = Arrays.asList("jbossews-2.0");
    private static final boolean CORE_SCALE = false;
    
    //@Autowired
    private AcadamanSchool acadamanSchool;
    private InstallServiceBean serviceBean;
    private ServiceHelper serviceHelper;

    public CoreService(AcadamanSchool acadamanSchool) {
        this.acadamanSchool = acadamanSchool;
    }

    public String call() {
        return execute();
    }

    private void generateAwsBucket() {
    
    }

    public String execute() {
        // generateAwsBucket
        return generateAppConfigAndCommit();
    }

    private String generateAppConfigAndCommit() {
        serviceBean.setServiceName(acadamanSchool.getSchoolName() + "core");
        serviceBean.setWorkingDirName("CoreServiceGitRepository");
        serviceBean.setRemoteConfigName("core-service-git-config");
        serviceBean.setRepositoryUrl(CORE_GIT_REMOTE_URL);
        serviceBean.setOpenShiftCatridge(CORE_CATRIDGE);
        serviceBean.setOpenShiftAppScale(CORE_SCALE);
        serviceBean.setOpenShiftPlatform(CORE_PLATFORM);
        serviceBean.setAcadamanSchool(acadamanSchool);
        
        AppYamlProperties appYaml = populateAppYaml();
        serviceBean.setAppYaml(appYaml);
        return serviceHelper.generateAppConfigAndCommit(serviceBean);
    }
    /**
     * Deploy app to OpenShift
     */ 
    private String deployToOpenShift() {
        return "{status: deployed}";
    }

    public String deploy() {
        return deployToOpenShift();
    }

    private AppYamlProperties populateAppYaml() {
        AppYamlProperties spring = new AppYamlProperties();
        List<AppYamlProperties.Properties> properties = new ArrayList<AppYamlProperties.Properties>();
        AppYamlProperties.Properties property = spring.new Properties();
        property.setProfile("internal");
        property.setSchoolName(acadamanSchool.getSchoolName());
        property.setName(property.getSchoolName() + "core");
        property.setDriverClassName("com.mysql.jdbc.Driver");
        property.setUrl("jdbc:mysql://10.0.0.4/eduerp");
        property.setUsername(acadamanSchool.getDbUser());
        property.setPassword(acadamanSchool.getDbPass());
        property.setDbPort(0); // @todo Get from posted value and convert to int.
        property.setDbHost(acadamanSchool.getDbHost());
        property.setFqdn(acadamanSchool.getHostname());
        property.setInitialized(false);
        property.setPlatformVersion(CORE_PLATFORM);
        property.setAwsBucketName("");
        property.setFilesystemPath("");

        properties.add(property);
        spring.setSpring(null);
        return spring;
    }
}
