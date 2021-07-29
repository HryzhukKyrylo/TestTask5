package com.natife.testtask5.di

import com.natife.data.di.DataComponent
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent

@ActivityScoped
@EntryPoint
@InstallIn(SingletonComponent::class)
@Component(
    modules = [AppModule::class],
    dependencies = [DataComponent::class]
)
interface AppComponent {
}
