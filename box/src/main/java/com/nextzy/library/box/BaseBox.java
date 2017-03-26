package com.nextzy.library.box;

import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by trusttanapruk on 11/10/2016.
 */

public abstract class BaseBox {
    private Realm realm;

    protected Realm getRealm() {
        return realm;
    }

    protected void init(Realm realm) {
        this.realm = realm;
    }

    public void add(String name, Object object) {
        String json = new Gson().toJson(object);
        add(name, object.getClass().getSimpleName(), json);
    }

    public void add(String name, String className, String json) {
        add(new BoxObject(name, className, json));
    }

    public void add(BoxObject object) {
        realm.executeTransaction(realm -> realm.insertOrUpdate(object));
    }

    public void add(BoxObject objects, TransactionCallBack callback) {
        realm.executeTransactionAsync(realm -> realm.insert(objects),
                callback::onSuccess,
                callback::onFailure);
    }

    public RealmResults<? extends BoxObject> findAll() {
        return realm.where(BoxObject.class).findAll();
    }

    public RealmResults<? extends BoxObject> findAll(String name, Sort sortType) {
        return realm.where(BoxObject.class).findAll().sort(name, sortType);
    }

    @SuppressWarnings("unchecked")
    public void findAll(RealmChangeListener listener) {
        RealmResults results = realm.where(BoxObject.class).findAllAsync();
        results.addChangeListener(listener);
    }

    @SuppressWarnings("unchecked")
    public void findAll(String name, Sort sortType, final RealmChangeListener listener) {
        RealmResults results = realm.where(BoxObject.class).findAll().sort(name, sortType);
        results.addChangeListener(listener);
    }

    public BoxObject findByName(String value) {
        return find(BoxObject.KEY_NAME, value);
    }

    public <T> T findByName(String value, Class<T> cls) {
        BoxObject object = find(BoxObject.KEY_NAME, value);
        if (object != null) {
            return new Gson().fromJson(object.getJsonData(), cls);
        }
        return null;
    }

    public BoxObject find(String name, String value) {
        return realm.where(BoxObject.class).equalTo(name, value).findFirst();
    }

    public <T> T find(String name, String value, Class<T> cls) {
        BoxObject object = find(name, value);
        if (object != null) {
            return new Gson().fromJson(object.getJsonData(), cls);
        }
        return null;
    }

    public void delete(String name, String value) {
        delete(find(name, value), null);
    }

    public void delete(BoxObject object) {
        delete(object, null);
    }

    public void delete(BoxObject object, final TransactionCallBack callBack) {
        realm.executeTransaction(realm -> {
            if (object != null) {
                object.deleteFromRealm();
            }
        });

        if (callBack != null && object.isValid()) {
            callBack.onSuccess();
        } else if (callBack != null && !object.isValid()) {
            callBack.onFailure(new Throwable("Object deletion invalid"));
        }
    }

    public void deleteByName(String value, TransactionCallBack callBack) {
        delete(find(BoxObject.KEY_NAME, value), callBack);
    }

    public void deleteByName(String value) {
        delete(find(BoxObject.KEY_NAME, value), null);
    }

    public void close() {
        if (!realm.isClosed()) {
            realm.close();
            realm.removeAllChangeListeners();
        }
    }

    public void deleteAllDatabase() {
        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }

    interface TransactionCallBack {
        void onSuccess();

        void onFailure(Throwable error);
    }
}
