//package com.example.tubes1.viewmodel
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.net.Uri
//import com.example.tubes1.model.Image
//import java.util.Date
//
//class ImageStore(context: Context) {
//    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//
//    fun saveImage(uri: Uri, name: String, date: Date, story: String) {
//        val editor = sharedPreferences.edit()
//        editor.putString("uri", uri.toString())
//        editor.putString("name", name)
//        editor.putLong("date", date.time)
//        editor.putString("story", story)
//        editor.apply()
//    }
//
//    fun getImage(): Image {
//        val uriString = sharedPreferences.getString("uri", "") ?: ""
//        val name = sharedPreferences.getString("name", "") ?: ""
//        val dateMillis = sharedPreferences.getLong("date", 0)
//        val story = sharedPreferences.getString("story", "") ?: ""
//
//        val uri = Uri.parse(uriString)
//        val date = Date(dateMillis)
//
//        return Image(uri, name, date, story)
//    }
//
//    fun getAllImages(): List<Image> {
//        // Implement this method to retrieve all saved images from your storage
//        // Return a list of Image objects
//        return emptyList()
//    }
//}
//
