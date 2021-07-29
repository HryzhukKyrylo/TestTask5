package com.natife.data.di

import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Singleton
@DefineComponent(parent = SingletonComponent::class)
interface DataComponent {
}
