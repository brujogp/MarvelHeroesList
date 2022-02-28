package com.test.marvel.iu.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.marvel.R
import com.test.marvel.data.models.CharactersResponse
import com.test.marvel.data.models.Result
import com.test.marvel.data.models.Status
import com.test.marvel.databinding.FragmentListSuperheroesBinding
import com.test.marvel.iu.adapters.ListHeroesAdapter
import com.test.marvel.iu.viewmodels.MainViewModel

class ListSuperheroesFragment : Fragment() {
    companion object {
        const val TAG = "ListSuperheroesFragment"
    }

    private lateinit var temporalHeroesList: MutableList<Result>
    private var requireAddNewItems: Boolean = false
    private var offsetResults = 0

    private var totalHeroes = mutableListOf<Result>()

    private var binding: FragmentListSuperheroesBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

    private val loadNewHeroesMutableLiveData = MutableLiveData<Boolean>()
    private val onHeroClick = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentListSuperheroesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.onHeroClick.observe(viewLifecycleOwner) {
            this.viewModel.heroSelected(this@ListSuperheroesFragment.totalHeroes[it])
            findNavController().navigate(R.id.action_listSuperheroesFragment_to_heroDetailsFragment)
        }

        this.loadNewHeroesMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                this@ListSuperheroesFragment.offsetResults += 100
                makeRequestListHeroes(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        makeRequestListHeroes()
    }

    private fun makeRequestListHeroes(requireAddNewItems: Boolean = false) {
        this.requireAddNewItems = requireAddNewItems

        this.viewModel.getCharacters(this.offsetResults.toString())
            .observe(viewLifecycleOwner, onCharacterReceived)
    }

    private val onCharacterReceived = Observer<Status> {
        when (it) {
            is Status.Loading -> {
                this@ListSuperheroesFragment.binding!!.progressBar.visibility = View.VISIBLE
            }
            is Status.SuccessfulResponse<*> -> {
                this@ListSuperheroesFragment.binding!!.progressBar.visibility = View.GONE

                val data = (it.body as CharactersResponse).data.results

                this.temporalHeroesList = data
                this.totalHeroes.addAll(this.temporalHeroesList)

                configHeroesList(this.totalHeroes)
            }
            is Status.Error -> {
                this@ListSuperheroesFragment.binding!!.progressBar.visibility = View.GONE
                Log.d(TAG, it.message)
            }
            else -> {}
        }
    }

    private fun configHeroesList(charactersResponse: List<Result>) {
        val adapterHeroesList =
            ListHeroesAdapter(
                requireContext(),
                charactersResponse as MutableList<Result>,
                this.loadNewHeroesMutableLiveData,
                this.onHeroClick
            )

        if (this.requireAddNewItems) {
            adapterHeroesList.notifyItemRangeChanged(
                this@ListSuperheroesFragment.totalHeroes.size - this@ListSuperheroesFragment.temporalHeroesList.size,
                (this@ListSuperheroesFragment.totalHeroes.size - 1)
            )
        } else {
            this.binding!!.rvHeroesList.adapter = adapterHeroesList
            this.binding!!.rvHeroesList.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding = null
    }
}