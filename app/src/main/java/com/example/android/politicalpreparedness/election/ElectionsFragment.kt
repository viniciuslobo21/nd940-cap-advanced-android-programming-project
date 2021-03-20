package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding
    private lateinit var savedElectionsAdapter: ElectionListAdapter
    private lateinit var upcomingElectionsListAdapter: ElectionListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentElectionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.electionsViewModel = viewModel

        initializeUpcomingAdapter()
        initializeSavedElectionsAdapter()
        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner, { election ->
            if (election != null) {
                this.findNavController().navigate(
                        ElectionsFragmentDirections.toVoterInfo(election)
                )
                viewModel.navigationToVoterInfoComplete()
            }
        })
    }

    private fun initializeSavedElectionsAdapter() {
        savedElectionsAdapter = ElectionListAdapter("Saved Elections") {
            viewModel.onClick(it)
        }

        binding.savedElectionsList.adapter = savedElectionsAdapter

        viewModel.savedElections.observe(viewLifecycleOwner, { saved ->
            saved?.apply {
                savedElectionsAdapter.addHeaderAndSubmitList(saved)
            }
        })
    }

    private fun initializeUpcomingAdapter() {

        upcomingElectionsListAdapter = ElectionListAdapter("Upcoming Elections") {
            viewModel.onClick(it)
        }

        binding.upcomingElectionsList.adapter = upcomingElectionsListAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, { upcomingElections ->
            upcomingElections?.apply {
                upcomingElectionsListAdapter.addHeaderAndSubmitList(upcomingElections)
            }
        })
    }
}