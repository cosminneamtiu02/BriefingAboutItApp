package Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import Entities.Article;

public class FirestoreUtils {

    private final Article article;

    private final FirebaseFirestore db;

    public FirestoreUtils(Article article) {
        this.article = article;
        FirebaseDataBindings dbBinding = new FirebaseDataBindings();
        this.db = dbBinding.getDatabaseReference();
    }

    public boolean commitArticle(Context context){

        final boolean[] transactionResult = {false};

        db.runTransaction((Transaction.Function<Void>) transaction -> {


            // Success
            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(context,"Article submitted successfully!",Toast.LENGTH_SHORT).show();
            transactionResult[0] = true;
        }).addOnFailureListener(e -> {
                    Toast.makeText(context,"Error submitting article!",Toast.LENGTH_SHORT).show();
                    transactionResult[0] = false;
                });

        return  transactionResult[0];
    }

    public void updateArticle(){

    }

    public void deleteArticle(){

    }
}