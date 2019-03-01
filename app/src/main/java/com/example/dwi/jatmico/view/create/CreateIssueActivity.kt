package com.example.dwi.jatmico.view.create

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Detail
import com.example.dwi.jatmico.data.models.Issues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.view.home.HomeAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_issue.*
import kotlinx.android.synthetic.main.spinner_item.*
import kotlinx.android.synthetic.main.spinner_item.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CreateIssueActivity : AppCompatActivity(), CreateIssueView {

    private var issue: Detail? = null
    private lateinit var adapter: HomeAdapter
    private lateinit var issuePresenter: CreateIssuePresenter
    private var issuesData: MutableList<Issues>? = null

    var id: Int = 0
    var token: String? = null
    var project_id: RequestBody? = null
    var title: RequestBody? = null
    var description: RequestBody? = null
    var severity_id: RequestBody? = null
    var link: RequestBody? = null
    var image: MultipartBody.Part? = null

    private var isues_id = 0
    private val GALLERY = 1
    private val CAMERA = 2
    private var page = 1
    private var perPage = 10

    private var imageUriFromCamera: Uri? = null

    private var permission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun showLoading() {
        loadingcreate.visibility = View.GONE
    }

    override fun dismissLoading() {
        loadingcreate.visibility = View.GONE
    }

    override fun showErrorAlert(it: Throwable) {

        Log.e("CreateIssueActivity", it.localizedMessage)
        Toast.makeText(this@CreateIssueActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }


    override fun showDataIssue(isues: MutableList<Issues>) {
        Log.d("data_size", isues.toString())
        issuesData = isues
    }


    //--SPINNER SHOW DATA
    override fun showData(projects: MutableList<Project>) {
        Log.d("Show_Project", projects.size.toString())
        adapter.setData(projects)

        projects.add(0, Project(-1, "Select Project", null, null, null))

        val spinnerArrayAdapter = ProjectListAdapter(this@CreateIssueActivity, projects)
        select_project.adapter = spinnerArrayAdapter

        select_project?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    if (Intent.ACTION_SEND != intent.action && intent.type == null) {
                        project_id =
                            RequestBody.create(MediaType.parse("text/plain"), projects.get(position).id.toString())

                        spinnerArrayAdapter.notifyDataSetChanged()
                    }

                }
            }
    }

//--SPINNER--

    class ProjectListAdapter(private val ctx: Context, val projects: MutableList<Project>?) :
        ArrayAdapter<Project>(ctx, R.layout.spinner_item, R.id.item_spiner, projects) {

        override fun getDropDownView(
            position: Int, convertView: View?,
            parent: ViewGroup

        ): View {
            return getCustomView(position, parent)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getCustomView(position, parent)
        }

        fun getCustomView(position: Int, parent: ViewGroup): View {

            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val row = inflater.inflate(R.layout.spinner_item, parent, false)


            row.item_spiner.text = projects?.get(position)?.name
            if (projects?.get(position)?.image?.thumb?.url != null) {
                row.icon_project.visibility = View.VISIBLE
                Picasso
                    .with(row.context)
                    .load(projects?.get(position).image?.thumb?.url)
                    .into(row.icon_project)
            } else {
                row.icon_project.visibility = View.GONE
            }

            return row
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
        val columnIndex = cursor
            ?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_issue)
        setSupportActionBar(toolbarCreate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.getSerializableExtra("data") != null) {
            issue = intent.getSerializableExtra("data") as Detail
            id = issue!!.id

            Log.d("Get Issue", issue.toString())

        }

        initPresenter()
        initAdapter()

        if (issue != null) {
            bindData(issue!!)
            getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
                issuePresenter.getProjects(page, perPage, sp.getString(getString(R.string.access_token), "")!!)

            }

        } else {
            getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
                issuePresenter.getProjects(page, perPage, sp.getString(getString(R.string.access_token), "")!!)
            }
        }

        radio_grup.check(R.id.radio_critical)

        upload_img!!.setOnClickListener {
            if (isHaveStorageAndCameraPermission()) {
                showPictureDialog()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permission, 11)//ASK_PERMISSIONS_REQUEST_CODE
                }
            }
        }


//--SEND DATA--//

        btn_send.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {

                //Edit issue
                if (issue != null) {

                    when (radio_grup.checkedRadioButtonId) {
                        R.id.radio_critical -> severity_id = RequestBody.create(MediaType.parse("text/plain"), "1")
                        R.id.radio_minor -> severity_id = RequestBody.create(MediaType.parse("text/plain"), "2")
                        else -> severity_id = RequestBody.create(MediaType.parse("text/plain"), "3")

                    }

                    title = RequestBody.create(MediaType.parse("text/plain"), input_title.text.toString())
                    description =
                        RequestBody.create(MediaType.parse("text/plain"), input_description.text.toString())
                    link = RequestBody.create(MediaType.parse("text/plain"), input_link.text.toString())

                    getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->
                        token = sp.getString(getString(R.string.access_token).toString(), "")
                    }

                    issuePresenter.updateIssues(
                        id,
                        project_id!!,
                        title!!,
                        description!!,
                        severity_id!!,
                        link!!,
                        image,
                        token!!
                    )

                    Toast.makeText(this@CreateIssueActivity, "Data Saved!", Toast.LENGTH_SHORT).show()
                    finish()
                    setResult(1)


                } else {

//  Create Issue

                    if (input_title.text.toString() == "" || input_description.text.toString() == "" ||
                        input_link.text.toString() == "" || display_img == null || item_spiner == null
                    ) {
                        Toast.makeText(
                            this@CreateIssueActivity, "Lengkapi data terlebih dahulu",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        when (radio_grup.checkedRadioButtonId) {
                            R.id.radio_critical -> severity_id = RequestBody.create(MediaType.parse("text/plain"), "1")
                            R.id.radio_minor -> severity_id = RequestBody.create(MediaType.parse("text/plain"), "2")
                            else -> severity_id = RequestBody.create(MediaType.parse("text/plain"), "3")

                        }

                        var tokenAsRequestBody: RequestBody? = null

                        getSharedPreferences("Jatmico", MODE_PRIVATE).let { sp ->

                            tokenAsRequestBody = RequestBody.create(
                                MediaType.parse("text/plain"),
                                sp.getString(getString(R.string.access_token).toString(), "")!!
                            )
                        }


                        title = RequestBody.create(MediaType.parse("text/plain"), input_title.text.toString())
                        description =
                            RequestBody.create(MediaType.parse("text/plain"), input_description.text.toString())
                        link = RequestBody.create(MediaType.parse("text/plain"), input_link.text.toString())


                        isues_id = intent.getIntExtra("issue_id", isues_id)

                        issuePresenter.postIssues(
                            project_id!!,
                            title!!,
                            description!!,
                            severity_id!!,
                            link!!,
                            image!!,
                            tokenAsRequestBody!!
                        )

                        Toast.makeText(this@CreateIssueActivity, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                        setResult(1)

                    }

                }


            }
        })
    }

    //    show data in edit text
    private fun bindData(issue: Detail) {
        input_title.setText(issue.title)
        input_description.setText(issue.description)
        input_link.setText(issue.link)

        Picasso.with(this).load(issue.image.url).into(display_img)

        Log.d("TITLE", issue.title)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showPictureDialog()
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

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
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
                        image_name.text = file.path

                        Log.d("imagesize", "size =" +file.length())


                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this@CreateIssueActivity, "Failed!", Toast.LENGTH_SHORT).show()
                    }

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
//                compressedImageBitmap =  Compressor(this).compressToBitmap(actualImageFile)

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
                image = MultipartBody.Part.createFormData("image", destination.name, reqFile)
                image_name.text = destination.toString()
            }
        }
    }


    //navigation back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initPresenter() {
        issuePresenter = CreateIssuePresenterImp()
        issuePresenter.initView(this)
    }

    private fun initAdapter() {
        adapter = HomeAdapter()
    }

}
