package com.example.tubes1.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import com.example.tubes1.R
import com.example.tubes1.model.Image
import com.example.tubes1.viewmodel.ImageViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageFragment : Fragment() {

    private lateinit var imageViewModel: ImageViewModel
    private lateinit var imageAdapter: ImageAdapter

    private val REQUEST_IMAGE_CAPTURE = 1

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        // Restore the images list from the savedInstanceState
        val savedImages = savedInstanceState?.getSerializable("images") as? List<Image>
        if (savedImages != null) {
            imageViewModel.updateImageList(savedImages)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        imageAdapter = ImageAdapter { image -> onImageClick(image) }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = imageAdapter

        val imageObserver = Observer<List<Image>> { images ->
            imageAdapter.setImages(images)
        }

        imageViewModel.getImageList().observe(viewLifecycleOwner, imageObserver)

        // Set up click listener for FloatingActionButton
        val fabCamera: FloatingActionButton = view.findViewById(R.id.fabCamera)
        fabCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the images list when there's a configuration change
        outState.putSerializable("images", imageViewModel.getImageList().value?.toMutableList() as? Serializable)
        super.onSaveInstanceState(outState)
    }

    private fun onImageClick(image: Image) {
        // Handle the click event, for example, show a dialog with the image details.
        // For simplicity, I'll just print the description to the log.
        println("Clicked on image: ${image.description}")
    }

    private fun dispatchTakePictureIntent() {
        // Check if the camera permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If not, request the camera permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // The camera permission is already granted, proceed with the camera intent
            startCameraIntent()
        }
    }

    // Helper method to start the camera intent
    private fun startCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            // Pass additional information via Intent extras
            takePictureIntent.putExtra("imageName", "New Image")
            takePictureIntent.putExtra("imageDescription", "New Description")
            takePictureIntent.putExtra("imageStory", "New Story")
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with the camera intent
                    startCameraIntent()
                } else {
                    // Permission denied, handle accordingly (show a message, disable features, etc.)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // The image capture was successful, handle the result here
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            val imageName = data?.getStringExtra("imageName")
            val imageDescription = data?.getStringExtra("imageDescription")
            val imageStory = data?.getStringExtra("imageStory")

            // Save the captured image to storage
            if (imageBitmap != null) {
                val imagePath = saveImageToStorage(imageBitmap)

                // Create a new Image object with the saved image path
                val newImage = Image(
                    path = imagePath,
                    name = imageName ?: "New Image",
                    date = Date(),
                    description = imageDescription ?: "New Description",
                    story = imageStory ?: "New Story"
                )

                // Get the current list of images from the ViewModel
                val currentImages = imageViewModel.getImageList().value.orEmpty().toMutableList()

                // Check if the new image is not already in the list
                if (!currentImages.contains(newImage)) {
                    // Add the new image to the list
                    currentImages.add(newImage)

                    // Update the ViewModel's image list
                    imageViewModel.updateImageList(currentImages)
                }
            }
        }
    }

    // Function to save the captured image to a file
    private fun saveImageToStorage(bitmap: Bitmap): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "IMG_$timestamp.jpg"
        val file = File(requireContext().getExternalFilesDir(null), filename)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        return file.absolutePath
    }
}
