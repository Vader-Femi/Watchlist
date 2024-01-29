package com.company.watchlist.data.repositories.authentication

import com.company.watchlist.data.UserPreferences
import com.company.watchlist.data.repositories.base.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    firestoreReference: FirebaseFirestore,
    dataStore: UserPreferences
): AuthenticationRepository, BaseRepositoryImpl(firebaseAuth, firestoreReference, dataStore)