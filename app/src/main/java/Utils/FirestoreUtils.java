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
        Article temp = customTestObject();
        db.collection("Users" ).document(article.getCreator()).collection("Articles").document(article.getArticleId())
                .set(article)
                .addOnSuccessListener(aVoid -> Toast.makeText(context,"Article submitted successfully!",Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context,"Error submitting article!",Toast.LENGTH_SHORT).show());

    }

    public void updateArticle(){

    }

    public void deleteArticle(){

    }

    private Map<Object, Object> getArticleAsMap(){
        Map<Object, Object> articleAsMap = new HashMap<>();
        articleAsMap.put("name", "Los Angeles");
        articleAsMap.put("state", "CA");
        articleAsMap.put("country", "USA");
        return articleAsMap;
    }

    private Article customTestObject(){
        Title title = new Title();
        Article article1 = new Article(article.getCreator());

        //Paragraph p1 = new Paragraph("1", title,"", "");
        //Paragraph p2 = new Paragraph("2", title,"", "");
        //article1.addNewParagraph(p1);
        //article1.addNewParagraph(p2);

        Image i1 = new Image(article.getArticleId(), "", "", "", false, new ArrayList<>());
        Image i2 = new Image(article.getArticleId() + "plm", "", "", "", false, new ArrayList<>());
        article1.addNewImage(i1);
        article1.addNewImage(i2);
        return article1;
    }
}