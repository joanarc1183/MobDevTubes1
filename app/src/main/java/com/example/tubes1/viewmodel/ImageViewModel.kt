package com.example.tubes1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1.model.Image

class ImageViewModel : ViewModel() {

    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>> get() = _imageList

    fun addImage(image: Image) {
        // cek apakah image sekarang ada di imagelist atau tidak
        val existingImage = _imageList.value?.firstOrNull { it.uri == image.uri }

        if (existingImage == null) {
            // Kalau tidak ada, masukan ke list
            val currentList = _imageList.value.orEmpty().toMutableList()
            currentList.add(image)
            _imageList.value = currentList
        } else {
            // Kalau ada, update detailsnya tidak perlu dimasukan ke list
            existingImage.name = image.name
            existingImage.date = image.date
            existingImage.story = image.story
            _imageList.value = _imageList.value
        }
    }
}
