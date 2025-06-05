package com.example.composeproject.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFireAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}