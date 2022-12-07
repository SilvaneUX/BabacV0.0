package com.smart.smartbalibackpaker.auth

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.chat.GroupChatActivity
import com.smart.smartbalibackpaker.core.model.User
import com.smart.smartbalibackpaker.core.utils.Constant.ALL_GROUP
import com.smart.smartbalibackpaker.core.utils.Constant.ALL_GROUP_TITLE
import com.smart.smartbalibackpaker.databinding.ActivityAddPhotoBinding
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap

class AddPhotoActivity : AppCompatActivity() {

    private val binding by lazy{ActivityAddPhotoBinding.inflate(layoutInflater)}
    private lateinit var dialog: AlertDialog
    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private var user : FirebaseUser? = null
    private var statusAdd = false
    private lateinit var bitmap: Bitmap
    private var imageUri: Uri? = null
    private lateinit var removeBtn : ImageView
    private lateinit var removeTxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        user = auth.currentUser

        val userIntent = intent.getParcelableExtra<User>(USER)

        binding.txtUsername.text = "Welcome, ${userIntent?.username}"


        binding.imgAddProfile.setOnClickListener {
            if (statusAdd){
                statusAdd = false
                binding.btnAddPhoto.visibility = View.VISIBLE
            } else {
                addPhoto()
            }
        }

        binding.btnAddPhoto.setOnClickListener {
            val ref = storageRef.child("images/${UUID.randomUUID()}")
                ref.putFile(imageUri!!)
                    .addOnSuccessListener{
                        ref.downloadUrl.addOnSuccessListener {
                            registerUser(userIntent?.email ?: "", userIntent?.password ?: "", userIntent?.username ?: "", it.toString())
                        }
                    }
        }

        binding.btnSkip.setOnClickListener {
            registerUser(userIntent?.email ?: "", userIntent?.password ?: "", userIntent?.username ?: "", "")
        }

        setupToolbar()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK -> {
                statusAdd = true
                bitmap = data?.extras?.get("data") as Bitmap
                imageUri = getImageUriFromBitmap(bitmap)

                binding.imgPhoto.setImageBitmap(bitmap)
                binding.btnAddPhoto.visibility = View.VISIBLE
                binding.txtAddPhoto.text = "Change Profile Photo"
                removeBtn.visibility = View.VISIBLE
                removeTxt.visibility = View.VISIBLE
            }
            requestCode == REQUEST_UPLOAD_PICTURE && resultCode == RESULT_OK -> {
                statusAdd = true
                binding.imgPhoto.setImageURI(data?.data)
                imageUri = data?.data

                binding.txtAddPhoto.text = "Change Profile Photo"
                binding.btnAddPhoto.visibility = View.VISIBLE
                removeBtn.visibility = View.VISIBLE
                removeTxt.visibility = View.VISIBLE
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bytes)
        val resolver = applicationContext.contentResolver
        val path = MediaStore.Images.Media.insertImage(
            resolver,
            bitmap,
            System.currentTimeMillis().toString(),
            null
        )
        return Uri.parse(path.toString())
    }

    private fun registerUser(email: String, password: String, username: String, image: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Intent(this, MainActivity::class.java).also { intent ->
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                    val user : HashMap<Any, String> = HashMap()

                    user.apply{
                        put("id", auth.currentUser?.uid ?: "")
                        put("email", email)
                        put("username", username)
                        put("image", image)
                        put("onlineState", "online")
                        put("onTypingState", "noOne")
                    }

                    val reference = db.getReference("users")
                    auth.currentUser?.uid.let { id ->
                        if (id != null) {
                            reference.child(id).setValue(user)
                        }
                    }

                } else {
                    emailAlreadyUse(email)
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun emailAlreadyUse(email: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Email Telah Digunakan", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Email Tersedia", Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun addPhoto() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_photo, null)


        dialogBuilder.apply {
            setTitle(getString(R.string.choose))
            setView(dialogLayout)
            setNegativeButton("Cancel") {
                    dialog, _ -> dialog.dismiss()
            }
        }

        dialog = dialogBuilder.create()
        dialog.show()

        val galleryBtn = dialogLayout.findViewById<ImageView>(R.id.img_gallery)
        removeBtn = dialogLayout.findViewById<ImageView>(R.id.img_remove)
        val cameraBtn = dialogLayout.findViewById<ImageView>(R.id.img_camera)
        removeTxt = dialogLayout.findViewById<TextView>(R.id.txt_remove)

        if (binding.imgPhoto.drawable.constantState?.equals(resources.getDrawable(R.drawable.account).constantState) == true){
            removeBtn.visibility = View.GONE
            removeTxt.visibility = View.GONE
            binding.btnAddPhoto.visibility = View.GONE
            binding.txtAddPhoto.text = "Add DataUser Photo"
        }

        cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_TAKE_PICTURE)
            dialog.dismiss()
        }

        galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/"
            startActivityForResult(intent, REQUEST_UPLOAD_PICTURE)
            dialog.dismiss()
        }

        removeBtn.setOnClickListener {
            binding.btnAddPhoto.visibility = View.GONE
            binding.imgPhoto.setImageDrawable(resources.getDrawable(R.drawable.account))
            dialog.dismiss()
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbAddPhoto)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbAddPhoto.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.tbAddPhoto.setNavigationOnClickListener {
            finish()
        }
    }

    companion object {
        const val USER = "user"
        const val REQUEST_TAKE_PICTURE = 1
        const val REQUEST_UPLOAD_PICTURE = 2
    }

}