package com.amrabdelhamiddiab.waiting.framework.firebase.di

import com.amrabdelhamiddiab.core.data.IDownloadService
import com.amrabdelhamiddiab.core.data.IUploadService
import com.amrabdelhamiddiab.core.data.login.*
import com.amrabdelhamiddiab.core.usecases.login.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {
    @Provides
    fun provideRepositorySignUpUser(iSignUpUser: ISignUpUser): RepositorySignUpUser {
        return RepositorySignUpUser(iSignUpUser)
    }

    @Provides
    fun provideRepositorySignInUser(iSignInUser: ISignInUser): RepositorySignInUser {
        return RepositorySignInUser(iSignInUser)
    }

    @Provides
    fun provideRepositorySignOutUser(iSignOutUser: ISignOutUser): RepositorySignOutUser {
        return RepositorySignOutUser(iSignOutUser)
    }

    @Provides
    fun provideRepositorySendEmailVerification(iSendEmailVerification: ISendEmailVerification): RepositorySendEmailVerification {
        return RepositorySendEmailVerification(iSendEmailVerification)
    }

    @Provides
    fun provideRepositoryResetUserPassword(iResetUserPassword: IResetUserPassword): RepositoryResetUserPassword {
        return RepositoryResetUserPassword(iResetUserPassword)
    }

    @Provides
    fun provideRepositoryEmailVerifiedState(iEmailVerifiedState: IEmailVerifiedState): RepositoryEmailVerifiedState {
        return RepositoryEmailVerifiedState(iEmailVerifiedState)
    }

    @Provides
    fun provideSignUpUserUseCase(repositorySignUpUser: RepositorySignUpUser): SignUpUser {
        return SignUpUser(repositorySignUpUser)
    }

    @Provides
    fun provideSignInUserUseCase(repositorySignInUser: RepositorySignInUser): SignInUser {
        return SignInUser(repositorySignInUser)
    }

    @Provides
    fun provideSignOutUserUseCase(repositorySignOutUser: RepositorySignOutUser): SignOutUser {
        return SignOutUser(repositorySignOutUser)
    }

    @Provides
    fun provideSendEmailVerification(repositorySendEmailVerification: RepositorySendEmailVerification): SendEmailVerification {
        return SendEmailVerification(repositorySendEmailVerification)
    }

    @Provides
    fun provideResetUserPassword(repositoryResetUserPassword: RepositoryResetUserPassword): ResetUserPassword {
        return ResetUserPassword(repositoryResetUserPassword)
    }

    @Provides
    fun provideEmailVerifiedStated(repositoryEmailVerifiedState: RepositoryEmailVerifiedState): EmailVerifiedState {
        return EmailVerifiedState(repositoryEmailVerifiedState)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideUploadService(repositoryUploadService: RepositoryUploadService): UploadService {
        return UploadService(repositoryUploadService)
    }

    @Provides
    fun provideDownloadService(repositoryDownloadService: RepositoryDownloadService): DownloadService {
        return DownloadService(repositoryDownloadService)
    }

    @Provides
    fun provideRepositoryUploadService(iUploadService: IUploadService): RepositoryUploadService {
        return RepositoryUploadService(iUploadService)
    }

    @Provides
    fun provideRepositoryDownloadService(iDownloadService: IDownloadService): RepositoryDownloadService {
        return RepositoryDownloadService(iDownloadService)
    }



}