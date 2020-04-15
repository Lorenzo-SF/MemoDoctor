package com.memodoctor.memodoctor.application;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.memodoctor.memodoctor.model.MemoTask;
import com.memodoctor.memodoctor.model.MemoType;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MemoDoctorApplication extends Application {
    public static AtomicInteger MemoTaskID = new AtomicInteger();
    public static AtomicInteger MemoTypeID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();

        SetupRealmConfiguration();

        Realm realm = Realm.getDefaultInstance();

        MemoTaskID = GetLastId(realm, MemoTask.class);
        MemoTypeID = GetLastId(realm, MemoType.class);
        CreateMemoTypes(realm);

        realm.close();
    }

    private <T extends RealmObject> AtomicInteger GetLastId(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return results.size() > 0 ? new AtomicInteger( results.max("Id").intValue()) : new AtomicInteger();
    }

    private void SetupRealmConfiguration(){
        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .name("MemoDoctor.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getInstance(config);
    }

    private void CreateMemoTypes(Realm realm){
        RealmResults<MemoType> memoTypes = realm.where(MemoType.class).findAll();

        if(memoTypes.size() == 0){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    MemoType mt_1 = new MemoType("-- Seleccione tipo de cita --");
                    MemoType mt_2 = new MemoType("Médico de cabecera");
                    MemoType mt_3 = new MemoType("Enfermería");
                    MemoType mt_4 = new MemoType("Médico especialista");
                    MemoType mt_5 = new MemoType("Prueba");
                    MemoType mt_6 = new MemoType("Otros");

                    realm.copyToRealmOrUpdate(mt_1);
                    realm.copyToRealmOrUpdate(mt_2);
                    realm.copyToRealmOrUpdate(mt_3);
                    realm.copyToRealmOrUpdate(mt_4);
                    realm.copyToRealmOrUpdate(mt_5);
                    realm.copyToRealmOrUpdate(mt_6);
                }
            });
        }
    }


    public static void ShowToast(Context context, String message, int length){
        Toast.makeText(context, message, length).show();
    }
}
