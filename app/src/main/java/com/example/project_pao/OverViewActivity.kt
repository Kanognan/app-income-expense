package com.example.project_pao

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.project_pao.databinding.ActivityOverviewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class OverViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverviewBinding
    lateinit var session: SessionManager
    var wallet_id = ""
    var wallet_name :String? = null
    var currency_name :String? = null
    val createClient = userApi.create()
    var transactiontList = arrayListOf<transaction>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        session = SessionManager(applicationContext)
        val type_id = session.pref.getString(SessionManager.KEY_ID, null)
        wallet_id = session.pref.getString(SessionManager.KEY_WALLET, null).toString()


        binding.date.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        createClient.getwallet(type_id.toString())
            .enqueue(object : Callback<wallet> {
                override fun onResponse(call: Call<wallet>, response: Response<wallet>) {
                    if (response.isSuccessful) {
                        binding.text1.text = (response.body()?.all_income.toString())
                        binding.text2.text = (response.body()?.all_expent.toString())
                        binding.text3.text = (response.body()?.balance.toString())
                        binding.cur1.text = (response.body()?.currentcy.toString())
                        binding.cur2.text = (response.body()?.currentcy.toString())
                        binding.cur3.text = (response.body()?.currentcy.toString())
                        binding.cure1.text = (response.body()?.currentcy.toString())
                        binding.cure2.text = (response.body()?.currentcy.toString())
                        binding.Account.text = (response.body()?.wallet_name.toString())

                        wallet_name = (response.body()?.wallet_name.toString())
                        currency_name = (response.body()?.currentcy.toString())
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

        binding.editwallet.setOnClickListener() {
            val intent = Intent(applicationContext, EditwalletActivity::class.java)
            intent.putExtra("wallet_name",wallet_name)
            intent.putExtra("currentcy",currency_name)
            startActivity(intent)
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
        return TextView(this).apply { text = "ภาพรวม"
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
        callTransactionData()
    }

    fun callTransactionData() {

        transactiontList.clear();
        createClient.gettransaction(wallet_id)
            .enqueue(object : Callback<List<transaction>> {
                override fun onResponse(
                    call: Call<List<transaction>>,
                    response: Response<List<transaction>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.forEach {

                            transactiontList.add(
                                transaction(
                                    it.transaction_id,
                                    it.amount,
                                    it.note,
                                    it.wallet_id,
                                    it.category_id,
                                    it.type_id,
                                    it.createAt,
//

                                )
                            )

                        }
                        calculate(transactiontList)
                    }
                }

                override fun onFailure(call: Call<List<transaction>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()

                }
            })
    }
    fun now(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    fun calculate(transactiontList: ArrayList<transaction>) {

        var income_today: Int = 0
        var expent_today: Int = 0
        for (i in 0..transactiontList.size - 1) {
            var amount: Int = transactiontList[i].amount
            var datef = transactiontList[i].createAt
            var type_id = transactiontList[i].type_id


            val currentDay: String = SimpleDateFormat("yyyy-MM-d", Locale.getDefault()).format(Date())


            val datenewA = SimpleDateFormat("yyyy-MM-").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(datef))
            val datenewB =
                SimpleDateFormat("d").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(datef))
            val dateplus = datenewB.toInt() + 1
            val datenewplus = datenewA.toString() + dateplus.toString()


            val currentDayReal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//            Log.i("Event", "day database s :"+datenewA )
//            Log.i("Event", "day fun :"+dateplus )

            if (datenewplus == currentDay) {
                if (type_id == "1") {
                    income_today += amount
                } else {
                    expent_today += amount
                }
            }

        }
        binding.come1.text = income_today.toString()
        binding.cost1.text = expent_today.toString()

    }


}


