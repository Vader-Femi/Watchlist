package com.company.watchlist.di

import android.app.Application
import com.company.watchlist.data.UserPreferences
import com.company.watchlist.data.remote.RetrofitBuilder
import com.company.watchlist.data.remote.TMDBApi
import com.company.watchlist.use_case.ValidateEmail
import com.company.watchlist.use_case.ValidateLogInPassword
import com.company.watchlist.use_case.ValidateName
import com.company.watchlist.use_case.ValidateSearch
import com.company.watchlist.use_case.ValidateSignUpPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(application: Application): UserPreferences {
        return UserPreferences(application)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthReference(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseCollectionReference(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(): TMDBApi {
        return RetrofitBuilder.api
    }

    @Provides
    @Singleton
    fun provideValidateSearch(application: Application): ValidateSearch {
        return ValidateSearch(application)
    }

    @Provides
    @Singleton
    fun provideValidateEmail(application: Application): ValidateEmail {
        return ValidateEmail(application)
    }

    @Provides
    @Singleton
    fun provideValidateLogInPassword(application: Application): ValidateLogInPassword {
        return ValidateLogInPassword(application)
    }
    @Provides
    @Singleton
    fun provideValidateName(application: Application): ValidateName {
        return ValidateName(application)
    }

    @Provides
    @Singleton
    fun provideValidateSignUpPassword(application: Application): ValidateSignUpPassword {
        return ValidateSignUpPassword(application)
    }

}