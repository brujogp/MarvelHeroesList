package com.test.marvel.iu.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.marvel.R
import com.test.marvel.databinding.ViewListHeroItemBinding
import com.test.marvel.data.models.Result
import com.test.marvel.iu.fragments.ListSuperheroesFragment

class ListHeroesAdapter(
    private val context: Context,
    private val data: MutableList<Result>,
    private val loadInfo: MutableLiveData<Boolean>
) : RecyclerView.Adapter<ListHeroesAdapter.HeroesHolder>() {
    private var binding: ViewListHeroItemBinding? = null

    companion object {
        const val TAG = "ListHeroesAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroesHolder {
        this.binding = ViewListHeroItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HeroesHolder(this.binding!!)
    }

    override fun onBindViewHolder(holder: HeroesHolder, position: Int) {
        holder.holderViewBinding.let {
            it.tvNameHero.text = data[holder.adapterPosition].name
            it.tvBioHero.text = data[holder.adapterPosition].description

            Glide.with(context)
                .load(data[holder.adapterPosition].thumbnail.path)
                .placeholder(R.drawable.noimage)
                .into(it.ivThumbnailHero)
        }

        if ((data.size - 10) == holder.adapterPosition) {
            this.loadInfo.postValue(true)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class HeroesHolder(val holderViewBinding: ViewListHeroItemBinding) :
        RecyclerView.ViewHolder(holderViewBinding.root)
}