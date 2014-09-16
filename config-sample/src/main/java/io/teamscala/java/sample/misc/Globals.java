package io.teamscala.java.sample.misc;

import static io.teamscala.java.sample.misc.Globals.Profiles.DEV;
import static org.apache.commons.lang3.StringUtils.defaultString;

public interface Globals {

    interface Profiles {
        String DEV = "dev";
        String PROD = "prod";
    }

    String ACTIVE_PROFILE = defaultString(System.getProperty("app.profile"), DEV);
}
