package com.example.tubes1.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentDraftBinding
import com.example.tubes1.model.Image
import com.example.tubes1.viewmodel.GalleryRepository
import com.example.tubes1.viewmodel.ImageViewModel

class DraftFragment: Fragment() {
    private lateinit var binding: FragmentDraftBinding
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var galleryRepository: GalleryRepository
    private lateinit var uri: Uri
    private lateinit var name: String
    private lateinit var date: String
    private lateinit var description: String
    private lateinit var story: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val image = arguments?.getParcelable<Image>("image")
        image?.let {
            uri = Uri.parse(it.uri)
            name = it.name
            date = it.date
            description = it.description
            story = it.story
        }
        binding = FragmentDraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        galleryRepository = GalleryRepository(requireContext())

        // set name, date, story photo
        binding.name.setText(name)
        binding.date.text = date
        binding.description.text = description
        binding.story.setText(story)

        binding.save.setOnClickListener(){
            // ambil judul/nama photo dan story
            name = binding.name.text.toString()
            story = binding.story.text.toString()

            // objek image di ubah deskripsinya
            val tempImage = Image(uri.toString(), name, date, description, story)
            val images = galleryRepository.getImages()
            images.forEach { image ->
                imageViewModel.editImage(image,tempImage)
            }
            galleryRepository.saveImages(images)

            Log.d("updateimage", "jadi ini updateannya $name $story")

            val diaryView = DiaryFragment()
            val bundle = Bundle()
            bundle.putParcelable("image", tempImage)
            diaryView.arguments = bundle

            requireFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, diaryView)
                .addToBackStack(null)
                .commit()
        }
    }
}