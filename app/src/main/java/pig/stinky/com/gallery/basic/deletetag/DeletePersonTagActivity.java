package pig.stinky.com.gallery.basic.deletetag;

import android.os.Bundle;
import pig.stinky.com.gallery.basic.RVActivity;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.PersonTagDao;
import pig.stinky.com.gallery.task.PersonTagTask;

import java.lang.ref.WeakReference;

public class DeletePersonTagActivity extends RVActivity {

    public static final String EXTRA_DELETE_PERSON_TAG_PHOTO = "delete_person_tag_photo";

    private DeletePersonTagAdapter mAdapter;

    private Photo mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhoto = getIntent().getParcelableExtra(EXTRA_DELETE_PERSON_TAG_PHOTO);

        mAdapter = new DeletePersonTagAdapter(this);
        mAdapter.setOnPhotoDeleteListener(tag -> {
            // TODO: 12/12/18 do it in worker thread
            PersonTagDao.deleteTag(tag);

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
        PersonTagTask personTagTask = new PersonTagTask(new WeakReference<>(this), mAdapter, mPhoto);
        personTagTask.execute();
    }
}
