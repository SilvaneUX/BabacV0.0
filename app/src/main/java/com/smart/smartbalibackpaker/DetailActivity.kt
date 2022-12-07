package com.smart.smartbalibackpaker

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.smart.smartbalibackpaker.chat.GroupChatActivity
import com.smart.smartbalibackpaker.chat.GroupChatActivity.Companion.GROUP_ID
import com.smart.smartbalibackpaker.dashboard.DetailPlaceViewModel
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.utils.Formatter
import com.smart.smartbalibackpaker.databinding.ActivityDetailBinding
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import com.smart.smartbalibackpaker.registration.RegistFirstFormActivity
import java.io.ByteArrayOutputStream


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var imageUri: Uri
    private var state : Boolean = false
    private val detailPlaceViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        ).get(DetailPlaceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
//            setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.color.bg_primary, null))
            setDisplayHomeAsUpEnabled(true)
            title = "Detail"
        }
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val type = intent.getStringExtra(EXTRA_TYPE)
        showProgressBarDetails(true)
        if (id != 0 && type != null) {
            getDetails(id)
        }

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        db = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        createGroupChat()
    }

    private fun addToFavorite(place: TourismDataEntity) {
        binding.btnAddToFavorite.setOnClickListener {
            state = !state
            detailPlaceViewModel.setFavorite(place, state)
            if(state){
                Snackbar.make(it, "Berhasil ditambahkan ke favorit", Snackbar.LENGTH_LONG)
                    .setAction("Tutup") {
                        // Responds to click on the action
                    }
                    .show()
                binding.apply {
                    btnAddToFavorite.apply {
                        text = resources.getString(R.string.remove_from_favorite)
                        setBackgroundColor(resources.getColor(R.color.bg_btn_remove_favorite))
                    }
                }
            } else {
                Snackbar.make(it, "Berhasil dihapus dari favorit", Snackbar.LENGTH_LONG)
                    .setAction("Tutup") {
                        // Responds to click on the action
                    }
                    .show()
                binding.apply {
                    btnAddToFavorite.apply {
                        text = resources.getString(R.string.add_to_favorite)
                        setBackgroundColor(resources.getColor(R.color.bg_btn_add_favorite))
                    }
                }
            }
        }
    }

    private fun createGroupChat() {
        binding.btnJoinGroupChat.setOnClickListener {
            val title = binding.tvDetailTitle.text.toString()
            val time = "${System.currentTimeMillis()}"
            val createGroup: HashMap<String, String> = HashMap()
            val imageRef: StorageReference = storage.reference.child("images/$time")
            val imgBitmap = binding.imgDetailPoster.drawable.toBitmap()

            val baos = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val image = baos.toByteArray()

            imageRef.putBytes(image)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                        createGroup.apply {
                            put("groupId", time)
                            put("groupTitle", title)
                            put("groupImage", it.toString())
                            put("time", time)
                            put("createdBy", auth.uid.toString())
                        }

                        val groupExist = db.getReference("groups")
                        groupExist.orderByChild("groupTitle").equalTo(title)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (ds in snapshot.children) {
                                            val groupTitle = ds.child("groupTitle").value.toString()
                                            val groupId = ds.child("groupId").value.toString()

                                            // if group already exist
                                            if (title == groupTitle) {
                                                val ref = db.getReference("groups").child(groupId)
                                                    .child("member")
                                                ref.orderByChild("uid").equalTo(user.uid)
                                                    .addListenerForSingleValueEvent(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            // if user is a member of group
                                                            if (snapshot.exists()) {
                                                                val intent = Intent(
                                                                    this@DetailActivity,
                                                                    GroupChatActivity::class.java
                                                                )
                                                                intent.putExtra(GROUP_ID, groupId)
                                                                startActivity(intent)
                                                                Toast.makeText(
                                                                    applicationContext,
                                                                    "Group Already Exist",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                            // if user is not a member of group
                                                            else {
                                                                val timestamp =
                                                                    System.currentTimeMillis()
                                                                val hashMap: HashMap<String, String> =
                                                                    HashMap()
                                                                hashMap["uid"] = user.uid
                                                                hashMap["role"] = "member"
                                                                hashMap["time"] =
                                                                    timestamp.toString()

                                                                val addRef =
                                                                    db.getReference("groups")
                                                                addRef.child(groupId)
                                                                    .child("member").child(user.uid)
                                                                    .setValue(hashMap)
                                                                    .addOnSuccessListener {
                                                                        val intent = Intent(
                                                                            this@DetailActivity,
                                                                            GroupChatActivity::class.java
                                                                        )
                                                                        intent.putExtra(
                                                                            GROUP_ID,
                                                                            groupId
                                                                        )
                                                                        startActivity(intent)
                                                                        Toast.makeText(
                                                                            applicationContext,
                                                                            "Added Successfully",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    }
                                                                    .addOnFailureListener {
                                                                        Toast.makeText(
                                                                            applicationContext,
                                                                            it.message,
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    }
                                                            }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                        }
                                                    })
//                                Toast.makeText(applicationContext, "Group Already Exist", Toast.LENGTH_SHORT).show()
                                            }
                                            // if group not exist
                                            else {
                                                val ref: DatabaseReference =
                                                    db.getReference("groups")
                                                ref.child(time).setValue(createGroup)
                                                    .addOnSuccessListener {
                                                        val addMember: HashMap<String, String> =
                                                            HashMap()
                                                        addMember["uid"] = auth.uid.toString()
                                                        addMember["role"] = "creator"
                                                        addMember["time"] = time

                                                        val refMember = db.getReference("groups")
                                                        refMember.child(time).child("member")
                                                            .child(auth.uid.toString())
                                                            .setValue(addMember)
                                                            .addOnSuccessListener {
                                                                val intent = Intent(
                                                                    this@DetailActivity,
                                                                    GroupChatActivity::class.java
                                                                )
                                                                intent.putExtra(GROUP_ID, time)
                                                                startActivity(intent)
                                                                Toast.makeText(
                                                                    applicationContext,
                                                                    "Group Succesfully Created",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                            .addOnFailureListener {
                                                                Toast.makeText(
                                                                    applicationContext,
                                                                    it.message,
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                    }
                                                    .addOnFailureListener {
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                        }
                                    } else {
                                        val ref: DatabaseReference = db.getReference("groups")
                                        ref.child(time).setValue(createGroup)
                                            .addOnSuccessListener {
                                                val addMember: HashMap<String, String> = HashMap()
                                                addMember["uid"] = auth.uid.toString()
                                                addMember["role"] = "creator"
                                                addMember["time"] = time

                                                val refMember = db.getReference("groups")
                                                refMember.child(time).child("member")
                                                    .child(auth.uid.toString())
                                                    .setValue(addMember)
                                                    .addOnSuccessListener {
                                                        val intent = Intent(
                                                            this@DetailActivity,
                                                            GroupChatActivity::class.java
                                                        )
                                                        intent.putExtra(GROUP_ID, time)
                                                        startActivity(intent)
                                                        Toast.makeText(
                                                            applicationContext,
                                                            "Group Succesfully Created",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(
                                                            applicationContext,
                                                            it.message,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                            .addOnFailureListener {
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })


                    }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showProgressBarDetails(status: Boolean) {
        binding.apply {
            if (status) {
                progressbarDetail.visibility = View.VISIBLE
                container.visibility = View.GONE
            } else {
                progressbarDetail.visibility = View.GONE
                container.visibility = View.VISIBLE
            }
        }
    }

    private fun getDetails(id: Int) {
        detailPlaceViewModel.setSelectedPlace(id)
        detailPlaceViewModel.detailPlace.observe(this, {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarDetails(true)
                    Status.SUCCESS -> {
                        populateContent(it.data as TourismDataEntity)
                        buttonFavoriteCheck(it.data.isFavorite)
                        state = it.data.isFavorite
                        addToFavorite(it.data)
                        showProgressBarDetails(false)
                    }
                    Status.ERROR -> {
                        showProgressBarDetails(false)
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun populateContent(content: TourismDataEntity) {
        Glide.with(this)
            .load("https://balibackpacker.co.id/storage/public/pictures/thumbnail/${content.thumbnail}")
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .apply(RequestOptions().transform(RoundedCorners(10)).override(90, 120))
            .into(binding.imgDetailPoster)

        Glide.with(this)
            .load("https://balibackpacker.co.id/storage/public/pictures/thumbnail/${content.thumbnail}")
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .apply(RequestOptions().centerCrop())
            .into(binding.imgDetailBackdrop)

        binding.apply {
            tvDetailTitle.text = content.title
            tvDetailLocation.text = content.address
//            tvDetailPlaceDesc.text = content.desc
            tvDetailTitleTicketPriceValue.text = content.price?.toDouble()?.let {
                Formatter.rupiahFormatter(
                    it
                )
            }
            btnJoinGroupChat.text = "Bergabung ke Ruang Obrolan ${content.title}"

            supportActionBar?.title = content.title
        }
    }

    private fun buttonFavoriteCheck(state: Boolean) {
        if(state){
            binding.apply {
                btnAddToFavorite.apply {
                    text = resources.getString(R.string.remove_from_favorite)
                    setBackgroundColor(resources.getColor(R.color.bg_btn_remove_favorite))
                }
            }
        } else {
            binding.apply {
                btnAddToFavorite.apply {
                    text = resources.getString(R.string.add_to_favorite)
                    setBackgroundColor(resources.getColor(R.color.bg_btn_add_favorite))
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TYPE = "extra_type"
    }
}