package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemListBinding
import com.example.android.politicalpreparedness.network.models.Election
import com.google.android.gms.common.server.response.SafeParcelResponse.from
import com.google.android.material.transformation.ExpandableBehavior.from

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {
    class ElectionViewHolder(private val binding:ElectionItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(election: Election){
            binding.election = election
            binding.executePendingBindings()
        }
    }

    class ElectionListener(private val clickListener :(election:Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
    }

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>(){
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return  oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder(ElectionItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(election)
        }
        holder.bind(election)
    }
}
