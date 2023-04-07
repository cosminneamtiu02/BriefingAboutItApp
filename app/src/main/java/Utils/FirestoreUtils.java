package Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Entities.Article;
import Entities.Image;
import Entities.Title;

public class FirestoreUtils {

    private final Article article;

    private final FirebaseFirestore db;

    public FirestoreUtils(Article article) {
        this.article = article;
        FirebaseDataBindings dbBinding = new FirebaseDataBindings();
        this.db = dbBinding.getDatabaseReference();
    }

    public void commitArticle(Context context){
        db.collection("Users" ).document(article.getCreator()).collection("Articles").document(article.getArticleId())
                .set(article)
                .addOnSuccessListener(aVoid -> Toast.makeText(context,"Article submitted successfully!",Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context,"Error submitting article!",Toast.LENGTH_SHORT).show());
    }

    public void updateArticle(){

    }

    public void deleteArticle(){

    }


}