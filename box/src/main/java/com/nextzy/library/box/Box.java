package com.nextzy.library.box;

/**
 * Created by Akexorcist on 3/27/2017 AD.
 */

public class Box extends BaseBox {
    public static final String TYPE_DEFAULT = "default";
    private static Box instance;

    public static Box getInstance() {
        if (instance == null) {
            instance = new Box();
        }
        return instance;
    }

    public static void setMockInstance(Box instance) {
        Box.instance = instance;
    }

    private BoxConfiguration.Builder builder;

    private Box() {
        builder = new BoxConfiguration.Builder()
                .setFileName("defaultBox.realm")
                .setSchemaVersion(1)
                .setEncrypted(true);
    }

    public BoxConfiguration.Builder getConfigBuilder() {
        return builder;
    }
}
