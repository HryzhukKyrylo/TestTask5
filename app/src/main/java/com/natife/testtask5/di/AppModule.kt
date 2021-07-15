package com.natife.testtask5.di


import com.natife.testtask5.data.repository.ConnectServerRepository
import com.natife.testtask5.data.repository.Repository
import com.natife.testtask5.data.repository.SharedRepositoryImpl
import com.natife.testtask5.data.repository.WorkServerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideConnectToServer() = ConnectServerRepository()

    @Provides
    @Singleton
    fun provideWorkWithServer() = WorkServerRepository()

    @Provides
    @Singleton
    fun provideSharedRepository(connect : ConnectServerRepository, workWithServer: WorkServerRepository) :Repository =
        SharedRepositoryImpl(connect, workWithServer)

}
