package com.example.project_pao

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_pao.databinding.CategoryItemLayoutBinding

class CategoryAdapter(val items: List<category>, val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: CategoryItemLayoutBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            binding.cardView.setOnClickListener {
                val item = items[adapterPosition]
                val contextView:Context = view.context
                Toast.makeText(context, "รายการที่เลือก : ${item.name} ",Toast.LENGTH_SHORT).show()
                val item_selected = item.name
                val type_id = item.type_id
                val category_id  = item.id
                var i: Intent = Intent(contextView,TransactionActivity::class.java)
                i.putExtra("item_selected",item_selected)
                i.putExtra("type_id",type_id)
                i.putExtra("cate_id",category_id)
                contextView.startActivity(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val binding =
            CategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

        binding_holder.title?.text = items[position].name
        Glide.with(context).load(items[position].icon).into(binding_holder.imageMovie)

        val type =items[position].type_id
        if (type =="1"){
            binding_holder.cardView.backgroundTintList = context.resources.getColorStateList(R.color.blue)
        }else{
            binding_holder.cardView.backgroundTintList = context.resources.getColorStateList(R.color.pink)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}