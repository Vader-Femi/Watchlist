package com.company.watchlist.data.repositories.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

interface BaseRepository{

    val useDynamicTheme: Boolean

    fun getAuthReference(): FirebaseAuth

    fun getFirestoreReference(): FirebaseFirestore

    suspend fun userFName(): String

    suspend fun userLName(): String

    suspend fun logOut()
}