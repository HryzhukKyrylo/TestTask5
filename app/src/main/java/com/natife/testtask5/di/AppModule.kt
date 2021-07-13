package com.natife.testtask5.di


import com.natife.testtask5.data.repository.ConnectToServer
import com.natife.testtask5.data.repository.ListRepository
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
    fun provideRepository(connect : ConnectToServer) = ListRepository(connect)
}
