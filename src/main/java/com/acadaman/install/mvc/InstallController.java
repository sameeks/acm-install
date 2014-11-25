package com.acadaman.install.mvc;

import com.acadaman.install.InstallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InstallController {

    @Autowired
    private InstallService installService;

    @RequestMapping(value = "/install", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, String> install(@RequestBody AcadamanSchool school) {
        // installService.validateAwsBucket
        try {
            return installService.execute(school);
        }
        catch (Exception e) {
        
        }
        return null;
    }
}
