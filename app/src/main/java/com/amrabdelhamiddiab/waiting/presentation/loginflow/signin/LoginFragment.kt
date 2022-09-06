package com.amrabdelhamiddiab.waiting.presentation.loginflow.signin

import android.content.Intent
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
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.ProfileActivity
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentLoginBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.toast
import com.amrabdelhamiddiab.waiting.presentation.loginflow.LoginFlowViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
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
            }else {
                Log.d(TAG, "onActivityResult: accountTask.isNOTSuccessful: ")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize Google Sign in
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        firebaseAuth = viewModel.retrieveFirebaseAuth()
        //    checkUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        iPreferenceHelper = viewModel.preferenceHelper
        viewModel.downloading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingIndecatorLogin.visibility = View.VISIBLE
            } else {
                binding.loadingIndecatorLogin.visibility = View.GONE
            }
        }
        viewModel.userSignedIn.observe(viewLifecycleOwner) {
            if (it == true) {
                when(iPreferenceHelper.loadSignInMethode()){
                    "email" -> {viewModel.isEmailVerified()}
                    "google" ->{viewModel.downloadServiceV()}
                    else -> requireContext().toast("Error in Sign in")
                }
            }else {
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
                iPreferenceHelper.saveSignInMethode("email")
                viewModel.downloadServiceV()
            }
        }
//configure the google sign in
        binding.googleSignInButton.setOnClickListener {
            Log.d(TAG, "onCreate: begin google sign in")
            val intent = googleSignInClient.signInIntent
            //startActivityForResult(intent, RC_SIGN_IN)
            launcher.launch(intent)
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
        // Inflate the layout for this fragment
        Log.d(TAG, FirebaseAuth.getInstance().currentUser?.email.toString())
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credentials = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credentials).addOnSuccessListener { authResult ->
            //login success
            iPreferenceHelper.saveSignInMethode("google")
            viewModel.downloadServiceV()
            Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

            //get loggedIn user
            val firebaseUser = firebaseAuth.currentUser

            //get user info
            val uid = firebaseUser!!.uid
            val email = firebaseUser.email
            Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid = $uid")
            Log.d(TAG, "firebaseAuthWithGoogleAccount: Email = $email")

            //check if user new or existing
            if (authResult.additionalUserInfo!!.isNewUser) {
                //user is new -account created
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created... \n$email")
                Toast.makeText(requireContext(), "Account created...\n$email", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //existing user -- logged in
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing User... \n$email")
                Toast.makeText(requireContext(), "Logged in...\n$email", Toast.LENGTH_SHORT).show()
            }
            /*  // start profile activity
              val intent = Intent(requireContext(), ProfileActivity::class.java)
              intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
              startActivity(intent)
              requireActivity().finish()*/
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

    /* private fun checkUser() {
 //check if user loggedin or not
         val firebaseUser = firebaseAuth.currentUser
         if (firebaseUser != null) {
             viewModel.downloadServiceV()
             //user is already logged in
             *//*val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
            Log.d(MainActivity.TAG, "checkUser: user already logged in ${firebaseUser.email}")*//*
        }
    }*/

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