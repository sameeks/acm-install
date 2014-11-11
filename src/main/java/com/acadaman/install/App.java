package com.acadaman.install;

import spark.Request;
import spark.Response;
import static spark.Spark.*;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.PropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;
import com.acadaman.install.service.FeesService;

import org.reflections.*;

import java.io.IOException;
import java.util.Set;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

/**
 * Fees app bootstrap
 *
 */
@Controller
@Configuration
@EnableConfigurationProperties
@EnableAutoConfiguration
@ComponentScan
public class App 
{
    private static final Logger log = LogManager.getLogger(App.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private Environment env;

    private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
    private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 8505;
    //private static final String DATASOURCE_URL = System.getenv("OPENSHIFT_MYSQL_DB_HOST") != null ? env.getProperty("spring.datasource.url") : "";

//    @Bean
//    public PropertySource<?> yamlPropertySourceLoader() throws IOException {
//        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
//        PropertySource<?> applicationYamlPropertySource = loader.load(
//            "application.yml", new ClassPathResource("application.yml"),"default");
//        return applicationYamlPropertySource;
//    }

//    @Bean
//    public DataSource dataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver"));
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.username"));
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        return dataSource;
//    }

    // add here springmvc if spark wouldn't work.
    @RequestMapping("/")
    @ResponseBody
    public String home() {
        String username = this.jdbcTemplate.queryForObject(
                        "select username from client_account where id = ?",
                                new Object[]{1L}, String.class);
        System.out.println("Username is" + username);
        return "Hello World!";
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class, args);
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        // An executed filter to valid oauth
        before((request, response) -> {
            response.body("Filtering oauth");
        });

        get("/", (request, response) -> {
            System.out.println("Spark is initialized");
            return "Welcome to Acadaman install service";
        });

        post("/install", (request, response) -> {
            // @todo Validate by loading the AWS bucket. If it exists, return "School exists already".
            // @todo generate AWS bucket for schoolname
            // get the post params
            Map<String, String> params = request.params();
            log.info(params.getOrDefault("schoolname", "coursera"));
            // get the loaded application.yml
            Reflections reflections = new Reflections("com.acadaman.install.service");
            Set<Class<? extends IInstall>> serviceClasses = reflections.getSubTypesOf(IInstall.class);
            // for each of the services
            if(!serviceClasses.isEmpty()) {
                    log.info("Item is: " + serviceClasses.iterator().next());
                if(serviceClasses.contains(FeesService.class)) {
                    log.info("Item is: " + serviceClasses.iterator().next());
                    FeesService fees = new FeesService();
                    log.info("executing..." + fees.testReflectMethod());
                }
                for(Class<? extends IInstall> klazz: serviceClasses) {
                    log.info("class name is: " + klazz.getCanonicalName());
                    //log.info("executing..." + klazz.testReflectMethod());
                }
            }
            //  write params 
            //  make http request to openshift to create the app for the school
            return "opevel school setup successfully";// return 1 if install is successful.
        });
    }
}
