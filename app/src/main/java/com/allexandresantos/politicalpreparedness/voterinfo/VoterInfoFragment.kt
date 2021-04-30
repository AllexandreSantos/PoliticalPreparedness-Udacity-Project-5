package com.allexandresantos.politicalpreparedness.voterinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.allexandresantos.politicalpreparedness.R
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.allexandresantos.politicalpreparedness.voterinfo.VoterInfoViewModel.ButtonState
import org.koin.androidx.viewmodel.ext.android.viewModel


class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding

    private val  viewModel: VoterInfoViewModel by viewModel()

    private val args: VoterInfoFragmentArgs? by navArgs()

    private var election: Election? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentVoterInfoBinding.inflate(inflater)

        binding.lifecycleOwner = this

        getArgs()

        initViews()

        observeViewModel()

        return binding.root
    }

    private fun getArgs() {
        election = args?.election
        viewModel.initElection(election)
    }

    private fun initViews() {
        with(binding){
            election?.let {
                electionName.title = it.name
                electionDate.text = it.electionDay.toString()
            }
            saveUnsaveElectionButton.setOnClickListener {viewModel.saveUnsaveElection()}
            stateLocations.setOnClickListener { viewModel.goToVotingLocations()}
            stateBallot.setOnClickListener{viewModel.goToBallotInformation()}
        }
    }

    private fun observeViewModel() {
        viewModel.buttonState.observe(viewLifecycleOwner){ buttonState ->
            when(buttonState){
                ButtonState.UNSAVE -> binding.saveUnsaveElectionButton.text = getString(R.string.unsave_election)
                else -> binding.saveUnsaveElectionButton.text = getString(R.string.save_election)
            }
        }

        viewModel.webActionUrl.observe(viewLifecycleOwner){ event ->
            event?.getContentIfNotHandled()?.let { webActionUrl ->
                goToUrl(webActionUrl)
            }
        }

        viewModel.isVoterInfoReady.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    binding.stateBallot.visibility = View.VISIBLE
                    binding.stateLocations.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun goToUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    companion object{
        private val TAG = VoterInfoFragment::class.java.simpleName
    }

}