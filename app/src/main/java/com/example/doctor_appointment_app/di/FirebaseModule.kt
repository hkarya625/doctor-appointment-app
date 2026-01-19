package com.arya.bookmydoc.di

import com.arya.bookmydoc.data.remote.FirebaseDataSource
import com.arya.bookmydoc.data.remote.FirebaseDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun providerFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()


}@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindFirebaseDataSource(
        impl: FirebaseDataSourceImpl
    ): FirebaseDataSource
}
