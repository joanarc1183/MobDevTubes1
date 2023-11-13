package com.example.tubes1.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1.databinding.FragmentImageBinding
import com.example.tubes1.model.Image
import android.graphics.Bitmap
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentDiaryBinding
import com.example.tubes1.viewmodel.GalleryRepository
import com.example.tubes1.viewmodel.ImageViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.Date

class ImageFragment : Fragment() {

    private lateinit var galleryViewModel: ImageViewModel
    private lateinit var galleryRepository: GalleryRepository
    private lateinit var binding: FragmentImageBinding

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

        // Create an instance of the adapter
        val imageAdapter = ImageAdapter()

        // Set up RecyclerView with the adapter
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = imageAdapter

        // Observe changes in the image list
        galleryViewModel.imageList.observe(viewLifecycleOwner) { images ->
            imageAdapter.setImages(
                images
            )
        }

        // Set up the camera intent
        val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
            result?.let {
                // Handle the result, save the image to SharedPreferences, and update the UI
                val image = Image(it.toString(), "Image Name", "Date", "Story")
                galleryViewModel.addImage(image)
                galleryRepository.saveImages(galleryViewModel.imageList.value.orEmpty())
            }
        }

        // Button to launch the camera intent
        binding.fabCamera.setOnClickListener {
            takePicture.launch(null)
        }
    }

    override fun onResume() {
        super.onResume()
        // Load images from SharedPreferences when the fragment is resumed
        val images = galleryRepository.getImages()
        images.forEach { image ->
            galleryViewModel.addImage(image)
        }
    }

    override fun onPause() {
        super.onPause()
        // Save images to SharedPreferences when the fragment is paused
        galleryRepository.saveImages(galleryViewModel.imageList.value.orEmpty())
    }
}

