package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    private lateinit var electionsViewModel: ElectionsViewModel
    private lateinit var electionsViewModelFactory: ElectionsViewModelFactory

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding  = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner =this

        electionsViewModelFactory = ElectionsViewModelFactory(requireActivity().application)

        electionsViewModel = ViewModelProvider(this , electionsViewModelFactory).get(ElectionsViewModel::class.java)

        binding.viewModel = electionsViewModel

        binding.upcomingElectionRecycleView.adapter =
                ElectionListAdapter(ElectionListAdapter.ElectionListener{

                    electionsViewModel.displayVoterInfo(it)
        })

        binding.savedElectionRecycleView.adapter =
                ElectionListAdapter(ElectionListAdapter.ElectionListener{
                    electionsViewModel.displayVoterInfo(it)
                })

        electionsViewModel.navigateToVoterInfo.observe(viewLifecycleOwner , Observer {
            if(null != it){
                this.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id,it.division))
                electionsViewModel.displayVoterInfoComplete()
            }
        })
        return binding.root
    }

}