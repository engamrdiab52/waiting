package com.amrabdelhamiddiab.waiting.presentation.loginflow.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.core.domain.Constants.EMAIL
import com.amrabdelhamiddiab.core.domain.Constants.FACEBOOK
import com.amrabdelhamiddiab.core.domain.Constants.GOOGLE
import com.amrabdelhamiddiab.core.domain.Constants.PUBLIC_PROFILE
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentLoginBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.toast
import com.amrabdelhamiddiab.waiting.presentation.loginflow.LoginFlowViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginFlowViewModel by hiltNavGraphViewModels(R.id.nested_graph_login)
    private lateinit var binding: FragmentLoginBinding
    private var validPassword: Boolean = false
    private var validEmail: Boolean = false

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var iPreferenceHelper: IPreferenceHelper
    private lateinit var callbackManager: CallbackManager

    //Google Sign in
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (accountTask.isSuccessful) {
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
        //Firebase Generally
        firebaseAuth = viewModel.mAuth

        //Initialize Google Sign in
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        //--------------------------------------------------------

        //Initialize facebook login
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d(TAG, "-----------------------facebook:onSuccess:$result")
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
        //-----------------------------------------------

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.buttonFacebookLogin.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                requireActivity(),
                callbackManager,
                listOf(EMAIL, PUBLIC_PROFILE)
            )
        }
        binding.googleSignInButton.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            //startActivityForResult(intent, RC_SIGN_IN)
            launcher.launch(intent)
        }
        iPreferenceHelper = viewModel.preferenceHelper
        viewModel.downloading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
            }
        }
        viewModel.userSignedIn.observe(viewLifecycleOwner) {
            if (it == true) {
                when (iPreferenceHelper.loadSignInMethode()) {
                    EMAIL -> {
                        viewModel.isEmailVerified()
                    }
                    GOOGLE -> {
                        viewModel.downloadServiceV()
                    }
                    FACEBOOK->{
                        viewModel.downloadServiceV()
                    }
                    else -> requireContext().toast("Error in Sign in")
                }
            } else {
                requireContext().toast("Error in Sign in")
            }
        }
        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.action_global_serviceFragment)
            } else {
                findNavController().navigate(R.id.action_global_createServiceFragment)
            }
        }
        viewModel.emailVerified.observe(viewLifecycleOwner) {
            if (it == true) {
                iPreferenceHelper.saveSignInMethode(EMAIL)
                viewModel.downloadServiceV()
            }
        }
        binding.buttonLogin.setOnClickListener {
            validateEmailField()
            validatePasswordField()
            if (validEmail && validPassword) {
                val email = binding.editTextLoginEmail.text.toString()
                val password = binding.editTextLoginPassword.text.toString()
                viewModel.signIn(email, password)
            } else {
                requireContext().toast(getString(R.string.invalid_credentials))
            }
        }
        binding.tvLoginForgetPasswordClickable.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
        binding.tvLoginSignupClickable.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        return binding.root
    }
        //Facebook
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //login success
                    iPreferenceHelper.saveSignInMethode(FACEBOOK)
                    viewModel.downloadServiceV()
                } else {
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
        //Google
    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credentials = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credentials).addOnSuccessListener { authResult ->
            //login success
            iPreferenceHelper.saveSignInMethode(GOOGLE)
            viewModel.downloadServiceV()

            //check if user new or existing
            if (authResult.additionalUserInfo!!.isNewUser) {
                //user is new -account created
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created...")
                Toast.makeText(requireContext(), "Account created", Toast.LENGTH_SHORT)
                    .show()
            } else {

                Toast.makeText(requireContext(), "Logged in", Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "LoggedIn Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
        //Email
    private fun validateEmailField() {
        binding.editTextLoginEmail.validator().nonEmpty().validEmail().addErrorCallback {
            validEmail = false
            binding.textLayoutLoginEmail.error = it
            Log.d(TAG, it)
        }.addSuccessCallback {
            validEmail = true
            binding.textLayoutLoginEmail.error = null
        }.check()
    }
        //Email
    private fun validatePasswordField() {
        binding.editTextLoginPassword.validator().nonEmpty().addErrorCallback {
            validPassword = false
            binding.textLayoutLoginPassword.error = it
            Log.d(TAG, it)
        }.addSuccessCallback {
            validPassword = true
            binding.textLayoutLoginPassword.error = null
        }.check()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setDrawerLocked()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setDrawerUnlocked()
    }

    companion object {
        private const val RC_SIGN_IN = 5
    }
}