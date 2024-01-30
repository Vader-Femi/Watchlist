package com.company.watchlist.data.repositories.base

import com.company.watchlist.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

open class BaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getFirestoreReference: FirebaseFirestore,
    private val dataStore: UserPreferences,
) : BaseRepository {

    override val useDynamicTheme = true

    override fun getAuthReference() = firebaseAuth

    override fun getFirestoreReference() = getFirestoreReference

    override suspend fun logOut() {
        getAuthReference().signOut()
    }

}