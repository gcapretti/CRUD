package com.example.crud;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //En manifests agrego clase MyFirebaseApp para que en cada activity haya persistencia de datos
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
