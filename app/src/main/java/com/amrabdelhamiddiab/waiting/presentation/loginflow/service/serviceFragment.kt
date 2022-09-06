package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.NotificationData
import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentServiceBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.amrabdelhamiddiab.waiting.framework.utilis.toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class serviceFragment : Fragment() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var permissionGranted: Boolean = false

    private lateinit var navigationView: NavigationView
    private lateinit var navigationHeader: View
    private lateinit var navigationHeaderTitle: TextView
    private lateinit var navigationHeaderNameOfService: TextView
    private lateinit var navigationHeaderPeriod: TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var iPreferenceHelper: IPreferenceHelper
    private lateinit var callbackManager: CallbackManager

    private var myService: Service = Service("", "", "", 0)

    private var passwordForDeleteAccount: String = ""
    private lateinit var binding: FragmentServiceBinding
    private var fromButton: Boolean = false
    private val viewModel by viewModels<ServiceViewModel>()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "onActivityResult: Google SignIn intent Result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (accountTask.isSuccessful) {
                Log.d(TAG, "onActivityResult: accountTask.isSuccessful")
                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    if (account != null) {
                        // Initialize auth credential
                        firebaseAuthWithGoogleAccount(account)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onActivityResult: ${e.message}")
                }
            } else {
                Log.d(TAG, "onActivityResult: accountTask.isNOTSuccessful: ")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Firebase
        firebaseAuth = viewModel.firebaseAuth
        //Initialize Google Sign in
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)


        //Initialize facebook login
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "-----------------------facebook:onSuccess:$result")
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "*****************************facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "///////////////////////////facebook:onError", error)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)
        iPreferenceHelper = viewModel.preferenceHelper
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                permissionGranted = true
                // Do if the permission is granted
                Toast.makeText(
                    requireContext(),
                    "permission Post Notification Already granted",
                    Toast.LENGTH_LONG
                )
                    .show()

            } else {
                // Do otherwise
                //   Toast.makeText(requireContext(), "permission Denied", Toast.LENGTH_LONG).show()
                showPermissionDeniedDialog()
            }
        }


        askForPostNotificationPermission()

        viewModel.notifyWhenServiceChange()
        viewModel.notifyWhenOrderChange()
        viewModel.notifyWhenListOfTokensChanged()
        viewModel.downloadServiceV()

        viewModel.downloading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingIndecator.visibility = View.VISIBLE
            } else {
                binding.loadingIndecator.visibility = View.GONE
            }
        }
        viewModel.service.observe(viewLifecycleOwner) {
            Log.d(
                TAG,
                "//////////////////////////??????????????????" + it?.name_of_service.toString()
            )
            if (it != null) {
                myService = it
                navigationHeaderTitle.text = myService.category
                navigationHeaderNameOfService.text = myService.name_of_service
            }
            if (it != null) {
                val text = it.period_per_each_service
                (getString(R.string.about) + " " + text + " " + getString(R.string.minuits_for_each_visit)).also { it1 ->
                    navigationHeaderPeriod.text = it1
                }
            } else {
                navigationHeaderPeriod.text = "0"
            }
        }

        viewModel.orderValue.observe(viewLifecycleOwner) {

            if (it != null) {
                binding.textViewCurrentNumber.text = it.order.toString()
                //  val token = viewModel.tokenDownloaded.value?.token ?: ""
                if (fromButton) {
                    fromButton = false
                    val listOfTokens = viewModel.listOfDownloadedTokens.value
                    if (!listOfTokens.isNullOrEmpty()) {
                        listOfTokens.forEach { token ->
                            if (token.visitorNumber == it.order.toInt()) {
                                PushNotification(
                                    NotificationData(
                                        getString(R.string.current_serving_number),
                                        it.order.toString()
                                    ),
                                    token.token
                                ).also { pushNotification ->
                                    viewModel.sendNotification(pushNotification)
                                }
                            }
                        }
                    }
                }
            }
        }
        viewModel.dataBaseError.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }
        //*****************************
        viewModel.userSignedOut.observe(viewLifecycleOwner) {
            if (it == true) {
                iPreferenceHelper.saveSignInMethode("")
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
        }
        //*********************************
        viewModel.tokenDownloaded.observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(requireContext(), "NO Client is waiting......", Toast.LENGTH_LONG)
                    .show()
            }

        }

        viewModel.dayEnd.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
        }

        binding.cardViewServiceIncrementOrder.setOnClickListener {
            fromButton = true
            val currentOrderValueFromTextView = binding.textViewCurrentNumber.text.toString()
            viewModel.incrementCurrentOrderValue(currentOrderValueFromTextView)
        }

        binding.buttonEndThisDay.setOnClickListener {
            displayDialogAreYouSure()
        }

        viewModel.userDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                requireContext().toast("Account Deleted")
                iPreferenceHelper.saveSignInMethode("")
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
        }
        viewModel.serviceDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                when (viewModel.preferenceHelper.loadSignInMethode()) {
                    "email" -> {
                        if (passwordForDeleteAccount.isNotEmpty()) {
                            viewModel.deleteAccountV(passwordForDeleteAccount)

                        }
                    }
                    "google" -> {
                        firebaseAuth.currentUser?.delete()?.addOnCompleteListener {
                            viewModel.deleteAccountFromGoogle()
                        }
                    }
                    "facebook" -> {
                        firebaseAuth.currentUser?.delete()?.addOnCompleteListener {
                            viewModel.deleteAccountFromFacebook()
                        }
                    }
                    else -> requireContext().toast("Error in Delete")
                }
            }
        }
        //***********************************************************************************
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        navigationView = requireActivity().findViewById(R.id.navigation_view)
        navigationHeader = navigationView.getHeaderView(0)
        navigationHeaderTitle = navigationHeader.findViewById(R.id.textView_category_nav_header)
        navigationHeaderPeriod =
            navigationHeader.findViewById(R.id.text_view_peiod_for_each_visitor_nav_header)
        navigationHeaderNameOfService =
            navigationHeader.findViewById(R.id.textView_name_of_service_nav_header)
    }

    private fun askForPostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permissionGranted = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_service, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_edit_current_order -> {
                        displayDialogEditOrder()
                        true
                    }
                    R.id.menu_delete_account -> {

                        when (viewModel.preferenceHelper.loadSignInMethode()) {
                            "email" -> {
                                displayDialogDeleteAccount()
                            }
                            "google" -> {
                                displayDialogDeleteAccountFromGoogle()
                            }
                            "facebook" -> {
                                displayDialogDeleteAccountFromFacebook()
                            }
                            else -> requireContext().toast("Error in Sign out")
                        }
                        true
                    }
                    R.id.menu_qr_code -> {
                        findNavController().navigate(R.id.action_serviceFragment_to_QRcodeFragment)
                        true
                    }
                    R.id.menu_log_out -> {
                        displayDialogLogOut()
                        true
                    }
                    R.id.menu_edit_service -> {
                        findNavController().navigate(R.id.action_serviceFragment_to_createServiceFragment)
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun displayDialogDeleteAccount() {
        var myValue: CharSequence = ""
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_delete_account_title)
            message(R.string.dialog_delete_account_message)
            val input = input(
                hint = getString(R.string.enter_your_password_here),
                allowEmpty = false,
                inputType = InputType.TYPE_CLASS_TEXT
            ) { _, password ->
                myValue = password
            }
            //*********************************
            positiveButton(R.string.delete_account) {
                if (myValue.isNotEmpty()) {

                    viewModel.deleteServiceV()
                    passwordForDeleteAccount = myValue.toString()
                } else {
                    it.dismiss()
                }
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun displayDialogAreYouSure() {
        MaterialDialog(requireContext()).show {
            title(R.string.end_the_day)
            message(R.string.dialog_end_day_service)
            positiveButton(R.string.yes) {
                viewModel.deleteThisDayV()
                if (!viewModel.sayIfReviewDone()) {
                    val reviewManager = ReviewManagerFactory.create(requireContext())
                    val request = reviewManager.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        try {
                            if (task.isSuccessful) {
                                // We got the ReviewInfo object
                                val reviewInfo = task.result
                                if (reviewInfo != null) {
                                    val flow = reviewManager.launchReviewFlow(
                                        requireActivity(),
                                        reviewInfo
                                    )
                                    flow.addOnCompleteListener {
                                        //here i will put logic to not ask again
                                        Log.d(
                                            TAG,
                                            viewModel.sayIfReviewDone().toString()
                                        )
                                        viewModel.setReviewDoneStatus(false)
                                    }
                                }
                            } else {

                                val reviewErrorCode = task.exception?.message
                                Log.d(
                                    TAG,
                                    "++++++++++++++" + reviewErrorCode.toString()
                                )
                            }

                        } catch (ex: Exception) {
                            Log.d(TAG, ex.message.toString())
                        }
                    }
                }
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun displayDialogEditOrder() {
        var myValue: CharSequence = ""
        MaterialDialog(requireContext()).show {
            val input = input(
                hint = getString(R.string.enter_current_serving_number),
                allowEmpty = false,
                maxLength = 3,
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { _, currentNumber ->
                myValue = currentNumber
            }
            positiveButton(R.string.edit) {
                if (myValue.isNotEmpty()) {
                    fromButton = true
                    binding.textViewCurrentNumber.text = myValue.toString()
                    viewModel.changeOrderValueV(myValue.toString().toLong())
                } else {
                    it.dismiss()
                }
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun displayDialogLogOut() {
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_logout_title)
            message(R.string.dialog_logout_message)
            positiveButton(R.string.yes) {
                when (viewModel.preferenceHelper.loadSignInMethode()) {
                    "email" -> {
                        viewModel.signOut()
                    }
                    "google" -> {
                        val googleSignInClient =
                            GoogleSignIn.getClient(
                                requireContext(),
                                GoogleSignInOptions.DEFAULT_SIGN_IN
                            )
                        googleSignInClient.signOut().addOnCompleteListener {
                            if (it.isSuccessful) {
                                viewModel.firebaseAuth.signOut()
                                viewModel.signOutFromGoogle()
                            }
                        }
                    }
                    "facebook"->{
                        if (firebaseAuth.currentUser != null){
                            firebaseAuth.signOut()
                            LoginManager.getInstance().logOut()
                            viewModel.signOutFromFacebook()
                        }
                        //need to know how to sign out from facebook
                    }
                    else -> requireContext().toast("Error in Sign out")
                }
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    //here i will customize it
    private fun displayDialogDeleteAccountFromGoogle() {
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_delete_account_title)
            message(R.string.dialog_delete_account_message)
            positiveButton(R.string.delete_account) {
                deleteAccountFromGoogle()

            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun displayDialogDeleteAccountFromFacebook() {
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_delete_account_title)
            message(R.string.dialog_delete_account_message)
            positiveButton(R.string.delete_account) {
                deleteAccountFromFacebook()

            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun deleteAccountFromGoogle() {
        Log.d(TAG, "onCreate: begin google sign in")
        val intent = googleSignInClient.signInIntent
        //startActivityForResult(intent, RC_SIGN_IN)
        launcher.launch(intent)
    }

    private fun deleteAccountFromFacebook() {
            //reInter Again
        LoginManager.getInstance().logInWithReadPermissions(
            requireActivity(),
            callbackManager,
            listOf("email", "public_profile")
        )
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credentials = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credentials).addOnSuccessListener { authResult ->
            //login success
            viewModel.deleteServiceV()
        }
            .addOnFailureListener { e ->
                //login failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn Failed due to ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "LoggedIn Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //login success
                    viewModel.deleteServiceV()
                    // Sign in success, update UI with the signed-in user's information
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        navigationHeader.visibility = View.VISIBLE
        viewModel.downloadServiceV()
        if (!checkInternetConnection(requireActivity().applicationContext)) {
            displayNoInternetConnection()
        }
    }

    override fun onStop() {
        navigationHeader.visibility = View.GONE
        navigationHeaderTitle.text = ""
        navigationHeaderPeriod.text = ""
        navigationHeaderNameOfService.text = ""
        super.onStop()
    }



    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.denied))
            .setMessage(getString(R.string.permission_denied))
            .setPositiveButton(
                getString(R.string.app_settings)
            ) { _, _ ->
                // send to app settings if permission is denied permanently
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun displayNoInternetConnection() {
        MaterialDialog(requireContext()).show {
            cancelOnTouchOutside(true)
            title(R.string.no_internet_title)
            message(R.string.no_internet_message)
        }
    }
}
