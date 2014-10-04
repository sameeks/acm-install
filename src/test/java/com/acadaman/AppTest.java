package com.acadaman;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import com.acadaman.install.config.FeesYamlProperties;

import java.io.*;
import java.util.*;

import org.junit.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
//    private static final Logger log = LogManager.getLogger(AppTest.class);
    private Repository repository = null;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
//    public static Test suite()
//    {
//        return new TestSuite( AppTest.class );
//    }

    @Before
    public void setUp() {
        System.out.println("setting up...");
        // clone the repo
        try {
            File localPath = File.createTempFile("FeesGitRepository", "");
            localPath.delete();

            System.out.println("Cloning from FEES_GIT_REMOTE_URL to " + localPath);
            Git.cloneRepository()
                .setURI("https://github.com/charyorde/acm.git")
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
        //FileRepositoryBuilder builder = new FileRepositoryBuilder();
    }

    public void testFileExistsinRepo() throws IOException {
   
        String path = repository.getDirectory().getAbsolutePath();
        System.out.println("repo absolute path is " + path);
        File newFile = new File(path + File.separator, "application.yml");
        System.out.println("new file is: " + newFile);
        System.out.println("New file is " + newFile.getAbsolutePath());
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        //Properties spring = new Properties();
       
        FeesYamlProperties spring = new FeesYamlProperties();
        List<FeesYamlProperties.Properties> properties = new ArrayList<FeesYamlProperties.Properties>();
        FeesYamlProperties.Properties property = spring.new Properties();
        property.setProfile("remote");
        property.setName("fees");
//        List<FeesYamlProperties.App> apps = new ArrayList<FeesYamlProperties.App>();
//        FeesYamlProperties.App app = spring.new App();
//        app.setName("fees");
//        apps.add(app);
//        spring.setApps(apps);
        properties.add(property);
        spring.setSpring(properties);
        //spring.setProperty("application", StringUtils.join(application));
        //spring.setProperty("application", new HashMap<String, String>().put("name", "fees"));
        FileWriter writer = new FileWriter(newFile);

        Yaml yaml = new Yaml();
        String yamlStr = yaml.dumpAs(spring, null, null);
        BufferedWriter out = new BufferedWriter(writer);
        //FileOutputStream out = new FileOutputStream(writer);
        out.write(yamlStr);
        out.close();

        assertTrue(newFile.exists());
    }

    @After
    public void tearDown() throws IOException {
        File directory = repository.getDirectory();
        // remove the repo dir
        if(directory.exists()) {
            if(directory.list().length == 0) {
                System.out.println("Deleting empty repo directory");
                //directory.delete();
            }
            else {
                System.out.println("Deleting cloned repo directory");
                //FileUtils.deleteDirectory(directory);
            }
        }
        // close repository
        System.out.println("Closing repo");
        repository.close();
    }

    class App {
    
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
        
            this.name = name;
        }
    }
}
