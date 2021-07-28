package com.natife.testtask5.di

import com.natife.domain.data.repo.ConnectServerRepository
import com.natife.domain.data.repo.ServerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindConnectRepository(connect: com.natife.data.repoImpl.ConnectRepositoryImpl): ConnectServerRepository

    @Binds
    @Singleton
    fun bindServerRepository(server: com.natife.data.repoImpl.ServerRepositoryImpl): ServerRepository

}
