package com.natife.testtask5.di


import android.content.Context
import android.content.SharedPreferences
import com.natife.data.repoImpl.SharedRepositoryImpl
import com.natife.domain1.data.repo.ConnectServerRepository
import com.natife.domain1.data.repo.Repository
import com.natife.domain1.data.repo.ServerRepository
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
    fun providePreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    internal fun provideSharedRepository(
        connect: ConnectServerRepository,
        server: ServerRepository
    ): Repository =
        SharedRepositoryImpl(connect, server)

}
