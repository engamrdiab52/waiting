package com.amrabdelhamiddiab.waiting.framework.firebase.di

import com.amrabdelhamiddiab.core.data.IDownloadService
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.data.IUploadService
import com.amrabdelhamiddiab.core.data.login.*
import com.amrabdelhamiddiab.waiting.framework.firebase.login.*
import com.amrabdelhamiddiab.waiting.framework.utilis.PreferenceManager
import com.google.firebase.database.ValueEventListener
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class intefacesModule {
    @Binds
    abstract fun bindSignUpUser(signupUserImpl: SignupUserImpl): ISignUpUser

    @Binds
    abstract fun bindSignInUser(signInUserImpl: SignInUserImpl): ISignInUser

    @Binds
    abstract fun bindSignOutUser(signOutUserImpl: SignOutUserImpl): ISignOutUser

    @Binds
    abstract fun bindSendEmailVerification(sendEmailVerificationImpl: SendEmailVerificationImpl): ISendEmailVerification

    @Binds
    abstract fun bindResetUserPassword(resetUserPasswordImpl: ResetUserPasswordImpl): IResetUserPassword

    @Binds
    abstract fun bindEmailVerifiedState(emailVerifiedStateImpl: EmailVerifiedStateImpl): IEmailVerifiedState

    @Binds
    abstract fun bindPreferenceHelper(preferenceManager: PreferenceManager): IPreferenceHelper

    @Binds
    abstract fun bindUploadService(uploadServiceImpl: UploadServiceImpl): IUploadService

    @Binds
    abstract fun bindDownloadService(downloadServiceImpl: DownloadServiceImpl): IDownloadService

    @Binds
    abstract fun bindValueEventListener(notifyOrderChangerImpl: NotifyOrderChangerImpl): ValueEventListener
}