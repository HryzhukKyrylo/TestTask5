package com.natife.testtask5.di


import android.content.Context
import android.content.SharedPreferences

import com.natife.testtask5.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
private const val CUSTOM_PREF_NAME = "my_preferences"


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideConnectRepository(connect: ConnectRepositoryImpl):ConnectServerRepository = connect


    @Provides
    @Singleton
    fun provideServerRepository(server: ServerRepositoryImpl):ServerRepository = server

    @Provides
    @Singleton
    fun provideSharedRepository(connect : ConnectServerRepository, server: ServerRepository) :Repository =
        SharedRepositoryImpl(connect, server)

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext appContext: Context) : SharedPreferences =
    appContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

}
