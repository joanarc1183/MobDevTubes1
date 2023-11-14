package com.example.tubes1.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentDiaryBinding
import com.example.tubes1.model.Image

class DiaryFragment: Fragment() {
    private lateinit var binding: FragmentDiaryBinding
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

        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.setText(name)
        binding.photo.setImageURI(uri)
        binding.date.text = date
        binding.story.setText(story)

        // Get data
//        val draftView = DraftFragment()
//
//        binding.edit.setOnClickListener {
//            childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, draftView)
//                .commit()
//        }
    }
}