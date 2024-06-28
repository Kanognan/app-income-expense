package com.example.project_pao

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_pao.databinding.TransactionItemLayoutBinding
import java.text.SimpleDateFormat

class transactionAdapter(val items: ArrayList<transactionClass>, val context: Context) :
    RecyclerView.Adapter<transactionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View, val binding: TransactionItemLayoutBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            binding.textEdit.setOnClickListener{
                val items = items[adapterPosition]
                val contextView:Context = view.context
                val intent = Intent(contextView, Edittransaction::class.java)
                intent.putExtra("list",items.category_name)
                intent.putExtra("cate_id",items.category_id)
                intent.putExtra("date",items.createAt)
                intent.putExtra("amount" ,items.amount.toString())
                intent.putExtra("note",items.note)
                intent.putExtra("type_id",items.type_id)
                intent.putExtra("transaction_id",items.transaction_id.toString())
                contextView.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            TransactionItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: transactionAdapter.ViewHolder, position: Int) {
        val binding = holder.binding
        val datef = items[position].createAt
        Log.i("gg",datef)
        val datenewA = SimpleDateFormat("yyyy-MM-").format(SimpleDateFormat("yyyy-MM-dd").parse(datef))
        val datenewB = SimpleDateFormat("d").format(SimpleDateFormat("yyyy-MM-dd").parse(datef))
        Log.i("gg",datenewA)
        Log.i("gg",datenewB)
        val dateplus = datenewB.toInt() +9
        val datenewplus = datenewA.toString()+dateplus.toString()

        val type =items[position].type_id
        if (type =="1"){
            binding.textCost?.text = "+ "+items[position].amount.toString()
            binding.cardView3.backgroundTintList = context.resources.getColorStateList(R.color.blue)
            binding.cardView2.backgroundTintList = context.resources.getColorStateList(R.color.blue)

        }else{
            binding.textCost?.text = "- "+items[position].amount.toString()
            binding.cardView3.backgroundTintList = context.resources.getColorStateList(R.color.pink)
            binding.cardView2.backgroundTintList = context.resources.getColorStateList(R.color.pink)
        }

        binding.DetailItem?.text = items[position].category_name
        binding.nameItem?.text = items[position].note
        binding.textdate?.text = datenewplus
        Glide.with(context).load(items[position].category_icon).into(binding.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}