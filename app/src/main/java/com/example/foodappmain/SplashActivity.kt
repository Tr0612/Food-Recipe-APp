package com.example.foodappmain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.foodappmain.database.RecipeDatabase
import com.example.foodappmain.entities.Meal
import com.example.foodappmain.entities.MealsItems
import com.example.foodappmain.interfaces.GetDataService
import com.example.foodappmain.retrofitclient.RetrofitClientInstance
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity(),EasyPermissions.RationaleCallbacks,EasyPermissions.PermissionCallbacks{

    private var READ_STORAGE_PERM = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        readStorageTask()

        btnGetStarted.setOnClickListener {
            var intent = Intent(this@SplashActivity,HomeActivity::class.java)
            startActivity(intent)
            finish()

        }

    }


    fun getCategories(){
        Log.d("cat","succ")
        val service = RetrofitClientInstance.retrofitInstance.create(GetDataService::class.java)
        val call = service.getCategoryList()
        call.enqueue(object : Callback<Category>{

            override fun onResponse(
                call: Call<Category>,
                response: Response<Category>
            ) {
                for (arr in response.body()!!.categorieitems!!){
                    Log.d("s",arr.toString())
                    getMeal(arr.strcategory)
                }
                insertDataIntoRoomDb(response.body())
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Toast.makeText(this@SplashActivity,"Something wrong",Toast.LENGTH_LONG).show()
            }

        }
        )
    }


    fun getMeal(categoryName:String){
        val service = RetrofitClientInstance.retrofitInstance.create(GetDataService::class.java)
        val call = service.getMealList(categoryName)
        call.enqueue(object : Callback<Meal>{

            override fun onResponse(
                call: Call<Meal>,
                response: Response<Meal>
            ) {
                Log.d("getmeal",categoryName)
                insertMealDataIntoRoomDb(categoryName,response.body())
            }

            override fun onFailure(call: Call<Meal>, t: Throwable) {
                loader.visibility = View.INVISIBLE

                Toast.makeText(this@SplashActivity,"Something wrong",Toast.LENGTH_LONG).show()
            }

        }
        )
    }


    private fun insertMealDataIntoRoomDb(categoryName:String,meal: Meal?) {
        launch {
            this.let {
                for (arr in meal!!.mealsItem!!){
                    var mealItemModel = MealsItems(
                        arr.id,
                        arr.idMeal,
                        categoryName,
                        arr.strMeal,
                        arr.strMealThumb
                    )
                    Log.d("into db",arr.toString())
                    RecipeDatabase.getDatabase(this@SplashActivity).recipeDao().insertMeal(mealItemModel)
                    Log.d("mealDb",arr.toString())
                }
                btnGetStarted.visibility = View.VISIBLE


            }
        }
    }

    private fun insertDataIntoRoomDb(category: Category?) {

       launch {
            this.let {

                for (arr in category!!.categorieitems!!){
//                    Log.d("into db",arr.toString())
                    RecipeDatabase.getDatabase(this@SplashActivity).recipeDao().insertCategory(arr)
                }

            }
        }

    }

    fun clearDatabase(){
        launch {
            this.let {
                RecipeDatabase.getDatabase(this@SplashActivity).recipeDao().clearDb()
            }
        }
    }

    private fun hasReadStoragePermission():Boolean{
        return EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun readStorageTask(){
        Log.d("readStoragetask","succ")
        if(hasReadStoragePermission()){
            clearDatabase()
            getCategories()
        }else{
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to storage",
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }


    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
    }
}