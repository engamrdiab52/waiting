package com.amrabdelhamiddiab.waiting.framework.firebase.di

import com.amrabdelhamiddiab.waiting.framework.firebase.fcm.FcmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideFcmService(): FcmService {
        return FcmService.create()
    }
}