package com.example.tubes1.view

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentImageBinding
import com.example.tubes1.model.Image
import com.example.tubes1.viewmodel.GalleryRepository
import com.example.tubes1.viewmodel.ImageViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageFragment : Fragment() {

    private lateinit var galleryViewModel: ImageViewModel
    private lateinit var galleryRepository: GalleryRepository
    private lateinit var binding: FragmentImageBinding

    private val CAMERA_PERMISSION_REQUEST_CODE = 123
    private var colCount = 2

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
        result?.let { bitmap ->
            // Save image dan save ke URI
            val imageUri = saveImageToStorage(bitmap)

            val currentDateForName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val image = Image(imageUri.toString(), "IMG_$currentDateForName", currentDateForName, "")
            galleryViewModel.addImage(image)
            galleryRepository.saveImages(galleryViewModel.imageList.value.orEmpty())
        }
    }

    // Method untuk ambil gambar + store ke external storage
    private fun saveImageToStorage(bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }

        val contentResolver = requireContext().contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let { imageUri ->
            try {
                contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }

        return uri
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryRepository = GalleryRepository(requireContext())
        galleryViewModel = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)

        val imageAdapter = ImageAdapter()

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), colCount)
        recyclerView.adapter = imageAdapter

        galleryViewModel.imageList.observe(viewLifecycleOwner) { images ->
            imageAdapter.setImages(images)
        }

        binding.fabCamera.setOnClickListener {
            checkCameraPermission()
        }

        binding.toolbar.setOnClickListener {
            colCount = if (colCount == 3) 2 else 3

            // Update the layout manager span count
            val layoutManager = GridLayoutManager(requireContext(), colCount)
            binding.recyclerView.layoutManager = layoutManager
        }


    }

    override fun onResume() {
        super.onResume()
        val images = galleryRepository.getImages()
        images.forEach { image ->
            galleryViewModel.addImage(image)
        }
    }

    override fun onPause() {
        super.onPause()
        galleryRepository.saveImages(galleryViewModel.imageList.value.orEmpty())
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            launchCameraIntent()
        }
    }

    private fun launchCameraIntent() {
        takePicture.launch(null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCameraIntent()
                }
            }
        }
    }
}
