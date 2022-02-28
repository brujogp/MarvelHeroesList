package com.test.marvel.iu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.marvel.data.models.comics.Result
import com.test.marvel.databinding.ViewListComicsBinding

class ComicsAdapter(
    var data: List<Result>,
    val context: Context
) :
    RecyclerView.Adapter<ComicsAdapter.ComicsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        val binding: ViewListComicsBinding =
            ViewListComicsBinding.inflate(LayoutInflater.from(context), parent, false)

        return ComicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        holder.binding.let {
            Glide.with(context)
                .load(this@ComicsAdapter.data[position].thumbnail.path)
                .into(it.ivThumbnailHeroComic)
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class ComicsViewHolder(val binding: ViewListComicsBinding) :
        RecyclerView.ViewHolder(binding.root)
}