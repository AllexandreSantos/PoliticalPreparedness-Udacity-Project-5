package com.allexandresantos.politicalpreparedness.representative.adapter

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.allexandresantos.politicalpreparedness.R
import com.allexandresantos.politicalpreparedness.data.models.State
import com.allexandresantos.politicalpreparedness.util.fadeIn
import com.allexandresantos.politicalpreparedness.util.fadeOut
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        Log.d("oi", "fetchImage: " + src)
        val uri = src.toUri().buildUpon().scheme("https").build()
        Glide.with(view.context)
                .load(uri)
                .circleCrop()
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.ic_profile)
                                .error(R.drawable.ic_profile)
                )
                .into(view)
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }

}


inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T>{
    return adapter as ArrayAdapter<T>
}
