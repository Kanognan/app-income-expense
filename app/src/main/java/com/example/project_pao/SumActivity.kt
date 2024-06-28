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
import com.example.project_pao.databinding.ActivitySumBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SumActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySumBinding
    var createClient = userApi.create()
    lateinit var session: SessionManager
    var summ :Double = 0.0
    //    var calper_expent :Int?=null
    var balance :Double=0.0
    var all_income :Double=0.0
    var all_expent :Double=0.0

    var calper_expent :Double= 0.0
    var calper_income :Double=0.0

    // on belowall_income line we are creating
    // variables for our pie chart
    lateinit var pieChart: PieChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySumBinding.inflate(layoutInflater)
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
    override fun onResume() {
        super.onResume()
        callWallet()
    }

    fun callWallet(){
        session = SessionManager(applicationContext)
        val id:String? = session.pref.getString(SessionManager.KEY_ID,null)
        createClient.retrieveWalletID(id.toString())
            .enqueue(object : Callback<wallet> {
                override fun onResponse(call: Call<wallet>, response: Response<wallet>) {
                    if (response.isSuccessful) {
//                        binding.tvBalance.text = (response.body()?.balance.toString())
//                        binding.tvAllinCome.text = (response.body()?.all_income.toString())
//                        binding.tvAllExpent.text = (response.body()?.all_expent.toString())
//                        binding.tvSum.text.toString().toInt()
                        all_income = (response.body()?.all_income.toString().toDouble())
                        balance = (response.body()?.balance.toString().toDouble())
                        all_expent = (response.body()?.all_expent.toString().toDouble())
                        //        คำนวณเปอร์เซ็น รายจ่ายหารรายรับคูณ 100 = %รายจ่าย ลบกับ100 ก็จะได้ของรายรับ

//                        val summ:Double = all_expent.toDouble() / all_income.toDouble()
//                        calper_expent = summ * 100
//                        calper_income = 100 - calper_expent
//                        binding.tvSum.text = calper_income.toString()

                        binding.come1.text = all_income.toString()
                        binding.cost1.text = all_expent.toString()

                        // on below line we are initializing our
                        // variable with their ids.
                        pieChart = findViewById(R.id.pieChart)

                        // on below line we are setting user percent value,
                        // setting description as enabled and offset for pie chart
                        pieChart.setUsePercentValues(true)
                        pieChart.getDescription().setEnabled(false)
                        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

                        // on below line we are setting drag for our pie chart
                        pieChart.setDragDecelerationFrictionCoef(0.95f)

                        // on below line we are setting hole
                        // and hole color for pie chart
                        pieChart.setDrawHoleEnabled(true)
                        pieChart.setHoleColor(Color.WHITE)

                        // on below line we are setting circle color and alpha
                        pieChart.setTransparentCircleColor(Color.WHITE)
                        pieChart.setTransparentCircleAlpha(110)

                        // on  below line we are setting hole radius
                        pieChart.setHoleRadius(58f)
                        pieChart.setTransparentCircleRadius(61f)

                        // on below line we are setting center text
                        pieChart.setDrawCenterText(true)

                        // on below line we are setting
                        // rotation for our pie chart
                        pieChart.setRotationAngle(0f)

                        // enable rotation of the pieChart by touch
                        pieChart.setRotationEnabled(true)
                        pieChart.setHighlightPerTapEnabled(true)

                        // on below line we are setting animation for our pie chart
                        pieChart.animateY(1400, Easing.EaseInOutQuad)

                        // on below line we are disabling our legend for pie chart
                        pieChart.legend.isEnabled = false
                        pieChart.setEntryLabelColor(Color.WHITE)
                        pieChart.setEntryLabelTextSize(12f)


                        val summ = all_expent.toString().toDouble() + all_income.toString().toDouble()

                        calper_expent = (all_expent.toString().toDouble()/summ.toString().toDouble()) * 100
                        calper_income = (all_income.toString().toDouble()/summ.toString().toDouble()) * 100

//                        binding.tvSum.text = calper_expent.toString()



                        val pieEntries: ArrayList<PieEntry> = ArrayList()
                        val label = "type"

                        //initializing data

                        //initializing data
                        val typeAmountMap: MutableMap<String, Double> = HashMap()
                        typeAmountMap["รายรับ"] = calper_income
                        typeAmountMap["รายจ่าย"] = calper_expent

                        //initializing colors for the entries

                        //initializing colors for the entries
                        val colors: ArrayList<Int> = ArrayList()
                        colors.add(Color.parseColor("#FFF44336"))
                        colors.add(Color.parseColor("#FF36A63B"))

                        //input data and fit data into pie chart entry
                        //input data and fit data into pie chart entry
                        for (type in typeAmountMap.keys) {
                            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
                        }

                        //collecting the entries with label name
                        //collecting the entries with label name
                        val pieDataSet = PieDataSet(pieEntries, label)
                        //setting text size of the value
                        //setting text size of the value
                        pieDataSet.valueTextSize = 12f
                        //providing color list for coloring different entries
                        //providing color list for coloring different entries
                        pieDataSet.colors = colors
                        //grouping the data set from entry to chart
                        //grouping the data set from entry to chart
                        val pieData = PieData(pieDataSet)
                        //showing the value of the entries, default true if not set
                        //showing the value of the entries, default true if not set
                        pieData.setDrawValues(true)

                        pieChart.data = pieData
                        pieChart.invalidate()


                    } else {
                        Toast.makeText(applicationContext, "Not Found",
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

    fun Cancel(view: View) {
        val intent = Intent(this,OverViewActivity::class.java)
        this.startActivity(intent)

    }
    private fun replaceActivity(activity: AppCompatActivity){
        var i : Intent = Intent(applicationContext,activity::class.java)
        startActivity(i)
        finish()
    }
    private fun actionBarCustomTitle(): TextView? {
        return TextView(this).apply { text = "สรุปรวม"
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

}