package pig.stinky.com.gallery;

import android.app.Application;
import android.content.Context;
import pig.stinky.com.gallery.db.GalleryDatabase;
import pig.stinky.com.gallery.db.DatabaseManager;

public class GalleryApplication extends Application {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        DatabaseManager.initializeInstance(new GalleryDatabase(this));
    }
}
