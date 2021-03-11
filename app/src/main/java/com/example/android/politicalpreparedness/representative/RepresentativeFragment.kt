package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import kotlinx.android.synthetic.main.fragment_representative.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

private const val TAG = "RepresentativeFragment"

class RepresentativeFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var representativeViewModel: RepresentativeViewModel
    private lateinit var representativesViewModelFactory: RepresentativesViewModelFactory
    private lateinit var adapter: RepresentativeListAdapter

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        representativesViewModelFactory = RepresentativesViewModelFactory(requireActivity().application)
        representativeViewModel = ViewModelProvider(this, representativesViewModelFactory).get(RepresentativeViewModel::class.java)
        binding.viewModel = representativeViewModel
        adapter = RepresentativeListAdapter()
        binding.representativesRecycleView.adapter = adapter
        binding.motionLayoutRepresentative.setTransition(R.id.start, R.id.start)

        binding.buttonLocation.setOnClickListener {
            Log.i(TAG, checkLocationPermissions().toString())
            if (checkLocationPermissions()) {


                getLocation()
                hideKeyboard()
                binding.motionLayoutRepresentative.setTransition(R.id.start, R.id.end)

            }
        }
        binding.buttonSearch.setOnClickListener {
            val address = Address(binding.addressLine1.text.toString(), "",
                    binding.city.text.toString(),
                    binding.state.selectedItem.toString(),
                    binding.zip.text.toString())
            representativeViewModel.getAddress(address)
            representativeViewModel.getRepresentativeList()
            hideKeyboard()
            binding.motionLayoutRepresentative.setTransition(R.id.start, R.id.end)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        representativeViewModel.representative.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, it.size.toString())
            adapter.submitList(it)


        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[REQUEST_LOCATION_PERMISSION] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(activity, "You should allow location", Toast.LENGTH_LONG).show()
        } else {
            getLocation()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                representativeViewModel.getAddress(geoCodeLocation(location))
                representativeViewModel.getRepresentativeList()
                Log.i(TAG, representativeViewModel.address.toString())
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}