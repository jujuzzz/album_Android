package pig.stinky.com.gallery.utils;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import java.io.File;

public class UriUtils {

    public static final Uri MEDIA_FILE_URI = MediaStore.Files.getContentUri("external");
    public static final String MEDIA_DATA_COLUMN = MediaStore.Files.FileColumns.DATA;

    private UriUtils() {

    }

    public static String getImagePathFromMediaUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static Uri getMediaUriFromFile(Context context, ContentProviderClient client, File
            file) {
        Uri uri = null;
        try {
            uri = getMediaUriFromFileInternal(context, client, file);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    client.close();
                } else {
                    client.release();
                }
            }
        }
        return uri;
    }

    @SuppressLint("Recycle") // cursor is closed when ContentProviderClient closed
    private static Uri getMediaUriFromFileInternal(Context context, ContentProviderClient client,
                                                   File file) throws RemoteException {
        String filePath = file.getAbsolutePath();
        Uri baseUri = MEDIA_FILE_URI;
        Cursor cursor = client.query(baseUri, new String[]{MediaStore.MediaColumns._ID},
                MediaStore.MediaColumns.DATA + "=? ", new String[]{filePath},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            // convert path to media provider uri
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

            return Uri.withAppendedPath(baseUri, Integer.toString(id));
        } else {
            if (file.exists()) {
                // convert path to file provider uri
                return FileProvider.getUriForFile(context, MEDIA_DATA_COLUMN, file);
            } else {
                // empty uri
                return Uri.parse("");
            }
        }
    }

}
