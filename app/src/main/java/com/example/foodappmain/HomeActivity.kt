package com.example.foodappmain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappmain.adapter.MainCategoryAdapter
import com.example.foodappmain.adapter.SubCategoryAdapter
import com.example.foodappmain.database.RecipeDatabase
import com.example.foodappmain.entities.MealsItems
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    var arrMainCategory = ArrayList<CategoryItems>()
    var arrSubCategory = ArrayList<MealsItems>()
    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        getDataFromDb()

        mainCategoryAdapter.setClickListener(onClicked)
        subCategoryAdapter.setClickListener(onClickedSubItem)

        signout.setOnClickListener(){
                Firebase.auth.signOut()
            val intent = Intent(this@HomeActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


//        arrMainCategory.add(Recipes(1,"Beef"))
//        arrMainCategory.add(Recipes(2,"Chicken"))
//        arrMainCategory.add(Recipes(3,"Dessert"))
//        arrMainCategory.add(Recipes(4,"Ice Cream"))
//        mainCategoryAdapter.setData(arrMainCategory)
//
//        arrSubCategory.add(Recipes(1,"Beef and pie"))
//        arrSubCategory.add(Recipes(2,"Chicken curry"))
//        arrSubCategory.add(Recipes(3,"Dessert pool"))
//        arrSubCategory.add(Recipes(4,"Ice Cream sucks"))
//        subCategoryAdapter.setData(arrSubCategory)

//        rv_main_category.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
//        rv_main_category.adapter = mainCategoryAdapter

//        rv_sub_category.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
//        rv_sub_category.adapter = subCategoryAdapter
    }


    private val onClicked = object  : MainCategoryAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            getMealDataFromDb(categoryName)
        }

    }
    private val onClickedSubItem = object  : SubCategoryAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@HomeActivity,DetailActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }



    }
    private fun getDataFromDb(){
        launch {
            this.let {
                var cat  = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()
                arrMainCategory = cat as ArrayList<CategoryItems> /* = java.util.ArrayList<com.example.foodappmain.CategoryItems> */
                arrMainCategory.reverse()

                getMealDataFromDb(arrMainCategory[0].strcategory)
                mainCategoryAdapter.setData(arrMainCategory)
                rv_main_category.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                rv_main_category.adapter = mainCategoryAdapter
                Log.d("set","entered")
            }
        }
    }

    private fun getMealDataFromDb(categoryName:String){
        tvCategory.text = "$categoryName Category"
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealList(categoryName)
                arrSubCategory = cat as ArrayList<MealsItems>
                subCategoryAdapter.setData(arrSubCategory)
                rv_sub_category.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                rv_sub_category.adapter = subCategoryAdapter
            }


        }
    }

}