package com.allexandresantos.politicalpreparedness.util

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.allexandresantos.politicalpreparedness.BuildConfig
import com.allexandresantos.politicalpreparedness.R
import com.allexandresantos.politicalpreparedness.data.models.Address
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun hideKeyboard(activity: FragmentActivity, view: View) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun provideLocationPermissionRationale(view: View) {
    Snackbar.make(view, R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.settings) {
                view.context.startActivity(
                        Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
            }.show()
}

fun geoCodeLocation(location: Location, context: Context): Address {
    val geocoder = Geocoder(context, Locale.getDefault())
    return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(address.thoroughfare?: "", address.subThoroughfare?: "", address.locality?: "", address.adminArea?: "", address.postalCode?: "")
            }.first()

}