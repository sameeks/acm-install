package com.acadaman.install;

import org.springframework.stereotype.Service;

import com.acadaman.install.InstallServiceBean;
import com.acadaman.install.OpenShiftServiceInstall;
import com.acadaman.install.config.AppYamlProperties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FileUtils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.RefSpec;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

@Service
public class ServiceHelper {

    private static final Logger log = LogManager.getLogger(ServiceHelper.class);
    //private InstallServiceBean serviceBean;

    public String generateAppConfigAndCommit(InstallServiceBean serviceBean) {
        // clone the git repo into $OPENSHIFT_DATA_DIR
        Repository repository = null;

        try {
            File localPath = File.createTempFile(serviceBean.getWorkingDirName(), "");
            localPath.delete();

            log.info("Cloning from FEES_GIT_REMOTE_URL to " + localPath);
            Git.cloneRepository()
                .setURI(serviceBean.getRepositoryUrl())
                .setDirectory(localPath)
                .call();

            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            repository = builder.setGitDir(localPath)
                .readEnvironment()
                .findGitDir()
                .build();
        }
        catch (IOException io) {
        
        }
        catch (InvalidRemoteException ire) {
        
        }
        catch (TransportException te) {
        
        }
        catch (GitAPIException gae) {
        
        }
        
        // @todo init OpenSshConfig
//        File home = new File(repository.getWorkTree(), "home");
//        FileUtils.mkdir(home);

//        File configFile = new File(new File(home, ".ssh"), Constants.CONFIG);
//        FileUtils.mkdir(configFile.getParentFile());

//        System.setProperty("user.name", "");
        // modify config before initializing openssh.
        // File configFile = repository.getConfig().getFile();
//        OpenSshConfig osc = OpenSshConfig.get(repository.getFS());

        // write ssh config into configFile
//        OutputStreamWriter fw = null;
//        String config = "";
//        try {
//            fw = new OutputStreamWriter(new FileOutputStream(config), "UTF-8");
//            fw.write(config);
//        }
//        catch (IOException e) {
//        
//        }
//        finally {
//            fw.close();
//        }

        // init RemoteConfig. synonymous to settings at .ssh/config
        // @todo Make sure the public key of the server acm-install is installed on
        // is copied into git server (e.g github)
        try {
            final StoredConfig config = repository.getConfig();
            RemoteConfig remoteConfig = new RemoteConfig(config, serviceBean.getRemoteConfigName());
            URIish uri= new URIish(repository.getDirectory().toURI().toURL());
            remoteConfig.addURI(uri);
            remoteConfig.update(config);
            config.save();
        }
        catch (Exception e) { // URISyntaxException
        
        }
        

        // populateServiceParams
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        // populate params for openshift install for fees service.
//        FeesServiceProperties feesService = new FeesServiceProperties();
//        feesService.setName(params.getOrDefault("name", ""));
//        feesService.setPlatform("java");
//        feesService.setRepoUrl("https:github.com/charyorde/fees");
//        feesService.setOpenShiftCatridge(Arrays.asList(new String[]{"diy", "mysql"}));
           
//        AppYamlProperties spring = new AppYamlProperties();
//        List<AppYamlProperties.Properties> properties = new ArrayList<AppYamlProperties.Properties>();
//        AppYamlProperties.Properties property = spring.new Properties();
//        property.setProfile("internal");
//        property.setSchoolName(serviceBean.getAcadamanSchool().getSchoolName());
//        property.setName(property.getSchoolName() + "fees");
//        property.setDriverClassName("com.mysql.jdbc.Driver");
//        property.setUrl("jdbc:mysql://10.0.0.4/eduerp");
//        property.setUsername(params.getOrDefault("root", "root"));
//        property.setPassword(params.getOrDefault("root", "root"));
//        property.setDbPort(0); // @todo Get from posted value and convert to int.
//        property.setDbHost(params.getOrDefault("root", ""));
//        property.setFqdn(params.getOrDefault("fqdn", ""));
//        property.setInitialized(false);
//        property.setPlatformVersion("");
//        property.setAwsBucketName("");
//        property.setFilesystemPath("");
//
//        properties.add(property);
//        spring.setSpring(null);

        AppYamlProperties spring = serviceBean.getAppYaml();

        // dumpYaml
        Yaml yaml = new Yaml(options);
        String resourcesPath = repository.getDirectory().getAbsolutePath();
        File yamlFile = new File(resourcesPath + File.separator, "application.yml");
        FileWriter writer = null;
        BufferedWriter out = null;
        try {
            writer = new FileWriter(yamlFile);
            String yamlStr = yaml.dumpAs(spring, null, null);
            out = new BufferedWriter(writer);
            out.write(yamlStr);
            out.close();
        }
        catch (IOException e) {
            log.info("Couldn't write yaml: " + e.getMessage());
        }
        
        // commitApplicationYaml
        try {
            Git git = new Git(repository);
            git.add()
                .addFilepattern("application.yml")
                .call();
            log.info("Added file " + yamlFile + " to repository at " + repository.getDirectory());
            // commit the changes.
            git.commit()
                .setMessage("Added application.yml")
                .call();
            log.info("Committed file " + yamlFile + " to repository at " + repository.getDirectory());
            
            // push to remote.
            RefSpec spec = new RefSpec("refs/heads/master");
            git.push().setRemote("fees-git-repo").setRefSpecs(spec)
                .call();
        }
        catch (Exception e) { // GitAPIException
        }
        // if committed successfully, remove the git repo from $OPENSHIFTDATADIR
        
        // close the repo
        repository.close();
    
        // generateAwsBucket
        // return deployToOpenShift();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", serviceBean.getServiceName());
        map.put("initial_git_url", serviceBean.getRepositoryUrl());
        map.put("catridge", StringUtils.join(serviceBean.getOpenShiftCatridge()));
        OpenShiftServiceInstall serviceInstall = new OpenShiftServiceInstall("email", "password", new File("/path/to/fees-service.json"), map);
        try {
            return serviceInstall.createAppUsingHttpWithJsonResponse();
        }
        catch(Exception e) {
        
        }
        return null;
    }

    private void commitApplicationYaml(File yamlFile) {
    
    }
    
    /**
     * Deploy app to OpenShift
     */ 
    private String deployToOpenShift() {
        return "{status: deployed}";
    }
}
