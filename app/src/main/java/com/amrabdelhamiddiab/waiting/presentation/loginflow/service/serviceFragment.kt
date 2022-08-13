package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.amrabdelhamiddiab.core.domain.NotificationData
import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class serviceFragment : Fragment() {
    private var passwordForDeleteAccount: String = ""
    private lateinit var binding: FragmentServiceBinding
    var fromButton: Boolean = false
    private val viewModel by viewModels<ServiceViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  viewModel.downloadTokenV()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)

        viewModel.notifyWhenServiceChange()
        viewModel.notifyWhenOrderChange()
        viewModel.notifyWhenListOfTokensChanged()

        viewModel.downloading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingIndecator.visibility = View.VISIBLE
            } else {
                binding.loadingIndecator.visibility = View.GONE
            }
        }
        viewModel.service.observe(viewLifecycleOwner) {
            binding.textViewCategory.text = it?.category ?: ""
            binding.textViewNameOfService.text = it?.name_of_service ?: ""
            if (it != null) {
                binding.textViewPeriodPerEachService.text = it.period_per_each_service.toString()
            } else {
                binding.textViewPeriodPerEachService.text = "0"
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
                            PushNotification(
                                NotificationData(
                                    "Current Serving Number",
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
        viewModel.dataBaseError.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }
        //*****************************
        viewModel.userSignedOut.observe(viewLifecycleOwner) {
            if (it == true) {
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

        binding.buttonServiceIncrementOrder.setOnClickListener {
            fromButton = true
            val currentOrderValueFromTextView = binding.textViewCurrentNumber.text.toString()
            viewModel.incrementCurrentOrderValue(currentOrderValueFromTextView)
            Log.d(
                TAG,
                "binding.buttonServiceIncrementOrder........................." + viewModel.listOfDownloadedTokens.value.toString()
            )
        }
        binding.buttonServiceDecreaseOrder.setOnClickListener {
            fromButton = true
            val currentOrderValueFromTextView = binding.textViewCurrentNumber.text.toString()
            viewModel.decrementCurrentOrderValue(currentOrderValueFromTextView)
        }

        binding.buttonEndThisDay.setOnClickListener {
            viewModel.deleteThisDayV()
        }

        //********************************************************************************

        binding.buttonLogout.setOnClickListener {
            displayDialogLogOut()
        }
        //********************************************************************************
        binding.buttonServiceDeleteAccount.setOnClickListener {
            displayDialogDeleteAccount()

        }
        //*******************************************************************************
        binding.buttonQrcode.setOnClickListener {
            findNavController().navigate(R.id.action_serviceFragment_to_QRcodeFragment)
        }
        //*******************************************************************************
        binding.buttonEditService.setOnClickListener {
            findNavController().navigate(R.id.action_serviceFragment_to_createServiceFragment)
        }
        //*******************************************************************************
        binding.buttonServiceEditOrder.setOnClickListener {
            displayDialogEditOrder()
        }
        binding.buttonServiceResetOrder.setOnClickListener {
            fromButton = true
            binding.textViewCurrentNumber.text = "0"
            viewModel.changeOrderValueV(0L)
        }
        viewModel.userDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
        }
        viewModel.serviceDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                if (passwordForDeleteAccount.isNotEmpty()) {
                    viewModel.deleteAccountV(passwordForDeleteAccount)
                }
            }
        }

        //***********************************************************************************
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_edit, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.menu_edit -> {
                        Toast.makeText(requireContext(), "EDIT", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
    /*   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
           inflater.inflate(R.menu.menu_edit, menu)
       }*/


    /*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menu_edit -> {
                    Toast(requireContext(), "EDIT", Toast.LENGTH_SHORT).show()
                    true
                }
                *//*     R.id.menu_add -> {
                     viewModel.counterForFakeTime++
                     if (viewModel.counterForFakeTime >= 29) {
                         viewModel.fakeTime -= (28 * DAY)
                         viewModel.counterForFakeTime = 0
                     }
                     viewModel.fakeTime += DAY
                     Log.d(TAG, "${viewModel.fakeTime} +  ${viewModel.counterForFakeTime}")
                     adapter.run { notifyDataSetChanged() }
                     true
                 }*//*
            else -> super.onOptionsItemSelected(item)
        }
    }*/
    private fun displayDialogDeleteAccount() {
        var myValue: CharSequence = ""
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_delete_account_title)
            message(R.string.dialog_delete_account_message)
            val input = input(
                hint = "Enter Your Password here",
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

    private fun displayDialogEditOrder() {
        var myValue: CharSequence = ""
        MaterialDialog(requireContext()).show {
            val input = input(
                hint = "Enter current number here",
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
                Log.d(TAG, "Logged out ")
                viewModel.signOut()
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun displayNoInternerConnection() {
        MaterialDialog(requireContext()).show {
            cancelOnTouchOutside(true)
            title(R.string.no_internet_title)
            message(R.string.no_internet_message)
        }
    }
}
