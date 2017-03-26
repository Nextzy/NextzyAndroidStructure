package com.nextzy.library.box;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by trusttanapruk on 8/10/2016.
 */
public class BoxObject extends RealmObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_CLASS_NAME = "className";
    public static final String KEY_JSON_DATA = "jsonData";

    @PrimaryKey
    private String name;
    private String className;
    private String jsonData;

    public BoxObject() {
    }

    public BoxObject(String name, String className, String jsonData) {
        this.name = name;
        this.className = className;
        this.jsonData = jsonData;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getJsonData() {
        return jsonData;
    }
}
