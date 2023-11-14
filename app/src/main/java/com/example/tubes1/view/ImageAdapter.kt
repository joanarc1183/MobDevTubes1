package com.example.tubes1.view

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tubes1.R
import com.example.tubes1.databinding.RecyclerItemBinding
import com.example.tubes1.model.Image

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var imageList: List<Image> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = imageList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setImages(images: List<Image>) {
        imageList = images
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            if (!image.uri.isNullOrEmpty()) {
                val uri = Uri.parse(image.uri)
                Log.d("BIND", "URI: $uri")

                Glide.with(itemView.context)
                    .load(uri)
                    .into(binding.imageView)
            }
        }
    }
}
