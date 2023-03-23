package Utils;


import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DesignUtils {
    private final View view;

    public DesignUtils(View view) {
        this.view = view;
    }

    public void closeKeyBoard(){
        InputMethodManager imm = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
