package com.example.tubes1.view

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.R
import com.example.tubes1.model.Image

class ImageAdapter(private val onItemClick: (Image) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var imageList: List<Image> = emptyList()

    fun setImages(images: List<Image>) {
        imageList = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        holder.bind(image)
        holder.itemView.setOnClickListener { onItemClick(image) }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(image: Image) {
            val bitmap = BitmapFactory.decodeFile(image.path)
            imageView.setImageBitmap(bitmap)
        }
    }
}
