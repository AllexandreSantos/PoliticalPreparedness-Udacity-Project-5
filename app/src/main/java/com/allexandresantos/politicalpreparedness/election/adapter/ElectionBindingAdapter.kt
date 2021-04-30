package com.allexandresantos.politicalpreparedness.election.adapter

import android.text.method.TextKeyListener.clear
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.util.Event
import com.allexandresantos.politicalpreparedness.util.fadeIn
import com.allexandresantos.politicalpreparedness.util.fadeOut


@BindingAdapter("electionList")
fun <T> bindElectionRecyclerView(recyclerView: RecyclerView, items: LiveData<List<T>>?) {
    items?.value?.let { list ->
        (recyclerView.adapter as? ElectionListAdapter)?.apply {
            submitList(list as List<Election>)
            notifyDataSetChanged()
        }
    }
}

@BindingAdapter("android:fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()
        if (visible == true) {
            if (view.visibility == View.GONE)
                view.fadeIn()
        } else {
            if (view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }
}
