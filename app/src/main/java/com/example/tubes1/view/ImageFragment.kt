package com.example.tubes1.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.Size
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.databinding.FragmentImageBinding
import com.example.tubes1.model.Image
import com.example.tubes1.viewmodel.GalleryRepository
import com.example.tubes1.viewmodel.ImageViewModel
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class ImageFragment : Fragment() {

    private lateinit var galleryViewModel: ImageViewModel
    private lateinit var galleryRepository: GalleryRepository
    private lateinit var binding: FragmentImageBinding
    private lateinit var importDeviceButton: Button

    private val IMPORT_IMAGE_REQUEST_CODE = 101
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private var colCount = 2

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchCameraIntent()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
        result?.let { bitmap ->
            // Save image dan save ke URI
            val imageUri = saveImageToStorage(bitmap)

            val currentDateForName =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

            // Ubah format date
            val today: LocalDate = LocalDate.now()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formattedToday: String = today.format(formatter)

            val sizeInKb = getImageSize(bitmap)

            val dimensions = getImageDimensions(bitmap)

            val image = Image(
                imageUri.toString(),
                "IMG_$currentDateForName",
                formattedToday,
                "Size: $sizeInKb KB\nDimensions: ${dimensions.first} x ${dimensions.second}px",
                "")
            galleryViewModel.addImage(image)
            galleryRepository.saveImages(galleryViewModel.imageList.value.orEmpty())
        }
    }

    // Method untuk ambil gambar + store ke external storage
    private fun saveImageToStorage(bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
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

    // Method untuk mendapatkan size gambar/foto
    private fun getImageSize(bitmap: Bitmap): Long {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageInByte = stream.toByteArray()
        val size = imageInByte.size.toLong()
        return size / 1024
    }

    // Method untuk mendapatkan dimensi gambar/foto
    private fun getImageDimensions(bitmap: Bitmap): Pair<Int, Int> {
        val width = bitmap.width
        val height = bitmap.height
        return Pair(width, height)
    }

    private val imageAdapter by lazy {
        ImageAdapter(galleryViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root

        //val drawerLayout:DrawerLayout=view.findViewById(R.id.drawerLayout)
        // val navView:NavigationView=view.findViewById(R.id.nav_view)
        //toggle=ActionBarDrawerToggle(this,R.string.open,R.string.close)
        //drawerLayout.addDrawerListener(toggle)
        //toggle.syncState()
        //supporActionBar?.setDisplayHomeAsUpEnabled(true)
        //navView.setNavigationItemSelectedListener {
        //  true
        //}
    }

    // override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //   if(toggle.onOptionsItemSelected()){
    //     true
    //}
    //return super.onOptionsItemSelected(item)
    //}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryRepository = GalleryRepository(requireContext())
        galleryViewModel = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)

        importDeviceButton = binding.importDevice

//        val imageAdapter = ImageAdapter()

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

            val layoutManager = GridLayoutManager(requireContext(), colCount)
            binding.recyclerView.layoutManager = layoutManager
        }

        importDeviceButton.setOnClickListener {
            openImagePicker()
        }
    }

    // Intent untuk import Image
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMPORT_IMAGE_REQUEST_CODE)
    }

    // Method untuk import image
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMPORT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let {
                val currentDateForName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

                // Ubah format date
                val today: LocalDate = LocalDate.now()
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val formattedToday: String = today.format(formatter)

                // size photo
                val parcelFileDescriptor: ParcelFileDescriptor? =
                    context?.contentResolver?.openFileDescriptor(it, "r", null)
                val fileSize = parcelFileDescriptor?.statSize ?: 0L

                var size: String
                if (fileSize <= 0) {
                    size = "0 KB"
                } else {
                    val units = arrayOf("B", "KB", "MB", "GB", "TB")
                    val digitGroups = (Math.log10(fileSize.toDouble()) / Math.log10(1024.0)).toInt()
                    size = DecimalFormat("#,##0.#").format(fileSize / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
                }

                // dimension photo
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(it), null, options)
                options.inJustDecodeBounds = false
                val dimensions = Pair(options.outWidth, options.outHeight)

                val image = Image(
                    it.toString(),
                    "IMG_$currentDateForName",
                    formattedToday,
                    "Size: $size\nDimensions: ${dimensions.first} x ${dimensions.second}px",
                    "")
//                val image = Image(it.toString(), "IMG_$currentDateForName", currentDateForName, "", "")
                galleryViewModel.addImage(image)
                galleryRepository.saveImages(galleryViewModel.imageList.value.orEmpty())
            }
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
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            launchCameraIntent()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCameraIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            takePicture.launch(null)
        }
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

