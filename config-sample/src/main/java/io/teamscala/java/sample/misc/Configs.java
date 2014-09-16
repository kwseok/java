package io.teamscala.java.sample.misc;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static io.teamscala.java.sample.misc.Globals.ACTIVE_PROFILE;

public interface Configs {

    public static final String ROOT_CONFIG = "sample";
    public static final String CONFIG_FILE_PREFIX = "config-";

    Config current = ConfigFactory.load(CONFIG_FILE_PREFIX + ACTIVE_PROFILE);
    Config glas = current.getConfig(ROOT_CONFIG);

}
