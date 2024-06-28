package com.example.project_pao

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.project_pao.databinding.ActivityChangepasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangepasswordBinding
    val createClient = userApi.create()
    var mId : String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mId = intent.getStringExtra("Id").toString()
        var mEmail = intent.getStringExtra("Email")
        var mPass = intent.getStringExtra("Pass")
        var mImage = intent.getStringExtra("image")

        binding.email.setText(mEmail)
        binding.email.isEnabled = false
        binding.edtPass.setText(mPass)
        binding.edtImage.setText(mImage)

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
        return TextView(this).apply { text = "แก้ไขข้อมูล"
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
    fun Confirm(v: View) {
        createClient.updatePassword(
            mId.toInt(),
            binding.email.text.toString(),
            binding.edtPass.text.toString(),
            binding.edtImage.text.toString()
        ).enqueue(object : Callback<user> {
            override fun onResponse(call: Call<user>, response: Response<user>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext, "แก้ไขข้อมูลเสร็จสิ้น",
                        Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext, " Update Failure",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
            override fun onFailure(call: Call<user>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Error onFailure " + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
    fun Cancel(view: View) {
        val intent = Intent(this,ProfileActivity::class.java)
        this.startActivity(intent)

    }
}