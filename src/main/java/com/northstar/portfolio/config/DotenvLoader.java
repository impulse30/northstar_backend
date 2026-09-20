package com.northstar.portfolio.config;

import io.github.cdimascio.dotenv.Dotenv;

public final class DotenvLoader {

    private DotenvLoader() {
    }

    public static void load() {
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();

            if (System.getenv(key) == null && System.getProperty(key) == null) {
                System.setProperty(key, entry.getValue());
            }
        });
    }
}