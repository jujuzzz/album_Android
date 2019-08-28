package pig.stinky.com.gallery.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import androidx.appcompat.app.AlertDialog;

public class DialogHelper {

    public static AlertDialog buildCustomViewDialog(Context context, String title, View view, DialogInterface.OnClickListener confirmListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, confirmListener)
                .create();
    }

    public static AlertDialog buildDeleteDialog(Context context, String message, DialogInterface.OnClickListener confirmListener) {
        return new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, confirmListener)
                .create();
    }

}
