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
        val existingImage = imageList.value?.find { it.uri == image.uri }
        Log.d("URI Image", "URI Image Asli ${image.uri}")
        existingImage?.let {
            Log.d("URI it", "URI image it.uri ${it.uri}")
        }

        Log.d("existingimage1", "image Asli $image")
        Log.d("existingimage2", "Image Existing $existingImage ${imageList.value}")

        if (existingImage == null) {
            // Kalau tidak ada, masukan ke list
            val currentList = _imageList.value.orEmpty().toMutableList()
            Log.d("existingimage3", "jadi ini ${currentList.size} ${imageList.value} $image")
            currentList.add(image)
//            _imageList.postValue(currentList)
            _imageList.value = currentList
        } else {
            Log.d("MASUK SINI", "$existingImage")
            // Kalau ada, update detailsnya tidak perlu dimasukan ke list
            existingImage.name = image.name
            existingImage.date = image.date
            existingImage.description = image.description
            existingImage.story = image.story
            _imageList.value = _imageList.value
            Log.d("updateimage1", "jadi ini updateannya $existingImage.name")
            Log.d("updateimage2", "jadi ini updateannya $image")
        }
    }
    fun editImage(image: Image, tempImage: Image){
        if(tempImage.uri == image.uri) {
            Log.d("printanEdit", "$image $tempImage")
            image.name = tempImage.name
            image.date = tempImage.date
            image.description = tempImage.description
            image.story = tempImage.story
            _imageList.value = _imageList.value
            Log.d("editimage1", "jadi ini updateannya $tempImage")
            Log.d("editimage2", "jadi ini updateannya $image")
        }
    }
}
