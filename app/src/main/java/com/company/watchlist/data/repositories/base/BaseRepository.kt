package com.company.watchlist.data.repositories.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

interface BaseRepository{

    val useDynamicTheme: Boolean

    fun getAuthReference(): FirebaseAuth

    fun getFirestoreReference(): FirebaseFirestore

    suspend fun userFName(fName: String)

    suspend fun userFName(): String

    suspend fun userLName(lName: String)

    suspend fun userLName(): String

    suspend fun logOutFromFirebaseAndDeleteFromPref()
}