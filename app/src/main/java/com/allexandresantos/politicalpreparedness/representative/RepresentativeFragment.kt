package com.allexandresantos.politicalpreparedness.representative

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.allexandresantos.politicalpreparedness.data.models.Address
import com.allexandresantos.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.allexandresantos.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.allexandresantos.politicalpreparedness.util.geoCodeLocation
import com.allexandresantos.politicalpreparedness.util.hideKeyboard
import com.allexandresantos.politicalpreparedness.util.provideLocationPermissionRationale
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_representative.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    private val representativesViewModel: RepresentativeViewModel by viewModel()
    private lateinit var representativesAdapter: RepresentativeListAdapter

    private val getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted)
            launchGetLocationFlow()
        else
            provideLocationPermissionRationale(binding.motionLayout)
    }

    private val getTurnDeviceLocationOn = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ activityResult ->
        if (activityResult.resultCode == RESULT_OK)
            getLocation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = representativesViewModel

        initViews()

        observeViewModel()

        return binding.root
    }

    private fun initViews() {
        binding.locationButton.setOnClickListener {
            hideKeyboard(requireActivity(), it)
            launchGetLocationFlow()
        }

        binding.searchButton.setOnClickListener {
            with(binding){
                val address = Address(addressLine1.text.toString(), addressLine2.text.toString(), city.text.toString(), state.selectedItem.toString(), zip.text.toString())
                representativesViewModel.launchFindRepresentativesFlow(address)
            }
            hideKeyboard(requireActivity(), it)
        }

        representativesAdapter = RepresentativeListAdapter()
        binding.representativesRecyclerList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = representativesAdapter
        }
    }

    private fun observeViewModel() {
        representativesViewModel.error.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let {
                showErrorMessage(it)
            }
        }

        representativesViewModel.representativesList.observe(viewLifecycleOwner){
            representativesAdapter.apply {
                submitList(it)
            }
        }
    }

    private fun launchGetLocationFlow() {
        isPermissionGranted()
        if (isPermissionGranted()) {
            checkDeviceLocationSettings()
        }
        else
            getPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun isPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun checkDeviceLocationSettings(resolve: Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(requireContext())

        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve)
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution.intentSender).build()
                    getTurnDeviceLocationOn.launch(intentSenderRequest)
                }
                catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(TAG, "Error geting location settings resolution: " + sendEx.message)
                }
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful)
                getLocation()
        }

    }

    private fun getLocation() {
        try {
            val locationResult = LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation

            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val lastKnownLocation: Location? = task.result
                    if (lastKnownLocation != null)
                        bindAddress(geoCodeLocation(lastKnownLocation, requireContext()))
                }
            }

        }
        catch (e: SecurityException) {
            Log.e(TAG, e.message, e)
        }
    }

    private fun bindAddress(address: Address) {
        representativesViewModel.setSearchAddress(address)
    }

    private fun showErrorMessage(message: Int) = Snackbar.make(motionLayout, getString(message), Snackbar.LENGTH_SHORT).show()

    companion object {
        private val TAG = DetailFragment::class.java.simpleName
    }
}