package Utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.briefingaboutitapp.LogInActivity;
import com.example.briefingaboutitapp.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class AuthUtils {

    private final FirebaseDataBindings connection;

    public AuthUtils() {
        this.connection = new FirebaseDataBindings();
    }

    public void register(Context context , String email, String password){
        connection.getFirebaseAuth().createUserWithEmailAndPassword(email,password).

                addOnCompleteListener((Activity) context, task -> {
                    Log.v("pointReached", "withinOnCompleteListenerBeforeIF");

                    if(task.isSuccessful()){

                        Map<String, String> user = new HashMap<>();

                        user.put("email", email);

                        connection.getDatabaseReference().collection("Users").document(email)
                                .set(user)
                                .addOnSuccessListener(aVoid -> Log.d("UserCreateState","User added"))
                                .addOnFailureListener(e -> Log.d("UserCreateState","Failed to add user"));

                        Toast.makeText(context,"Registration successful", Toast.LENGTH_SHORT).show();
                        Intent goToLogInPage = new Intent(context, LogInActivity.class);
                        context.startActivity(goToLogInPage);
                    }
                    else{
                        Toast.makeText(context,"Registration failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logIn(Context context , String email, String password){
        connection.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {

                        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("email", email);
                        myEdit.apply();
                        Intent friendsListIntent  = new Intent(context, MainActivity.class);
                        context.startActivity(friendsListIntent);
                        Toast.makeText(context,"Log In successful.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context,"Log In failed.", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public void logout(Context context){
        //delete user from shared pref
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        sh.edit().remove("email").apply();

        //logout from firebase
        connection.getFirebaseAuth().signOut();

        //redirect to log in
        context.startActivity(new Intent(context, LogInActivity.class));
    }
}
