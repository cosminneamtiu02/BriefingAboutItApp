package Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Entities.Article;

public class FirestoreUtils {

    private final Article article;

    private final CollectionReference path;

    public FirestoreUtils(Article article, Context context) {
        this.article = article;
        FirebaseDataBindings dbBinding = new FirebaseDataBindings();
        FirebaseFirestore db = dbBinding.getDatabaseReference();
        EntitiesUtils entitiesUtils = new EntitiesUtils(context);
        this.path = db.collection("Users" ).document(entitiesUtils.getEmailFromShPref()).collection("Articles");
    }

    public void commitArticle(Context context){
        this.path.document(article.getArticleId()).set(article)
                .addOnFailureListener(e -> Toast.makeText(context,"Error submitting article!",Toast.LENGTH_SHORT).show());
    }

    public void deleteArticle(Context context){
        this.path.document(article.getArticleId()).delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(context,"Article deleted successfully!",Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context,"Error deleting article!",Toast.LENGTH_SHORT).show());

    }

    public CollectionReference getPath() {
        return path;
    }
}