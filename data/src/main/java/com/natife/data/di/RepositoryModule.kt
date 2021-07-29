package com.natife.data.di


import com.natife.data.repoImpl.ConnectRepositoryImpl
import com.natife.data.repoImpl.ServerRepositoryImpl
import com.natife.domain1.data.repo.ConnectServerRepository
import com.natife.domain1.data.repo.ServerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton

@InstallIn(DataComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindConnectRepository(connect: ConnectRepositoryImpl): ConnectServerRepository

    @Binds
    @Singleton
    fun bindServerRepository(server: ServerRepositoryImpl): ServerRepository
}
