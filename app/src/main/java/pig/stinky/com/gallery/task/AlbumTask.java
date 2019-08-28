package pig.stinky.com.gallery.task;

import android.content.Context;
import android.content.Intent;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.album.AlbumActivity;
import pig.stinky.com.gallery.bean.Album;
import pig.stinky.com.gallery.db.AlbumDao;
import pig.stinky.com.gallery.photo.PhotoActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class AlbumTask extends LoadTask<Album> {
    public AlbumTask(WeakReference<Context> context, BaseAdapter<Album> adapter) {
        super(context, adapter);
    }

    @Override
    protected List<Album> doInBackground() {
        return AlbumDao.loadAlbum();
    }

    @Override
    protected void itemClick(List<Album> data, int position) {
        if (mContext.get() != null) {
            Intent intent = new Intent(mContext.get(), PhotoActivity.class);
            intent.putExtra(AlbumActivity.EXTRA_OPEN_ALBUM, data.get(position));
            mContext.get().startActivity(intent);
        }
    }
}
