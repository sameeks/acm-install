package com.acadaman.install.service;

import com.acadaman.install.IInstall;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import com.acadaman.install.config.FeesYamlProperties;
import com.acadaman.install.config.FeesServiceProperties;
import com.acadaman.install.OpenShiftServiceInstall;

import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;

import org.apache.commons.lang3.StringUtils;

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

import org.apache.commons.io.FileUtils;

import java.util.*;
import java.io.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Component
public class FeesService implements IInstall {

    private static final Logger log = LogManager.getLogger(FeesService.class);

    // @todo Add FeesService properties here.
    
    // autowire FeesServiceProperties
    //
    // autowire FeesYamlProperties
    //
    public void execute(Map<String, String> params) {
        // clone the git repo into $OPENSHIFT_DATA_DIR
        Repository repository = null;

        try {
            File localPath = File.createTempFile("FeesGitRepository", "");
            localPath.delete();

            log.info("Cloning from FEES_GIT_REMOTE_URL to " + localPath);
            Git.cloneRepository()
                .setURI("https://github.com/charyorde/fees.git")
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
        try {
            final StoredConfig config = repository.getConfig();
            RemoteConfig remoteConfig = new RemoteConfig(config, "fees-git-repo");
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
        FeesServiceProperties feesService = new FeesServiceProperties();
        feesService.setName(params.getOrDefault("name", ""));
        feesService.setPlatform("java");
        feesService.setRepoUrl("https://github.com/charyorde/fees");
        feesService.setOpenShiftCatridge(Arrays.asList(new String[]{"diy", "mysql"}));
           
        FeesYamlProperties spring = new FeesYamlProperties();
        List<FeesYamlProperties.Properties> properties = new ArrayList<FeesYamlProperties.Properties>();
        FeesYamlProperties.Properties property = spring.new Properties();
        property.setProfile("internal");
        property.setName("fees");
        property.setDriverClassName("com.mysql.jdbc.Driver");
        property.setUrl("jdbc:mysql://10.0.0.4/eduerp");
        property.setUsername(params.getOrDefault("root", "root"));
        property.setPassword(params.getOrDefault("root", "root"));
        property.setDbPort(0); // @todo Get from posted value and convert to int.
        property.setDbHost(params.getOrDefault("root", ""));
        property.setSchoolName(params.getOrDefault("schoolName", ""));
        property.setFqdn(params.getOrDefault("fqdn", ""));
        property.setInitialized(false);
        property.setPlatformVersion("");
        property.setAwsBucketName("");
        property.setFilesystemPath("");

        properties.add(property);
        spring.setSpring(null);

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

        // createOpenShiftApplication
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", feesService.getName());
        map.put("initial_git_url", feesService.getRepoUrl());
        map.put("catridge", StringUtils.join(feesService.getOpenShiftCatridge()));
        OpenShiftServiceInstall serviceInstall = new OpenShiftServiceInstall("email", "password", new File("/path/to/fees-service.json"), null);
        try {
            serviceInstall.createAppUsingHttp();
        }
        catch(Exception e) {
        
        }
    }

    public String testReflectMethod() {
        return "invoked using reflection";
    }
}
