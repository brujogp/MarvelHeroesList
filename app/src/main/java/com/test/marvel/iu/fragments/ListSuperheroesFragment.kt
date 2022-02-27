package com.test.marvel.iu.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var binding: FragmentListSuperheroesBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentListSuperheroesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.binding!!.let {

        }
    }

    override fun onStart() {
        super.onStart()
        this.viewModel.getCharacters().observe(viewLifecycleOwner, onCharacterReceived)
    }

    private val onCharacterReceived = Observer<Status> {
        when (it) {
            is Status.Loading -> {
                Log.d(TAG, "Loading")
            }
            is Status.SuccessfulResponse<*> -> {
                val data = (it.body as CharactersResponse).data.results
                configHeroesList(data)
            }
            is Status.Error -> {
                Log.d(TAG, it.message)
            }
            else -> {}
        }
    }

    private fun configHeroesList(charactersResponse: List<Result>) {
        val adapterHeroesList = ListHeroesAdapter(requireContext(), charactersResponse as MutableList<Result>)

        this.binding!!.rvHeroesList.adapter = adapterHeroesList
        this.binding!!.rvHeroesList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding = null
    }
}