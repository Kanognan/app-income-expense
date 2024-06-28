package com.example.project_pao

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.example.project_pao.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    lateinit var session: SessionManager
    var createClient = userApi.create()
    var mEmail :String? = null
    var NewPass :String? = null
    var edtImage :String? = null
    //    lateinit var imageView: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            // Show custom title in action bar
            customView = actionBarCustomTitle()
            displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM

            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)

            // Set action bar background color
            setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#125B50")
                )
            )
            customView.setPadding(
                230, // left padding
                0, // top padding
                0, // right padding
                0 // bottom padding
            )
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.home -> {
                replaceActivity(OverViewActivity())
                return true}
            R.id.add_tran -> {
                replaceActivity(TransactionActivity())
                return true}
            R.id.monetization -> {
                replaceActivity(HistoryTransactionActivity())
                return true}
            R.id.chart -> {
                replaceActivity(SumActivity())
                return true}
            R.id.account -> {
                replaceActivity(ProfileActivity())
                return true}
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun replaceActivity(activity: AppCompatActivity){
        var i : Intent = Intent(applicationContext,activity::class.java)
        startActivity(i)
        finish()
    }
    private fun actionBarCustomTitle(): TextView? {
        return TextView(this).apply { text = "โปรไฟล์ของฉัน"
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT)

            layoutParams = params
            gravity = Gravity.CENTER


            setTextAppearance(
                android.R.style.TextAppearance_Material_Widget_ActionBar_Title
            )
            setTextColor(Color.WHITE)
        }
    }


    //แก้ไขรูป

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.imageUser.setImageURI(null)
            binding.imageUser.setImageURI(imageUri)
        }
    }
    override fun onResume() {
        super.onResume()
        ShowProfile()
    }
    //ดึงข้อมูลจากดาต้า มาแสดง คือ email กับ รูป
    fun ShowProfile(){
        session = SessionManager(applicationContext)
        val id:String? = session.pref.getString(SessionManager.KEY_ID,null)
        createClient.retrieveProfile(id.toString())
            .enqueue(object : Callback<user> {
                override fun onResponse(
                    call: Call<user>,
                    response: Response<user>
                ) {
                    if(response.isSuccessful){
                        binding.tvEmail.text= (response.body()?.us_email.toString())
                        mEmail = (response.body()?.us_email.toString())
                        Glide.with(applicationContext)
                            .load((response.body()?.image.toString()))
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(binding.imageUser);
                    }else{
                        Toast.makeText(applicationContext,"User ID Not Found",
                            Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<user>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }

    //เมื่อกดแก้ไขข้อมูล
    fun toChangepass(view: View){
        session = SessionManager(applicationContext)
        val id:String? = session.pref.getString(SessionManager.KEY_ID,null)
        val intent = Intent(this, ChangePasswordActivity::class.java)
        intent.putExtra("Id",id.toString())
        intent.putExtra("Email",mEmail)
        intent.putExtra("Pass",NewPass)
        intent.putExtra("Image",edtImage)
        this.startActivity(intent)
    }

    fun toLogout (view: View){
        session = SessionManager(applicationContext)
        val edit = session.edior
        edit.clear()
        edit.commit()

        var i:Intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(i)
        finish()
    }

    fun Cancel(view: View) {
        val intent = Intent(this,OverViewActivity::class.java)
        this.startActivity(intent)

    }

    //menu
//    private fun actionBarCustomTitle(): TextView? {
//        return TextView(this).apply { text = "เป๋าเงิน"
//            val params = ActionBar.LayoutParams(
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.WRAP_CONTENT)
//
//            layoutParams = params
//            gravity = Gravity.CENTER
//
//            setTextAppearance(
//                android.R.style.TextAppearance_Material_Widget_ActionBar_Title
//            )
//            setTextColor(Color.WHITE)
//        }
//    }

}


