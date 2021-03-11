package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val electionID = VoterInfoFragmentArgs.fromBundle(arguments!!).argElectionId
        val electionDivision = VoterInfoFragmentArgs.fromBundle(arguments!!).argDivision
        val viewModelFactory = VoterInfoViewModelFactory(requireActivity().application ,electionID ,electionDivision)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
        binding.viewModel = viewModel

        binding.stateLocations.setOnClickListener {
            startUrl(viewModel.voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl)
        }
        binding.stateBallot.setOnClickListener {
            startUrl(viewModel.voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl)
        }

        binding.saveElectionBtn.setOnClickListener {
               viewModel.handleState()
        }
        return binding.root
    }
   private fun startUrl(url :String?){
        var i  = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

}