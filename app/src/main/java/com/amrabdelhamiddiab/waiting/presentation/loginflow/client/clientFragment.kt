package com.amrabdelhamiddiab.waiting.presentation.loginflow.client

import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.TextView
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
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.MyFirebaseMessagingService
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentClientBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class clientFragment : Fragment() {
    private lateinit var navigationView: NavigationView
    private lateinit var navigationHeader: View
    private lateinit var navigationHeaderTitle: TextView
    private lateinit var navigationHeaderNameOfService: TextView
    private lateinit var navigationHeaderPeriod: TextView
    private var myService: Service = Service("", "", "", 0)
    private val viewModel by viewModels<ClientViewModel>()
    private lateinit var binding: FragmentClientBinding
    private var orderNumber: Int = 0

    var order: Order? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client, container, false)

        navigationView = requireActivity().findViewById(R.id.navigation_view)
        navigationHeader = navigationView.getHeaderView(0)
        navigationHeaderTitle = navigationHeader.findViewById(R.id.textView_category_nav_header)
        navigationHeaderPeriod =
            navigationHeader.findViewById(R.id.text_view_peiod_for_each_visitor_nav_header)
        navigationHeaderNameOfService =
            navigationHeader.findViewById(R.id.textView_name_of_service_nav_header)


        val userId = viewModel.retrieveUserIdFromPreferences()
        viewModel.notifyWhenOrderChange(userId)
        viewModel.downloadServiceV(userId)
        viewModel.retrieveClientNumberFromPreferences()
        binding.buttonEndVisit.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialogAreYouSure()
            } else {
                displayNoInternetConnection()
            }
        }
        binding.textViewMyNumber.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialog()
            } else {
                displayNoInternetConnection()
            }
        }
        viewModel.myNumber.observe(viewLifecycleOwner) {
            (getString(R.string.my_number) + " " + it.toString()).also { it1 ->
                binding.textViewMyNumber.text = it1
            }
        }

        viewModel.tokenUploaded.observe(viewLifecycleOwner){
            if (it == true){
                viewModel.sayIfClientIsInAVisit(true)
            }
        }

        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null) {
                myService = it
                navigationHeaderTitle.text = myService.category
                val text = it.period_per_each_service
                (getString(R.string.about)+" " + text +" " + getString(R.string.minuits_for_each_visit)).also { it1 -> navigationHeaderPeriod.text = it1 }

                navigationHeaderNameOfService.text = myService.name_of_service
            } else {
                displayDialogWrongQrcode()
            }
        }


        viewModel.orderValue.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.textViewOrder.text = "0"
            } else {
                binding.textViewOrder.text = it.order.toString()
                orderNumber = it.order.toInt()
            }
        }

        viewModel.clientTokenRemoved.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_clientFragment_to_homeFragment)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_client, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        if (checkInternetConnection(requireActivity().applicationContext)) {
                            displayDialog()
                        } else {
                            displayNoInternetConnection()
                        }
                        true
                    }
                  else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun displayDialog() {
        var myValue: CharSequence = ""
        MaterialDialog(requireContext()).show {
            val input = input(
                hint = getString(R.string.enter_your_number_here),
                allowEmpty = false,
                maxLength = 3,
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { _, myNumber ->
                myValue = myNumber
            }
            positiveButton(R.string.add) {
                if (myValue.isNotEmpty()) {
                    val myValueInt = myValue.toString().toInt()
                    if (myValueInt > orderNumber) {
                        viewModel.saveMyNumberInPreferences(myValue.toString().toInt())
                        viewModel.uploadMyClientToken(Token(MyFirebaseMessagingService.token.toString(),myValueInt ) )
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.invalid_number), Toast.LENGTH_SHORT)
                            .show()
                    }
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
            title(R.string.end_visit)
            message(R.string.are_you_sure)
            positiveButton(R.string.yes) {
                viewModel.saveMyNumberInPreferences(0)
                viewModel.sayIfClientIsInAVisit(false)
                viewModel.removeClientTokenV()
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun displayDialogWrongQrcode() {
        MaterialDialog(requireContext()).show {
            title(R.string.qrcode_fake_title)
            message(R.string.qrcode_fake_message)
            positiveButton(R.string.ok) {
                viewModel.sayIfClientIsInAVisit(false)
                findNavController().navigate(R.id.action_clientFragment_to_homeFragment)
            }
        }
    }

    private fun displayNoInternetConnection() {
        MaterialDialog(requireContext()).show {
            cancelOnTouchOutside(true)
            title(R.string.no_internet_title)
            message(R.string.no_internet_message)
        }
    }

    override fun onResume() {
        super.onResume()
        navigationHeader.visibility = View.VISIBLE
    }

    override fun onStop() {
        navigationHeader.visibility = View.GONE
        navigationHeaderTitle.text = ""
        navigationHeaderPeriod.text = ""
        navigationHeaderNameOfService.text = ""
        super.onStop()
    }
}
