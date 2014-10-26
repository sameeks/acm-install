package com.acadaman.install;

import org.springframework.stereotype.Service;

import com.acadaman.install.mvc.AcadamanSchool;
import com.acadaman.install.service.*;

import java.util.*;
import java.util.concurrent.*;
import org.reflections.*;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

@Service
public class InstallService {

    private static final Logger log = LogManager.getLogger(InstallService.class);

    public boolean validateAwsBucket(String schoolname) {
        return false;
    }

    //public void generateAwsBucket(String schoolname) {}

    public Map<String, String> execute(AcadamanSchool school) throws Exception {
        // get the loaded application.yml
        Reflections reflections = new Reflections("com.acadaman.install.service");
        Set<Class<? extends IInstall>> serviceClasses = reflections.getSubTypesOf(IInstall.class);
        HashMap<String, String> map = new HashMap<String, String>();
        ExecutorService executor = Executors.newFixedThreadPool(serviceClasses.size());

        // Execute independent services.
        if(!serviceClasses.isEmpty()) {
            log.info("Item is: " + serviceClasses.iterator().next());
            for(Class<? extends IInstall> klazz: serviceClasses) {
                log.info("class name is: " + klazz.getCanonicalName());
                //log.info("executing..." + klazz.testReflectMethod());
            }
            if(serviceClasses.contains(FeesService.class)) {
//                log.info("Item is: " + serviceClasses.iterator().next());
//                FeesService fees = new FeesService();
//                log.info("executing..." + fees.testReflectMethod());
//                Future<String> feesResult = executor.submit(fees);
//                map.put("fees", feesResult);
            }
            if(serviceClasses.contains(CoreService.class)) {
                log.info("Executing CoreService...");
                Callable<String> core = new CoreService(school);
                Future<String> coreResult = executor.submit(core);
                map.put("core",(String) coreResult.get());
                //core.execute(school);
            }
            // Don't take any new thread.
            executor.shutdown();
            // if we are not awaiting any thread to shutdown after 60 seconds
            // After 60 seconds timeout, terminate any thread forcefully
            if(!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();  
            }
        }
        // Return all the threads return values.
        return map;
    }
}
