package pig.stinky.com.gallery.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import pig.stinky.com.gallery.bean.Album;
import pig.stinky.com.gallery.bean.LocationTag;
import pig.stinky.com.gallery.bean.PersonTag;
import pig.stinky.com.gallery.bean.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhotoDao {

    public static final int INDEX_IMAGE_PATH = 1;
    public static final int INDEX_ALBUM_NAME = 0;

    private PhotoDao() {

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

    public static List<Photo> loadPhotos(Album album) {
        List<Photo> ret = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT * FROM `photo` WHERE `albumid` = '"
                    + album.getAlbumName()
                    + "'";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Photo photo = new Photo(new File(cursor.getString(INDEX_IMAGE_PATH)), cursor.getString(INDEX_ALBUM_NAME));
                setPhotoTag(db, photo);
                ret.add(photo);
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

    public static List<Photo> loadAllPhotos() {
        List<Photo> ret = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT * FROM `photo`";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Photo photo = new Photo(new File(cursor.getString(INDEX_IMAGE_PATH)), cursor.getString(INDEX_ALBUM_NAME));
                setPhotoTag(db, photo);
                ret.add(photo);
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

    public static void movePhoto(Photo photo, Album src, Album dst) {
        String insertSql = "INSERT INTO `photo` (`filepath`,`albumid`)  VALUES ('"
                + photo.getFullPath()
                + "','"
                + dst.getAlbumName()
                + "')";

        String deleteSql = "DELETE FROM `photo` WHERE `albumid` ='"
                + src.getAlbumName()
                + "' and `filepath` = '"
                + photo.getFullPath()
                + "'";
        List<String> sql = new ArrayList<>();
        sql.add(insertSql);
        sql.add(deleteSql);
        runRawSql(sql);
    }

    public static void deletePhoto(Photo photo) {
        String deletePersonTagSql = "DELETE FROM `persontag` WHERE `albumsource` ='"
                + photo.getAlbumName()
                + "' and `photoname` = '"
                + photo.getFullPath()
                + "'";

        String deleteLocationTagSql = "DELETE FROM `locationtag` WHERE `albumorigin` ='"
                + photo.getAlbumName()
                + "' and `photoname` = '"
                + photo.getFullPath()
                + "'";

        String deletePhotoSql = "DELETE FROM `photo` WHERE `albumid` ='"
                + photo.getAlbumName()
                + "' and `filepath` = '"
                + photo.getFullPath()
                + "'";

        List<String> sqls = new ArrayList<>();
        sqls.add(deletePersonTagSql);
        sqls.add(deleteLocationTagSql);
        sqls.add(deletePhotoSql);

        runRawSql(sqls);
    }

    public static void addPhoto(Photo photo) {
        String sql = "INSERT INTO `photo` (`filepath`,`albumid`)  VALUES ('"
                + photo.getFullPath()
                + "','"
                + photo.getAlbumName()
                + "')";
        runRawSql(Collections.singletonList(sql));
    }

    public static List<Photo> searchByPersonTag(String key) {
        List<Photo> ret = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT p.albumid, t1.photoname FROM ( SELECT l.photoname FROM persontag l WHERE value Like '%"
                    + key
                    + "%')t1, photo p WHERE p.filepath = t1.photoname GROUP BY t1.photoname";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Photo photo = new Photo(new File(cursor.getString(INDEX_IMAGE_PATH)), cursor.getString(INDEX_ALBUM_NAME));
                setPhotoTag(db, photo);
                ret.add(photo);
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

    public static List<Photo> searchByLocationTag(String key) {
        List<Photo> ret = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT p.albumid, t1.photoname FROM ( SELECT l.photoname FROM locationtag l WHERE value Like '%"
                    + key
                    + "%')t1, photo p WHERE p.filepath = t1.photoname GROUP BY t1.photoname";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Photo photo = new Photo(new File(cursor.getString(INDEX_IMAGE_PATH)), cursor.getString(INDEX_ALBUM_NAME));
                setPhotoTag(db, photo);
                ret.add(photo);
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

    public static List<Photo> searchByTags(String personKey, String locationKey) {
        List<Photo> ret = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            String sql = "SELECT photo.albumid, t1.photoname FROM (SELECT p.photoname FROM persontag p WHERE value Like '%"
                    + personKey
                    + "%' UNION ALL SELECT l.photoname FROM locationtag l WHERE value Like '%"
                    + locationKey
                    + "%')t1, photo WHERE photo.filepath = t1.photoname GROUP BY t1.photoname";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Photo photo = new Photo(new File(cursor.getString(INDEX_IMAGE_PATH)), cursor.getString(INDEX_ALBUM_NAME));
                setPhotoTag(db, photo);
                ret.add(photo);
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

    private static void setPhotoTag(SQLiteDatabase db, Photo photo) {
        String photoPath = photo.getFullPath();
        String albumName = photo.getAlbumName();
        Cursor personCursor = null;
        Cursor locationCursor = null;

        try {
            String personTagSql = "SELECT `value` FROM `persontag` WHERE `photoname` = '"
                    + photoPath
                    + "' and `albumsource` = '"
                    + albumName
                    + "'";

            personCursor = db.rawQuery(personTagSql, null);

            List<PersonTag> personTags = new ArrayList<>();
            while (personCursor.moveToNext()) {
                PersonTag personTag = new PersonTag(personCursor.getString(0), photo);
                personTags.add(personTag);
            }
            photo.setPersonTag(personTags);

            String locationTagSql = "SELECT `value` FROM `locationtag` WHERE `photoname` = '"
                    + photoPath
                    + "' and `albumorigin` = '"
                    + albumName
                    + "'";

            locationCursor = db.rawQuery(locationTagSql, null);

            List<LocationTag> locationTags = new ArrayList<>();
            while (locationCursor.moveToNext()) {
                LocationTag locationTag = new LocationTag(locationCursor.getString(0), photo);
                locationTags.add(locationTag);
            }
            photo.setLocationTags(locationTags);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (personCursor != null) {
                personCursor.close();
            }

            if (locationCursor != null) {
                locationCursor.close();
            }
        }
    }

}
