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
import com.example.tubes1.viewmodel.ImageViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DraftFragment: Fragment() {
    private lateinit var binding: FragmentDraftBinding
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var uri: Uri
    private lateinit var name: String
    private lateinit var date: String
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
            story = it.story
        }
        binding = FragmentDraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        // set name, date, story photo
        binding.name.setText(name)
        binding.date.text = date
        binding.story.setText(story)

        binding.save.setOnClickListener(){
            // ambil judul/nama photo dan story
            name = binding.name.text.toString()
            story = binding.story.text.toString()

            val image = Image(uri.toString(), name, date, story)
            imageViewModel.addImage(image)

            Log.d("HEHHEHEHE", name)

            val diaryView = DiaryFragment()

            val bundle = Bundle()
//            val image = Image(uri.toString(), name, date, story)
            bundle.putParcelable("image", image)
            diaryView.arguments = bundle

            requireFragmentManager().beginTransaction()
                .replace(binding.fragmentDraft.id, diaryView)
                .addToBackStack(null)
                .commit()
        }

    }
}