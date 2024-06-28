package com.example.project_pao

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.example.project_pao.databinding.ActivityEdittransactionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class Edittransaction : AppCompatActivity() {
    private lateinit var binding: ActivityEdittransactionBinding
    val createClient = userApi.create()
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdittransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = intent.getStringExtra("list")
        val date = intent.getStringExtra("date")
        val amount = intent.getStringExtra("amount")
        val note = intent.getStringExtra("note")
        val type_id = intent.getStringExtra("type_id")
        val transaction_id = intent.getStringExtra("transaction_id")
        session = SessionManager(applicationContext)
        val us_id = session.pref.getString(SessionManager.KEY_ID,null)


        val datenewA = SimpleDateFormat("yyyy-MM-").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date))
        val datenewB =
            SimpleDateFormat("d").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date))
        val dateplus = datenewB.toInt() + 9
        val datenewplus = datenewA.toString() + dateplus.toString()
        val newdate = SimpleDateFormat("MM/dd/yy").format(SimpleDateFormat("yyyy-MM-d").parse(datenewplus))

         binding.select.setText(list)
        binding.addtime.setText(newdate)
        binding.editAmount.setText(amount)
        binding.editNotes.setText(note)

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
                        balance -= amount.toString().toInt()
                        all_income -= amount.toString().toInt()
                    }else{
                        balance += amount.toString().toInt()
                        all_expent -= amount.toString().toInt()
                    }
                    createClient.updateWallet2(
                        us_id.toString(),balance,all_income,all_expent
                    ).enqueue(object : Callback<wallet> {
                        override fun onResponse(
                            call: Call<wallet>,
                            response: retrofit2.Response<wallet>
                        ) {
                            if (response.isSuccessful()) {
//                                Toast.makeText(applicationContext, "update finish level 1", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(applicationContext, "can't update level 1  ", Toast.LENGTH_SHORT).show()
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

        binding.cardViewdelete.setOnClickListener(){
            val myBuilder = AlertDialog.Builder(this)
            myBuilder.apply {
                setTitle("Warning")
                setMessage("หากลบรายการนี้แล้วจะไม่สารถกู้คืนได้ ต้องการลบรายการนี้หรือไม่")
                setNegativeButton("ใช่"){dialog, which->
                    createClient.deletetran(transaction_id.toString()).enqueue(object : Callback<transaction>{
                        override fun onResponse(call: Call<transaction>, response: Response<transaction>) {
                            if (response.isSuccessful){
                                Toast.makeText(applicationContext,"ลบรายการเสร็จสิ้น",Toast.LENGTH_LONG).show()
                                var i: Intent = Intent(applicationContext, HistoryTransactionActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<transaction>, t: Throwable) {
                            Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                        }

                    })
                    finish()
                }
                setPositiveButton("ไม่"){dialog,which->dialog.cancel()}
                show()
            }
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
                40, // left padding
                0, // top padding
                0, // right padding
                0 // bottom padding
            )
        }


    }
    private fun actionBarCustomTitle(): TextView? {
        return TextView(this).apply { text = "แก้ไขรายการ"
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
        val cate_id = intent.getStringExtra("cate_id")
        val type_id = intent.getStringExtra("type_id")
        val date = binding.addtime.text.toString()
        val amount1 = binding.editAmount.text.toString()

        val note = binding.editNotes.text.toString()
        var balance:Int = 0
        val item_select = intent.getStringExtra("item_selected").toString()
        if ( amount1 =="" ){
            Toast.makeText(applicationContext, "กรุณากรอกจำนวนเงิน", Toast.LENGTH_SHORT).show()
        }else{
            //get balance
            createClient.getwalletID(us_id.toString()).enqueue(object : Callback<wallet> {
                override fun onResponse(
                    call: Call<wallet>,
                    response: retrofit2.Response<wallet>
                ) {
                    if (response.isSuccessful()) {
                        balance = (response.body()?.balance.toString().toInt())
//                    Toast.makeText(applicationContext, "get balance ", Toast.LENGTH_SHORT).show()
                        if(type_id =="2"){
                            val amount = binding.editAmount.text.toString().toInt()
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

    fun addtransaction(){

        session = SessionManager(applicationContext)
        val us_id = session.pref.getString(SessionManager.KEY_ID,null)
        val wallet_id = session.pref.getString(SessionManager.KEY_WALLET,null)
        val cate_id = intent.getStringExtra("cate_id")
        val type_id = intent.getStringExtra("type_id")
        val transaction_id = intent.getStringExtra("transaction_id")
        val date = binding.addtime.text.toString()

        val datenewA = SimpleDateFormat("yyyy-MM-dd").format(SimpleDateFormat("MM/dd/yy").parse(date))
        val amount = binding.editAmount.text.toString()
        val note = binding.editNotes.text.toString()





        createClient.updatetran(transaction_id.toString(),datenewA,amount.toInt(),note).enqueue(object : Callback<transaction> {
            override fun onResponse(
                call: Call<transaction>,
                response: retrofit2.Response<transaction>
            ) {

                if (response.isSuccessful()) {
                    Toast.makeText(applicationContext, "แก้ไขรายการเสร็จสิ้น", Toast.LENGTH_SHORT).show()
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
//                                            Toast.makeText(applicationContext, "update finish wallet", Toast.LENGTH_SHORT).show()
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

                    var i: Intent = Intent(applicationContext, HistoryTransactionActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "no id wallet ", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<transaction>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Error onFailure getwalletID " + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })


//        var io: Intent = Intent(applicationContext,OverViewActivity::class.java)
//        startActivity(io)
    }


    fun back(view: View) {
        val transaction_id = intent.getStringExtra("transaction_id")
        val amount = intent.getStringExtra("amount")
        val type_id = intent.getStringExtra("type_id")
        session = SessionManager(applicationContext)
        val us_id = session.pref.getString(SessionManager.KEY_ID,null)
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
                        balance += amount.toString().toInt()
                        all_income += amount.toString().toInt()
                    }else{
                        balance -= amount.toString().toInt()
                        all_expent += amount.toString().toInt()
                    }
                    createClient.updateWallet2(
                        us_id.toString(),balance,all_income,all_expent
                    ).enqueue(object : Callback<wallet> {
                        override fun onResponse(
                            call: Call<wallet>,
                            response: retrofit2.Response<wallet>
                        ) {
                            if (response.isSuccessful()) {
//                                Toast.makeText(applicationContext, "update finish8", Toast.LENGTH_SHORT).show()
                                var i: Intent = Intent(applicationContext, HistoryTransactionActivity::class.java)
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

    }
}