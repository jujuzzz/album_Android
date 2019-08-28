package pig.stinky.com.gallery.basic.deletetag;

import android.os.Bundle;
import pig.stinky.com.gallery.basic.RVActivity;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.LocationTagDao;
import pig.stinky.com.gallery.task.LocationTagTask;

import java.lang.ref.WeakReference;

public class DeleteLocationTagActivity extends RVActivity {

    public static final String EXTRA_DELETE_LOCATION_TAG_PHOTO = "delete_location_tag_photo";

    private Photo mPhoto;
    private DeleteLocationTagAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoto = getIntent().getParcelableExtra(EXTRA_DELETE_LOCATION_TAG_PHOTO);

        mAdapter = new DeleteLocationTagAdapter(this);
        mAdapter.setOnPhotoDeleteListener(tag -> {
            // TODO: 12/12/18 do it in worker thread
            LocationTagDao.deleteTag(tag);

            refreshTags();
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTags();
    }

    private void refreshTags() {
        LocationTagTask personTagTask = new LocationTagTask(new WeakReference<>(this), mAdapter, mPhoto);
        personTagTask.execute();
    }
}
