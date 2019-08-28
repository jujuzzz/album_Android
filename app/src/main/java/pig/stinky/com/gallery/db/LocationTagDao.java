package pig.stinky.com.gallery.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import pig.stinky.com.gallery.bean.LocationTag;
import pig.stinky.com.gallery.bean.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationTagDao {

    public static final int INDEX_TAG_PHOTO_PATH = 0;
    public static final int INDEX_TAG_NAME = 1;
    public static final int INDEX_TAG_ALBUM = 2;

    private LocationTagDao() {

    }

    private static void runRawSql(List<String> sqls) {
        SQLiteDatabase db = null;

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            for (String sql : sqls) {
                db.execSQL(sql);
            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                DatabaseManager.getInstance().closeDatabase();
            }
        }
    }

    public static void addTag(LocationTag tag) {
        String sql = "INSERT INTO `locationtag` (`photoname`,`value`,`albumorigin`)  VALUES ('"
                + tag.getPhoto().getFullPath()
                + "','"
                + tag.getValue()
                + "','"
                + tag.getPhoto().getAlbumName()
                + "')";

        runRawSql(Collections.singletonList(sql));
    }

    public static void deleteTag(LocationTag tag) {
        String sql = "DELETE FROM `locationtag` WHERE `photoname` ='"
                + tag.getPhoto().getFullPath()
                + "' and `value` = '"
                + tag.getValue()
                + "' and `albumorigin` = '"
                + tag.getPhoto().getAlbumName()
                + "'";
        runRawSql(Collections.singletonList(sql));
    }

    public static List<LocationTag> getPhotoLags(Photo photo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<LocationTag> ret = new ArrayList<>();

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT * FROM `locationtag` WHERE `photoname` = '"
                    + photo.getFullPath()
                    + "'";
            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                LocationTag tag = new LocationTag(cursor.getString(INDEX_TAG_NAME), new Photo(new File(cursor.getString(INDEX_TAG_PHOTO_PATH)), cursor.getString(INDEX_TAG_ALBUM)));
                ret.add(tag);
            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                DatabaseManager.getInstance().closeDatabase();
            }

            if (cursor != null) {
                cursor.close();
            }
        }

        return ret;
    }

    public static List<LocationTag> getAllTags() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<LocationTag> ret = new ArrayList<>();

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT * FROM `locationtag`";
            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                LocationTag tag = new LocationTag(cursor.getString(INDEX_TAG_NAME), new Photo(new File(cursor.getString(INDEX_TAG_PHOTO_PATH)), cursor.getString(INDEX_TAG_ALBUM)));
                ret.add(tag);
            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                DatabaseManager.getInstance().closeDatabase();
            }

            if (cursor != null) {
                cursor.close();
            }
        }

        return ret;
    }
}
