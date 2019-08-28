package pig.stinky.com.gallery.basic.move;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.basic.RVActivity;
import pig.stinky.com.gallery.bean.Album;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.AlbumDao;
import pig.stinky.com.gallery.db.PhotoDao;
import pig.stinky.com.gallery.task.LoadTask;

import java.lang.ref.WeakReference;
import java.util.List;

public class MovePhotoActivity extends RVActivity {

    public static final String EXTRA_MOVE_PHOTO = "move_photo";

    public static final int MOVE_PHOTO_REQUEST_CODE = 100;

    private MovePhotoAdapter mAdapter;

    private Photo mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoto = getIntent().getParcelableExtra(EXTRA_MOVE_PHOTO);

        mAdapter = new MovePhotoAdapter(this, mPhoto);
        mAdapter.setOnPhotoMovedListener(dst -> {
            // TODO: 12/12/18 do it in worker thread
            PhotoDao.movePhoto(mPhoto, new Album(mPhoto.getAlbumName()), dst);
            setResult(RESULT_OK, new Intent());
            finish();

        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAlbum();
    }

    private void refreshAlbum() {
        AlbumTask task = new AlbumTask(new WeakReference<>(this), mAdapter);
        task.execute();
    }

    private static class AlbumTask extends LoadTask<Album> {
        public AlbumTask(WeakReference<Context> context, BaseAdapter<Album> adapter) {
            super(context, adapter);
        }

        @Override
        protected List<Album> doInBackground() {
            return AlbumDao.loadAlbum();
        }
    }
}
