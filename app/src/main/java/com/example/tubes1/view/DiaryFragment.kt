//package com.example.tubes1.view
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.example.tubes1.R
//import com.example.tubes1.databinding.FragmentDiaryBinding
//
//class DiaryFragment: Fragment() {
//    private lateinit var binding: FragmentDiaryBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentDiaryBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // get data
//
//        val draftView = DraftFragment()
////        binding.edit.setOnClickListener(){
////            childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, draftView)
////                .commit()
////        }
//
//    }
//}