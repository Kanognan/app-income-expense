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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project_pao.databinding.ActivityHistorytransactionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistorytransactionBinding
    var transactionList = arrayListOf<transactionClass>()
    val createClient = userApi.create()
    lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorytransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.getContext(), DividerItemDecoration.VERTICAL
            )
        )
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
        return TextView(this).apply { text = "ธุรกรรม"
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


    override fun onResume() {
        super.onResume()
        callData()
    }

    fun callData() {
        transactionList.clear();
        session = SessionManager(applicationContext)
        val wallet_id = session.pref.getString(SessionManager.KEY_WALLET,null)
        createClient.retrievetran(wallet_id.toString())
            .enqueue(object : Callback<List<transactionClass>> {
                override fun onResponse(
                    call: Call<List<transactionClass>>, response:
                    Response<List<transactionClass>>
                ) {
                    response.body()?.forEach {
                        transactionList.add(
                            transactionClass(
                                it.transaction_id,
                                it.amount,
                                it.createAt,
                                it.type_id,
                                it.category_id,
                                it.category_name,
                                it.category_icon,
                                it.note
                            )
                        )
                    }
                    transactionList.sortByDescending {
                        it.createAt
                    }
                    binding.recyclerView.adapter =
                        transactionAdapter(transactionList, applicationContext)

                }

                override fun onFailure(call: Call<List<transactionClass>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

}

