package com.example.tubes1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1.model.Image

class ImageViewModel : ViewModel() {

    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>> get() = _imageList

    fun addImage(image: Image) {
        val currentList = _imageList.value.orEmpty().toMutableList()
        currentList.add(image)
        _imageList.value = currentList
    }
}
