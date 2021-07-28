package com.natife.data.di

import dagger.hilt.DefineComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *@author admin
 */
@Singleton
@DefineComponent(parent = SingletonComponent::class)
interface DataComponent {
}
