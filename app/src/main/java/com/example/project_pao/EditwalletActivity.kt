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
import com.example.project_pao.databinding.ActivityEditwalletBinding
import retrofit2.Call
import retrofit2.Callback

class EditwalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditwalletBinding
    val createClient = userApi.create()
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditwalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var wal_name= intent.getStringExtra("wallet_name")
        var wal_cur = intent.getStringExtra("currentcy")
         binding.currency.setText(wal_cur)
        binding.walletname.setText(wal_name)

        binding.btnCancel.setOnClickListener() {
            var i: Intent = Intent(applicationContext, OverViewActivity::class.java)
            startActivity(i)
            finish()
        }
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
        return TextView(this).apply { text = "แก้ไข"
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


    fun updatewallet(v: View) {
        if ( binding.currency.text.toString().isNotEmpty()  && binding.walletname.text.toString().isNotEmpty() ){
            session = SessionManager(applicationContext)
            val id = session.pref.getString(SessionManager.KEY_ID, null)

            createClient.updateWallet(
                id.toString(),
                binding.currency.text.toString(),
                binding.walletname.text.toString()
            ).enqueue(object : Callback<wallet> {
                override fun onResponse(
                    call: Call<wallet>,
                    response: retrofit2.Response<wallet>
                ) {
                    if (response.isSuccessful()) {
                        Toast.makeText(applicationContext, "แก้ไขกระเป๋าเสร็จสิ้น", Toast.LENGTH_SHORT).show()
                        var i: Intent = Intent(applicationContext, OverViewActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "can't update ", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<wallet>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        } else{
            Toast.makeText(applicationContext, "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show()
        }


    }
}