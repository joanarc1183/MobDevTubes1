package com.example.tubes1.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            val uriString = image.uri
            if (uriString.isNotEmpty()) {
                val uri = Uri.parse(uriString)
                binding.imageView.setImageURI(uri)
            }
        }
    }
}
