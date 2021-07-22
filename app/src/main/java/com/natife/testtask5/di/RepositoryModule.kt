package com.natife.testtask5.di

import com.natife.testtask5.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindConnectRepository(connect: ConnectRepositoryImpl): ConnectServerRepository

    @Binds
    @Singleton
    abstract fun bindServerRepository(server: ServerRepositoryImpl): ServerRepository

}
