package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class VoterInfoFragment : Fragment() {
    private val args by navArgs<VoterInfoFragmentArgs>()
    private lateinit var binding: FragmentVoterInfoBinding
    private val viewModel: VoterInfoViewModel by viewModel(
            clazz = VoterInfoViewModel::class,
            qualifier = null,
            parameters = { parametersOf(args.argElection) }
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.election = args.argElection

        setObservables()

        return binding.root
    }

    private fun setObservables() {
        viewModel.errorOnFetchingNetworkData.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(
                        activity,
                        R.string.network_error,
                        Toast.LENGTH_LONG
                ).show()
                viewModel.displayNetworkErrorComplete()
            }
        })

        viewModel.ballotInfoUrl.observe(viewLifecycleOwner, { url ->
            url?.let {
                openBrowser(url)
                viewModel.onOpenBallotInfoUrlComplete()
            }
        })

        viewModel.votingLocationFinderUrl.observe(viewLifecycleOwner, { url ->
            url?.let {
                openBrowser(url)
                viewModel.onOpenVotingLocationsUrlComplete()
            }
        })

        viewModel.electionInfoUrl.observe(viewLifecycleOwner, { url ->
            url?.let {
                openBrowser(url)
                viewModel.onOpenElectionInformationUrlComplete()
            }
        })
    }

    private fun openBrowser(url: String?) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}
