package com.nextzy.library.box;

import android.content.Context;
import android.content.pm.PackageManager;

import io.realm.RealmConfiguration;

/**
 * Created by Akexorcist on 3/27/2017 AD.
 */

public class BoxEncryptedUtility {
    private static BoxEncryptedUtility utility;

    public static BoxEncryptedUtility getInstance() {
        if (utility == null) {
            utility = new BoxEncryptedUtility();
        }
        return utility;
    }

    public String getAppInstalledTime(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime + "";
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "N/A";
    }

    public String convertToRealmEncryptedKey(String key) {
        String encryptedKey = "";
        while (encryptedKey.length() < RealmConfiguration.KEY_LENGTH) {
            encryptedKey += key;
            if (encryptedKey.length() < RealmConfiguration.KEY_LENGTH) {
                encryptedKey += "|";
            }
        }
        return encryptedKey.substring(0, RealmConfiguration.KEY_LENGTH);
    }
}
