package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.ElectionListStatus
import com.example.android.politicalpreparedness.network.models.Election


@BindingAdapter("listElection")
fun bindElectionRecycleView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("followButtonState")
fun  bindSaveButton(textView: TextView , data:Boolean){
    if(data){
        textView.text ="UNFOLLW ELECTION"
    }else{
        textView.text ="FOLLOW ELECTION"

    }
}

@BindingAdapter("listStatus")
fun bindListStatus(imageView:ImageView,electionListStatus: ElectionListStatus?){
    when(electionListStatus){
ElectionListStatus.LOADING-> imageView.setImageResource(R.drawable.loading_animation)
        ElectionListStatus.DONE ->imageView.visibility = View.INVISIBLE
        ElectionListStatus.ERROR ->imageView.setImageResource(R.drawable.ic_connection_error)
    }
}