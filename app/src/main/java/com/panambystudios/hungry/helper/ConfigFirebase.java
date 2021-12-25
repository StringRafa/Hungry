package com.panambystudios.hungry.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {

    private static DatabaseReference referenceFirebase;
    private static FirebaseAuth referenceAuthentication;
    private static StorageReference referenceStorage;

    public static String getIdUsuario(){
        FirebaseAuth authentication = getFirebaseAuthentication();
        return authentication.getCurrentUser().getUid();
    }

    //Retorna a referencia do database
    public static DatabaseReference getFirebase(){
        if (referenceFirebase == null){
            referenceFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenceFirebase;
    }

    //Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAuthentication(){
        if (referenceAuthentication == null){
            referenceAuthentication = FirebaseAuth.getInstance();
        }
        return referenceAuthentication;
    }

    //Retorna instancia do FirebaseStorage
    public static StorageReference getFirebaseStorage(){
        if (referenceStorage == null){
            referenceStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenceStorage;
    }
}
