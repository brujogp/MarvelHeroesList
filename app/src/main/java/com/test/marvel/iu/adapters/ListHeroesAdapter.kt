package com.test.marvel.iu.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.marvel.R
import com.test.marvel.databinding.ViewListHeroItemBinding
import com.test.marvel.data.models.Result

class ListHeroesAdapter(
    private val context: Context,
    private val data: List<Result>
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
        Log.d(TAG, data[position].description)

        holder.holderViewBinding.let {
            Glide.with(context).load(data[position].thumbnail.path).placeholder(R.drawable.noimage)
                .into(it.ivThumbnailHero)
            it.tvNameHero.text = data[position].name
            it.tvBioHero.text = data[position].description
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class HeroesHolder(val holderViewBinding: ViewListHeroItemBinding) :
        RecyclerView.ViewHolder(holderViewBinding.root)
}