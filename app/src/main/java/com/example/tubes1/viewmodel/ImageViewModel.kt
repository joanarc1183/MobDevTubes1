package com.example.tubes1.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tubes1.model.Image
import java.util.Date

class ImageViewModel(application: Application) : AndroidViewModel(application) {

    private val contentResolver: ContentResolver = application.contentResolver

    private val imageList: MutableLiveData<List<Image>> by lazy {
        MutableLiveData<List<Image>>().also {
            it.value = loadImagesFromMediaStore()
        }
    }
    @JvmName("getImagesLiveData")
    fun getImageList(): LiveData<List<Image>> {
        return imageList
    }

    fun updateImageList(newImages: List<Image>) {
        val currentImages = imageList.value.orEmpty().toMutableList()
        val addedImages = newImages.filter { !currentImages.contains(it) }

        currentImages.addAll(addedImages)
        imageList.value = currentImages

        saveImagesToMediaStore(addedImages)
    }


    private fun saveImagesToMediaStore(images: List<Image>) {
        images.forEach { image ->
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${Date().time}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }

            val contentUri: Uri? =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            contentUri?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    // Save the image to the OutputStream
                    // Replace the following line with your actual image saving logic
                    outputStream.write("Your image bytes here".toByteArray())
                }
            }
        }
    }

    private fun loadImagesFromMediaStore(): List<Image> {
        return try {
            queryMediaStoreForImages()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun queryMediaStoreForImages(): List<Image> {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DESCRIPTION
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val imageList = mutableListOf<Image>()

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val descriptionColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateTaken = Date(cursor.getLong(dateColumn))
                val description = cursor.getString(descriptionColumn)

                val imagePath = "content://media/external/images/media/$id"

                val image = Image(imagePath, name, dateTaken, description, "")
                imageList.add(image)
            }
        }

        return imageList
    }
}
