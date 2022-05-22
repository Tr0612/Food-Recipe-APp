package com.example.foodappmain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodappmain.R
import com.example.foodappmain.entities.MealsItems
import kotlinx.android.synthetic.main.item_rv_main_category.view.*

class SubCategoryAdapter:RecyclerView.Adapter<SubCategoryAdapter.RecipeViewHolder>() {

    var listener: OnItemClickListener? =null
    var ctx : Context?=null
    var arrSubCategory = ArrayList<MealsItems>()
    class RecipeViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    fun setData(arrData : List<MealsItems>){
        arrSubCategory = arrData as ArrayList<MealsItems> /* = java.util.ArrayList<com.example.foodappmain.entities.Recipes> */

    }

    fun setClickListener(listener1 : OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_sub_category,parent,false))    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.itemView.tv_dish_name.text = arrSubCategory[position].strMeal
        Glide.with(ctx!!).load(arrSubCategory[position].strMealThumb).into(holder.itemView.img_dish)

        holder.itemView.rootView.setOnClickListener{
            listener!!.onClicked(arrSubCategory[position].idMeal)

        }
    }

    override fun getItemCount(): Int {
       return arrSubCategory.size
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}