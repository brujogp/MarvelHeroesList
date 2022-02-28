package com.test.marvel.iu.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.test.marvel.R
import com.test.marvel.data.models.Result
import com.test.marvel.data.models.Status
import com.test.marvel.data.models.comics.ComicsResponse
import com.test.marvel.databinding.FragmentDetailsHeroBinding
import com.test.marvel.iu.adapters.ComicsAdapter
import com.test.marvel.iu.viewmodels.MainViewModel

class HeroDetailsFragment : Fragment() {
    companion object {
        const val TAG = "HeroDetailsFragment"
    }

    private lateinit var heroSelected: Result
    private var binding: FragmentDetailsHeroBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentDetailsHeroBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.listSuperheroesFragment)
        }
        return this.binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.heroSelected = this.viewModel.heroSelected!!

        this.binding!!.let {
            it.tvNameHero.text = this.heroSelected.name
            it.tvDescriptionHero.text = this.heroSelected.description

            Glide.with(requireContext())
                .load(this.heroSelected.thumbnail.path)
                .placeholder(R.drawable.noimage)
                .into(it.ivThumbnailHero)
        }

        this.viewModel.getComicsByCharacterId(this.heroSelected.id.toString())
            .observe(viewLifecycleOwner, onComicsResponse)

    }

    private val onComicsResponse = Observer<Status> {
        when (it) {
            is Status.Loading -> {
                this@HeroDetailsFragment.binding!!.progressBar.visibility = View.VISIBLE
            }
            is Status.SuccessfulResponse<*> -> {
                this@HeroDetailsFragment.binding!!.progressBar.visibility = View.GONE

                val data = (it.body as ComicsResponse).data.results

                configAdapter(data)
            }
            is Status.Error -> {
                this@HeroDetailsFragment.binding!!.progressBar.visibility = View.GONE
                Log.d(TAG, it.message)
            }
            else -> {}
        }
    }

    private fun configAdapter(data: List<com.test.marvel.data.models.comics.Result>) {
        val adapter = ComicsAdapter(data, requireContext())

        this.binding!!.rvComics.isNestedScrollingEnabled = false
        this.binding!!.rvComics.adapter = adapter
        this.binding!!.rvComics.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding = null
    }


}