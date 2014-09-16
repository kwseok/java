package io.teamscala.java.sample;


import io.teamscala.java.sample.misc.Configs;

public class TestConfig {

    public static void main(String[] args) {
        System.out.println(Configs.glas.getString("profile"));
        System.out.println(Configs.glas.getConfig("upload"));
        System.out.println(Configs.glas.getConfig("smtp"));
        System.out.println(Configs.glas.getConfig("database"));
    }

}
