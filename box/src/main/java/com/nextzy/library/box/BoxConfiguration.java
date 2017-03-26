package com.nextzy.library.box;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmFileException;

/**
 * Created by nextzy on 7/26/2016 AD.
 */
public class BoxConfiguration {
    private static BoxConfiguration instance;

    public static BoxConfiguration getInstance() {
        if (instance == null) {
            instance = new BoxConfiguration();
        }
        return instance;
    }

    private static BoxConfiguration getInstance(List<RealmConfiguration> realmConfigurationList) {
        if (instance == null) {
            instance = new BoxConfiguration(realmConfigurationList);
        }
        return instance;
    }

    public static void init(Context context, Builder... boxConfigBuilderList) {
        Realm.init(context.getApplicationContext());
        String key = BoxEncryptedUtility.getInstance().getAppInstalledTime(context);
        key = BoxEncryptedUtility.getInstance().convertToRealmEncryptedKey(key);
        if (boxConfigBuilderList != null) {
            List<RealmConfiguration> realmConfigurationList = new ArrayList<>();
            for (Builder configBuilder : boxConfigBuilderList) {
                realmConfigurationList.add(configBuilder.isEncrypted() ? configBuilder.build(key) : configBuilder.build());
            }
            BoxConfiguration.getInstance(realmConfigurationList);
        }
    }

    private BoxConfiguration() {
        realmConfigurationList = new ArrayList<>();
    }

    private BoxConfiguration(List<RealmConfiguration> realmConfigurationList) {
        this.realmConfigurationList = realmConfigurationList;
    }

    private List<RealmConfiguration> realmConfigurationList;

    private void config(RealmConfiguration realmConfiguration) {
        int index = getRealmConfigIndexByFileName(realmConfiguration.getRealmFileName());
        if (index != -1) {
            realmConfigurationList.set(index, realmConfiguration);
        } else {
            realmConfigurationList.add(realmConfiguration);
        }
    }

    private int getRealmConfigIndexByFileName(String fileName) {
        if (realmConfigurationList != null) {
            for (int index = 0; index < realmConfigurationList.size(); index++) {
                RealmConfiguration realmConfiguration = realmConfigurationList.get(index);
                if (realmConfiguration.getRealmFileName().equals(fileName)) {
                    return index;
                }
            }
        }
        return -1;
    }

    public Realm getConfiguration(String fileName) {
        int index = getRealmConfigIndexByFileName(fileName);
        if (index != -1) {
            Realm realm;
            RealmConfiguration realmConfiguration = realmConfigurationList.get(index);
            try {
                realm = Realm.getInstance(realmConfiguration);
            } catch (RealmFileException exception) {
                Realm.deleteRealm(realmConfiguration);
                realm = Realm.getInstance(realmConfiguration);
            }
            return realm;
        }
        return null;
    }

    public static class Builder {
        private String fileName;
        private boolean isEncrypted;
        private long schemaVersion = -1;

        public Builder() {
        }

        public Builder(String fileName, boolean isEncrypted, long schemaVersion) {
            this.fileName = fileName;
            this.isEncrypted = isEncrypted;
            this.schemaVersion = schemaVersion;
        }

        public String getFileName() {
            return fileName;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public boolean isEncrypted() {
            return isEncrypted;
        }

        public Builder setEncrypted(boolean isEncrypted) {
            this.isEncrypted = isEncrypted;
            return this;
        }

        public long getSchemaVersion() {
            return schemaVersion;
        }

        public Builder setSchemaVersion(long schemaVersion) {
            this.schemaVersion = schemaVersion;
            return this;
        }

        public RealmConfiguration build() {
            return build(null);
        }

        public RealmConfiguration build(String encryptedKey) {
            RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
            // File name is required
            if (getFileName() != null) {
                builder.name(getFileName());
            } else {
                throw new NullPointerException("Box file name can't be null");
            }
            // Schema version is required
            if (getSchemaVersion() != -1) {
                builder.schemaVersion(getSchemaVersion());
            } else {
                throw new NullPointerException("Box schema version should be set");
            }
            // Encrypted is optional
            if (encryptedKey != null) {
                builder.encryptionKey(encryptedKey.getBytes());
            }
            return builder.build();
        }
    }
}
