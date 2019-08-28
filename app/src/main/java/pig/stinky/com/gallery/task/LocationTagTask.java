package pig.stinky.com.gallery.task;

import android.content.Context;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.bean.LocationTag;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.LocationTagDao;

import java.lang.ref.WeakReference;
import java.util.List;

public class LocationTagTask extends LoadTask<LocationTag> {

    private Photo mPhoto;

    public LocationTagTask(WeakReference<Context> context, BaseAdapter<LocationTag> adapter, Photo photo) {
        super(context, adapter);
        mPhoto = photo;
    }

    @Override
    protected List<LocationTag> doInBackground() {
        return LocationTagDao.getPhotoLags(mPhoto);
    }

}
