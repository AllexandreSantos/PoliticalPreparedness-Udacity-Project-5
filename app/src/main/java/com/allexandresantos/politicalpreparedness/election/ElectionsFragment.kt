package com.allexandresantos.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.allexandresantos.politicalpreparedness.data.models.Election
import com.allexandresantos.politicalpreparedness.databinding.FragmentElectionBinding
import com.allexandresantos.politicalpreparedness.election.adapter.ElectionListAdapter
import com.allexandresantos.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding
    private lateinit var networkElectionsAdapter: ElectionListAdapter
    private lateinit var savedElectionsAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initRecyclers()
        initViewModelFunctions()
        observeViewModel()

        return binding.root
    }

    private fun initRecyclers() {
        networkElectionsAdapter = ElectionListAdapter(ElectionListener {
            navigateToVoterInfo(it)
        })
        savedElectionsAdapter = ElectionListAdapter(ElectionListener {
            navigateToVoterInfo(it)
        })

        with(binding) {

            upcomingElectionsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = networkElectionsAdapter
            }

            savedElectionsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = savedElectionsAdapter
            }
        }
    }

    private fun initViewModelFunctions() {
        viewModel.apply {
            loadNetworkElections()
            loadSavedElections()
        }
    }

    private fun observeViewModel() {
        viewModel.networkElectionsList.observe(viewLifecycleOwner) {
            networkElectionsAdapter.apply {
                submitList(it)
            }
        }
        viewModel.savedElectionsList.observe(viewLifecycleOwner) {
            savedElectionsAdapter.apply {
                submitList(it)
            }
        }
    }

    fun navigateToVoterInfo(election: Election) {
        this.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election))
    }

    companion object {
        private val TAG = ElectionsFragment::class.java.simpleName
    }

}