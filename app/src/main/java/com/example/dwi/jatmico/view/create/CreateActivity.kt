package com.example.dwi.jatmico.view.create

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.Toast
import com.example.dwi.jatmico.R
import kotlinx.android.synthetic.main.activity_create.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import android.widget.ArrayAdapter
import android.util.Log
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.presenter.CreatePresenter
import com.example.dwi.jatmico.presenter.CreatePresenterImp
import com.example.dwi.jatmico.view.home.HomeAdapter
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.util.*


class CreateActivity : AppCompatActivity(), CreateView {

    var projectNames: MutableList<String>? = null
    override fun showLoading() {
        loading.visibility = View.GONE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {

        Log.e("CreateActivity", it?.localizedMessage)
        Toast.makeText(this@CreateActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }



    //--SPINNER SHOW DATA
    override fun showData(projects: MutableList<Project>) {
        Log.d("Show_Project", projects.size.toString())

        adapter.setData(projects)

        projectNames = ArrayList()
        for (project in projects) {
            projectNames?.add(project.name)
        }
        // Initializing an ArrayAdapter
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
            this, R.layout.spinner_item,R.id.item_spiner, projectNames
        ) {

            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                //           view.item_spiner = project.name
                return view
            }
        }

//--SPINNER--
        select_project.adapter = spinnerArrayAdapter

        select_project?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                project_id = RequestBody.create(MediaType.parse("text/plain"), projects.get(position).id.toString())
            }

        }
    }


    private fun isHaveStorageAndCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val cameraPermission = checkSelfPermission(Manifest.permission.CAMERA)
            val writeExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return !(storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED || writeExternalPermission != PackageManager.PERMISSION_GRANTED)
        } else {
            return true
        }
    }


    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        val bm = Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        ) as Bitmap
        return bm
    }

    private fun getRealPathFromURI(contentUri: Uri?): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor
            ?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)!!
    }

    private lateinit var adapter: HomeAdapter
    private lateinit var presenter: CreatePresenter

    var token: RequestBody? = null
    var project_id: RequestBody? = null
    var title: RequestBody? = null
    var description: RequestBody? = null
    var severity_id: RequestBody? = null
    var link: RequestBody? = null
    var image: MultipartBody.Part? = null

    private val GALLERY = 1
    private val CAMERA = 2
    private var page = 1
    private var perPage = 10

    private var imageUriFromCamera: Uri? = null

    private var permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initPresenter()
        initAdapter()
        getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
            presenter.getProjects(page, perPage, sp.getString(getString(R.string.access_token), "")!!)

        }

        val filter = when (radio_grup.checkedRadioButtonId) {
            R.id.radio_critical -> severity_id = RequestBody.create(MediaType.parse("text/plain"), 1.toString())
            R.id.radio_minor -> severity_id = RequestBody.create(MediaType.parse("text/plain"), 2.toString())
            else -> severity_id = RequestBody.create(MediaType.parse("text/plain"), 3.toString())
        }


//---Button Image--//
        upload_img!!.setOnClickListener {
            showPictureDialog()
        }

//--SEND DATA--//

        btn_send.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {


                if (input_title.text.toString().equals("") || input_description.text.toString().equals("") ||
                    input_link.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@CreateActivity, "Lengkapi data terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->

                        token = RequestBody.create(
                            MediaType.parse("text/plain"),
                            sp.getString(getString(R.string.access_token).toString(), "")!!
                        )

                    }

                    title = RequestBody.create(MediaType.parse("text/plain"), input_title.text.toString())
                    description = RequestBody.create(MediaType.parse("text/plain"), input_description.text.toString())
                    link = RequestBody.create(MediaType.parse("text/plain"), input_link.text.toString())

                    presenter.postIssues(project_id!!, title!!, description!!, severity_id!!, link!!, image!!, token!!)

                }
                Toast.makeText(this@CreateActivity, "Data Saved!", Toast.LENGTH_SHORT).show()
                finish()
                setResult(1,intent)


            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  //ASK_PERMISSIONS_REQUEST_CODE
        }

        if (isHaveStorageAndCameraPermission()) {
            showPictureDialog()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, GALLERY)//ASK_PERMISSIONS_REQUEST_CODE
            }
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)

    }

    private fun takePhotoFromCamera() {
        val values = ContentValues()
        imageUriFromCamera = contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //  val path = saveImage(bitmap)
                    display_img!!.setImageBitmap(bitmap)


                    val selectedImage = data.data
                    val projection = arrayOf(MediaStore.MediaColumns.DATA)
                    val cursor = managedQuery(selectedImage, projection, null, null, null)
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    cursor.moveToFirst()
                    val path = cursor.getString(columnIndex)
                    val file = File(path)

                    val type: String?
                    val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
                    type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                    val reqfile = RequestBody.create(MediaType.parse(type), file)
                    image = MultipartBody.Part.createFormData("image", file.name, reqfile)


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@CreateActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            } else if (requestCode == CAMERA) {

                val thumbnail = MediaStore.Images.Media.getBitmap(contentResolver, imageUriFromCamera)
                val ei = ExifInterface(getRealPathFromURI(imageUriFromCamera))
                val orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                val rotatedBitmap: Bitmap?
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> {
                        rotatedBitmap = rotateImage(thumbnail, 90f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_180 -> {
                        rotatedBitmap = rotateImage(thumbnail, 180f)
                    }
                    ExifInterface.ORIENTATION_ROTATE_270 -> {
                        rotatedBitmap = rotateImage(thumbnail, 270f)
                    }
                    else -> rotatedBitmap = thumbnail
                }

                val bytes = ByteArrayOutputStream()
                val compressedBitmap = Bitmap.createScaledBitmap(
                    rotatedBitmap,
                    rotatedBitmap?.width!! / 4,
                    rotatedBitmap.height / 4,
                    false
                )
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
                display_img.setImageBitmap(compressedBitmap)
                val destination = File(
                    Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis().toString() + ".jpg"
                )
                val fo: FileOutputStream
                try {
                    destination.createNewFile()
                    fo = FileOutputStream(destination)
                    fo.write(bytes.toByteArray())
                    fo.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val type: String
                val extension = MimeTypeMap.getFileExtensionFromUrl(destination.getAbsolutePath())
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                val reqFile = RequestBody.create(MediaType.parse(type), destination)
                image = MultipartBody.Part.createFormData("image", destination.getName(), reqFile)
            }
        }
    }



    //navigation back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initPresenter() {
        presenter = CreatePresenterImp()
        presenter.initView(this)
    }

    private fun initAdapter() {
        adapter = HomeAdapter()
    }

}

