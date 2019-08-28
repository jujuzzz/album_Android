package pig.stinky.com.gallery.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import pig.stinky.com.gallery.bean.Album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlbumDao {

    public static final int INDEX_ALBUM_NAME = 0;

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

    public static void addAlbum(Album album) {
        String sql = "INSERT INTO `album` (`albumname`) VALUES ('"
                + album.getAlbumName()
                + "')";
        runRawSql(Collections.singletonList(sql));
    }

    public static void deleteAlbum(Album album) {
        String deletePersonTagSql = "DELETE FROM `persontag` WHERE `albumsource` ='"
                + album.getAlbumName()
                + "'";

        String deleteLocationTagSql = "DELETE FROM `locationtag` WHERE `albumorigin` ='"
                + album.getAlbumName()
                + "'";

        String deletePhotoSql = "DELETE FROM `photo` WHERE (`albumid`) ='"
                + album.getAlbumName() + "'";

        String deleteAlbumSql = "DELETE FROM `album` WHERE (`albumname`) ='"
                + album.getAlbumName() + "'";

        List<String> sqls = new ArrayList<>();
        sqls.add(deletePersonTagSql);
        sqls.add(deleteLocationTagSql);
        sqls.add(deletePhotoSql);
        sqls.add(deleteAlbumSql);

        runRawSql(sqls);
    }

    public static void renameAlbum(Album album, String name) {
        String updatePhotoSql = "UPDATE `photo` SET `albumid` = '"
                + name + "' WHERE `albumid` = '"
                + album.getAlbumName() + "'";

        String updateAlbumSql = "UPDATE `album` SET `albumname` = '"
                + name + "' WHERE `albumname` = '"
                + album.getAlbumName() + "'";

        List<String> sqls = new ArrayList<>();
        sqls.add(updatePhotoSql);
        sqls.add(updateAlbumSql);

        runRawSql(sqls);
    }

    public static List<Album> loadAlbum() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        List<Album> ret = new ArrayList<>();

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT * FROM `album`";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Album album = new Album(cursor.getString(INDEX_ALBUM_NAME));
                ret.add(album);
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
