package Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDataBindings {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore databaseReference;

    public FirebaseDataBindings() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseFirestore.getInstance();
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseFirestore getDatabaseReference() {
        return databaseReference;
    }
}
