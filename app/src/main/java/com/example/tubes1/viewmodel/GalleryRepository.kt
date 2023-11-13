package com.example.tubes1.viewmodel

import android.content.Context
import android.util.Log
import com.example.tubes1.model.Image

class GalleryRepository(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("gallery_prefs", Context.MODE_PRIVATE)

    fun saveImages(images: List<Image>) {
        val editor = sharedPreferences.edit()

        // Clear existing data
        editor.clear()

        // Save new data
        images.forEachIndexed { index, image ->
            editor.putString("uri_$index", image.uri)
            editor.putString("name_$index", image.name)
            editor.putString("date_$index", image.date)
            editor.putString("story_$index", image.story)
        }

        editor.apply()
        Log.d("ImageRepository", "Saved images: $images")
    }

    fun getImages(): List<Image> {
        val imageList = mutableListOf<Image>()

        // Retrieve data from SharedPreferences
        var index = 0
        while (sharedPreferences.contains("uri_$index")) {
            val uri = sharedPreferences.getString("uri_$index", "") ?: ""
            val name = sharedPreferences.getString("name_$index", "") ?: ""
            val date = sharedPreferences.getString("date_$index", "") ?: ""
            val story = sharedPreferences.getString("story_$index", "") ?: ""

            val Image = Image(uri, name, date, story)
            imageList.add(Image)

            index++
        }

        return imageList
    }
}

