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
abstract class IntefacesModule {
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

    @Binds
    abstract fun bindDeleteAccount(deleteAccountImpl: DeleteAccountImpl): IDeleteAccount

    @Binds
    abstract fun bindDeleteService(deleteServiceImpl: DeleteServiceImpl): IDeleteService

    @Binds
    abstract fun bindDeleteThisDay(deleteThisDayImpl: DeleteThisDayImpl): IDeleteThisDay


    @Binds
    abstract fun bindDeleteCurrentOrder(deleteCurrentOrderImpl: DeleteCurrentOrderImpl): IDeleteCurrentOrder

    @Binds
    abstract fun bindChangeOrderValue(changeOrderValueImpl: ChangeOrderValueImpl) : IChangeOrderValue

    @Binds
    abstract fun bindDownloadOrder(downloadOrderImpl: DownloadOrderImpl): IDownloadOrder

    @Binds
    abstract fun bindUploadClientToken(uploadClientTokenImpl: UploadClientTokenImpl) :IUploadClientToken

    @Binds
    abstract fun bindDownloadToken(downloadTokenImpl: DownloadTokenImpl) : IDownloadToken

    @Binds
    abstract fun bindListDownloadTokens(listDownloadTokensImpl: ListDownloadTokensImpl) : IListDownloadTokens

    @Binds
    abstract fun bindRemoveClientToken(removeClientTokenImpl: RemoveClientTokenImpl) : IRemoveClientToken
}