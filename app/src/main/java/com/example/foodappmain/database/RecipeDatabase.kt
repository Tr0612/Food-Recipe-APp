package com.example.foodappmain.database



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodappmain.Category
import com.example.foodappmain.CategoryItems
import com.example.foodappmain.dao.RecipeDao
import com.example.foodappmain.entities.Meal
import com.example.foodappmain.entities.MealsItems
import com.example.foodappmain.entities.Recipes
import com.example.foodappmain.entities.converter.CategoryListConverter
import com.example.foodappmain.entities.converter.MealListConverter


@Database(entities = [Recipes::class,CategoryItems::class,Category::class,Meal::class,MealsItems::class], version = 1, exportSchema = false)
@TypeConverters(CategoryListConverter::class, MealListConverter::class)
abstract class RecipeDatabase:RoomDatabase() {
    companion object{
        var recipeDatabase:RecipeDatabase? = null

        @Synchronized
        fun getDatabase(context: Context):RecipeDatabase{
            if(recipeDatabase==null){
                recipeDatabase = Room.databaseBuilder(
                    context,
                    RecipeDatabase::class.java,
                    "recipe.db",
                ).build()
            }
            return recipeDatabase!!
        }
    }
    abstract fun recipeDao():RecipeDao
}