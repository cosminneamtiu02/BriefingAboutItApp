package Utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import Entities.Article;

public class EntitiesUtils {

    Context context;

    public EntitiesUtils(Context context) {
        this.context = context;
    }

    public void updateArticleShPref(Article myArticle){

        Gson gson = new Gson();
        String newArticleAsJson = gson.toJson(myArticle);
        SharedPreferences sh = this.context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putString("ARTICLE", newArticleAsJson);
        myEdit.apply();
    }

    public Article getArticleFromShPref(){
        Gson gson = new Gson();
        SharedPreferences sh = this.context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String articleAsJSON = sh.getString("ARTICLE", "");
        return gson.fromJson(articleAsJSON, Article.class);
    }

    public void dropArticleFromShPref(){
        SharedPreferences sh = this.context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        editor.remove("ARTICLE");
        editor.apply();
    }
}
