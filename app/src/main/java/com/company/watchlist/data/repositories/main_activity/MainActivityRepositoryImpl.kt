package com.company.watchlist.data.repositories.main_activity

import com.company.watchlist.data.UserPreferences
import com.company.watchlist.data.repositories.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MainActivityRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firestoreReference: FirebaseFirestore,
    dataStore: UserPreferences,
): MainActivityRepository, BaseRepositoryImpl(firebaseAuth, firestoreReference, dataStore){

    override suspend fun isNewUser(): Boolean {
        return getAuthReference().currentUser == null
    }

}