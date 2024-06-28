package com.example.project_pao

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.project_pao.databinding.ActivityTransactionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    val createClient = userApi.create()
    lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item_select = intent.getStringExtra("item_selected").toString()

        if (item_select != "null") {
            binding.select.text = item_select.toString()
        }
        val cate_id = intent.getStringExtra("cate_id")
        val amount = binding.editAmount.text.toString()
        val date = binding.addtime.text.toString()
        Log.i("test","cate_id1 ="+cate_id+", date1 = "+date +", amount1 ="+amount)
        session = SessionManager(applicationContext)
        val us_id = session.pref.getString(SessionManager.KEY_ID, null)

        createClient.getwallet(us_id.toString())
            .enqueue(object : Callback<wallet> {
                override fun onResponse(call: Call<wallet>, response: Response<wallet>) {
                    if (response.isSuccessful) {
                        binding.currency.text = (response.body()?.currentcy.toString())

                    } else {
                        Toast.makeText(
                            applicationContext, "Wallet Not Found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<wallet>, t: Throwable) {
                    Toast.makeText(
                        applicationContext, "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

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
        return TextView(this).apply { text = "เพิ่มรายการ"
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


    fun showDatePickerDialog(v: View) {
        val newDateFragment = DatePickerFragment()
        newDateFragment.show(supportFragmentManager, "Date Picker")
    }

    fun Save(view: View) {

            session = SessionManager(applicationContext)
            val us_id = session.pref.getString(SessionManager.KEY_ID,null)
            val type_id = intent.getStringExtra("type_id")

            val note = binding.editNotes.text.toString()
            var balance:Int = 0
            val item_select = intent.getStringExtra("item_selected").toString()
        val cate_id = intent.getStringExtra("cate_id")

        val date = binding.addtime.text.toString()
        val amount1 = binding.editAmount.text.toString()


        if (cate_id == null || amount1 =="" || date == "วันที่"){
            Toast.makeText(applicationContext, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show()


        }else{
            //get balance
            createClient.getwalletID(us_id.toString()).enqueue(object : Callback<wallet> {
                //            @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<wallet>,
                    response: retrofit2.Response<wallet>
                ) {
                    if (response.isSuccessful()) {
                        val amount = binding.editAmount.text.toString().toInt()
                        balance = (response.body()?.balance.toString().toInt())
//                    Toast.makeText(applicationContext, "get balance ", Toast.LENGTH_SHORT).show()
                        if(type_id =="2"){
                            if (balance < amount){
                                Toast.makeText(applicationContext, "เงินในกระเป๋ามีไม่เพียงพอ", Toast.LENGTH_SHORT).show()
                                binding.editAmount.text?.clear()
                            }else{
                                addtransaction()
                            }
                        }else{
                            addtransaction()
                        }
                    } else {
                        Toast.makeText(applicationContext, "no id wallet ", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<wallet>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure getwalletID " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

    }




//    @RequiresApi(Build.VERSION_CODES.O)
    fun addtransaction(){
        session = SessionManager(applicationContext)
        val us_id = session.pref.getString(SessionManager.KEY_ID,null)
        val wallet_id = session.pref.getString(SessionManager.KEY_WALLET,null)
        val cate_id = intent.getStringExtra("cate_id")
        val type_id = intent.getStringExtra("type_id")
        val date = binding.addtime.text.toString()

    val datenewA = SimpleDateFormat("yyyy-MM-dd").format(SimpleDateFormat("MM/dd/yy").parse(date))



        val amount = binding.editAmount.text.toString()
        val note = binding.editNotes.text.toString()

        Log.i("test","cate_id ="+cate_id+", date = "+datenewA +", amount ="+amount)
        createClient.inserttransaction(
            wallet_id.toString(),cate_id.toString(),type_id.toString(),datenewA.toString(),amount.toInt(),note
        ).enqueue(object : Callback<transaction> {
            override fun onResponse(
                call: Call<transaction>, response: Response<transaction>
            ) {
                if (response.isSuccessful()) {
                    createClient.getwalletID(us_id.toString()).enqueue(object : Callback<wallet> {
                        override fun onResponse(
                            call: Call<wallet>,
                            response: retrofit2.Response<wallet>
                        ) {
                            if (response.isSuccessful()) {
                                var all_income = (response.body()?.all_income.toString().toInt())
                                var all_expent = (response.body()?.all_expent.toString().toInt())
                                var balance  =  (response.body()?.balance.toString().toInt())
                                if (type_id =="1"){
                                    balance += amount.toInt()
                                    all_income += amount.toInt()
                                }else{
                                    balance -= amount.toInt()
                                    all_expent += amount.toInt()
                                }
                                createClient.updateWallet2(
                                    us_id.toString(),balance,all_income,all_expent
                                ).enqueue(object : Callback<wallet> {
                                    override fun onResponse(
                                        call: Call<wallet>,
                                        response: retrofit2.Response<wallet>
                                    ) {
                                        if (response.isSuccessful()) {
//                                            Toast.makeText(applicationContext, "update finish", Toast.LENGTH_SHORT).show()
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


//                                Toast.makeText(applicationContext, "get balance ", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "no id wallet ", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<wallet>, t: Throwable) {
                            Toast.makeText(
                                applicationContext,
                                "Error onFailure getwalletID " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })

                    Toast.makeText(applicationContext, "เพิ่มรายการแล้ว", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(applicationContext, "Error transaction", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<transaction>, t: Throwable) {
                Toast.makeText(applicationContext, "Error onFailure transaction " + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
        var io: Intent = Intent(applicationContext,OverViewActivity::class.java)
        startActivity(io)
    }

    fun showadd_income(view: View) {
        val id = "1"
        var ii: Intent = Intent(applicationContext,CategoryActivity::class.java)
        ii.putExtra("pID",id)
        startActivity(ii)
    }

    fun showadd_expent(view: View) {
        val id = "2"
        var iii: Intent = Intent(applicationContext,CategoryActivity::class.java)
        iii.putExtra("pID",id)
        startActivity(iii)
    }
    fun back(view: View) {
        var io: Intent = Intent(applicationContext,OverViewActivity::class.java)
        startActivity(io)
    }

}