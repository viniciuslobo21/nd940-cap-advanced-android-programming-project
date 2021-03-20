package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class RepresentativeFragment : Fragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1001
    }

    private lateinit var binding: FragmentRepresentativeBinding
    val viewModel: RepresentativeViewModel by viewModel()

    private var mLastKnownLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermissions()

        binding.representativeList.adapter = RepresentativeListAdapter()

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.onClickFindMyRepresentatives()
        }

        binding.useMyLocationButton.setOnClickListener {
            hideKeyboard()
            getRepresentatives()
        }

        return binding.root
    }

    private fun getRepresentatives() {
        if (mLastKnownLocation == null) {
            getmLastKnownLocation()
        }
        val address = mLastKnownLocation?.let { mlk -> geoCodeLocation(mlk) }
        address?.let { ad -> viewModel.onClickUseMyLocation(ad) }
    }

    private fun getmLastKnownLocation() {
        getLocation()
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (grantResults.isEmpty() ||
                grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showSnack(
                    requireActivity().findViewById(android.R.id.content),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_LONG
            )
            setupStateButton(false)
        } else {
            setupStateButton(true)
            getLocation()
        }
    }

    private fun setupStateButton(isEnabled: Boolean) {
        binding.useMyLocationButton.isEnabled = isEnabled
    }

    private fun showSnack(view: View, message: Int, duration: Int) {
        Snackbar.make(view, message, duration).setAction(R.string.settings) {
            showSettingsScreen()
        }.show()
    }

    private fun showSettingsScreen() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
        activity?.finish()
    }

    private fun checkLocationPermissions() {
        if (isPermissionGranted()) {
            setupStateButton(true)
            getLocation()
        } else {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                task.result?.let {
                    mLastKnownLocation = it
                }
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(
                            address.thoroughfare,
                            address.subThoroughfare,
                            address.locality,
                            address.adminArea,
                            address.postalCode
                    )
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}