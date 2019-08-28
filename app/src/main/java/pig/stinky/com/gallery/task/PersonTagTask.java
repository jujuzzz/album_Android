package pig.stinky.com.gallery.task;

import android.content.Context;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.bean.PersonTag;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.PersonTagDao;

import java.lang.ref.WeakReference;
import java.util.List;

public class PersonTagTask extends LoadTask<PersonTag>{

    private Photo mPhoto;

    public PersonTagTask(WeakReference<Context> mContext, BaseAdapter<PersonTag> mAdapter, Photo photo) {
        super(mContext, mAdapter);
        mPhoto = photo;
    }

    @Override
    protected List<PersonTag> doInBackground() {
        return PersonTagDao.getPhotoTags(mPhoto);
    }
}
