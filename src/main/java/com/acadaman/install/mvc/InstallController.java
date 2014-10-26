package com.acadaman.install.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.acadaman.install.InstallService;
import com.acadaman.install.mvc.AcadamanSchool;

import java.util.*;

@RestController
public class InstallController {

    @Autowired
    private InstallService installService;

    @RequestMapping(value = "/install", method = RequestMethod.POST, consumes = "application")
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
