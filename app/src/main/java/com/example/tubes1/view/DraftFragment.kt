package com.example.tubes1.view//package com.example.tubes1.view
//
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.RequiresApi
//import androidx.fragment.app.Fragment
//import com.example.tubes1.databinding.FragmentDraftBinding
//import com.example.tubes1.model.Image
//import com.example.tubes1.viewmodel.ImageViewModel
//import java.time.LocalDate
//import java.time.ZoneId
//import java.time.format.DateTimeFormatter
//import java.util.Date
//
//class DraftFragment: Fragment() {
//    private lateinit var binding: FragmentDraftBinding
//    private lateinit var viewModel: ImageViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentDraftBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val today: LocalDate = LocalDate.now()
//        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        val formattedToday: String = today.format(formatter)
//
//        binding.date.text = formattedToday
//
////        set name photo
//
////        set photo from view model
////        viewModel.getImageList().observe(viewLifecycleOwner, { imageList->
////
////        })
//
//        binding.save.setOnClickListener(){
////            save to list viewmodel
//            val newImage = Image(
//                uri = Uri.EMPTY,
//                name = binding.name.text.toString(),
//                date = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()),
//                story = binding.story.text.toString()
//
//            )
////            viewModel.updateImageList(newImage)
//        }
//
//    }
//}