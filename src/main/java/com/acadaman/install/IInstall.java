package com.acadaman.install;

import java.util.Map;

public interface IInstall {

    void execute(Map<String, String> params);

    //void populateServiceParams(Map<String, String> params);

    //dumpYaml(Class<Object> bean);

    //commitApplicationYaml();

    //void createOpenShiftApplication();
}
