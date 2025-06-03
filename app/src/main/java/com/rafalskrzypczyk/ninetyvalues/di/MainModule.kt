package com.rafalskrzypczyk.ninetyvalues.di

import com.rafalskrzypczyk.ninetyvalues.data.RepositoryImpl
import com.rafalskrzypczyk.ninetyvalues.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    @Singleton
    abstract fun bindRepository(impl: RepositoryImpl): Repository
}