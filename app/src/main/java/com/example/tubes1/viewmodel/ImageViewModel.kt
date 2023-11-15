package com.example.tubes1.viewmodel

import android.content.res.AssetFileDescriptor
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1.model.Image

class ImageViewModel : ViewModel() {

    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>> get() = _imageList

    fun addImage(image: Image) {
        // cek apakah image sekarang ada di imagelist atau tidak
        val existingImage = _imageList.value?.find { it.uri == image.uri }
        Log.d("existingimage1", "$image ${imageList.value}")
        Log.d("existingimage2", "$imageList $existingImage")

        if (existingImage == null) {
            // Kalau tidak ada, masukan ke list
            val currentList = _imageList.value.orEmpty().toMutableList()
            Log.d("existingimage3", "jadi ini ${currentList.size} ${imageList.value} $image")
            currentList.add(image)
//            _imageList.postValue(currentList)
            _imageList.value = currentList
        } else {
            // Kalau ada, update detailsnya tidak perlu dimasukan ke list
            existingImage.name = image.name
            existingImage.date = image.date
            existingImage.description = image.description
            existingImage.story = image.story
            _imageList.value = _imageList.value
//            _imageList.postValue(_imageList.value)
            Log.d("existingimage4", "jadi ini updateannyaelse $existingImage.name")
        }
    }

    fun updateInfo(image: Image, name: String, story: String){
        val currentPhoto = _imageList.value
        currentPhoto?.let{
            val updatedPhoto = image.copy(name = name, story = story)
            _imageList.value = listOf(updatedPhoto)
        }
    }
}
