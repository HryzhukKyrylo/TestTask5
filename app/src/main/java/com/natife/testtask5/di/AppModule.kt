package com.natife.testtask5.di


import com.natife.testtask5.data.repository.ConnectToServer
import com.natife.testtask5.data.repository.ListRepository
import com.natife.testtask5.data.repository.WorkWithServer
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
    fun provideConnectToServer() = ConnectToServer()

    @Provides
    @Singleton
    fun provideWorkWithServer() = WorkWithServer()

    @Provides
    @Singleton
    fun provideRepository(connect : ConnectToServer, workWithServer: WorkWithServer) =
        ListRepository(connect, workWithServer)
}
