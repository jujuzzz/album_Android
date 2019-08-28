package pig.stinky.com.gallery.photo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.PermissionActivity;
import pig.stinky.com.gallery.R;
import pig.stinky.com.gallery.album.AlbumActivity;
import pig.stinky.com.gallery.bean.Album;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.AlbumDao;
import pig.stinky.com.gallery.db.PhotoDao;
import pig.stinky.com.gallery.detail.PhotoDetailActivity;
import pig.stinky.com.gallery.task.LoadTask;
import pig.stinky.com.gallery.utils.DialogHelper;
import pig.stinky.com.gallery.utils.UriUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends PermissionActivity {

    public static final String EXTRA_OPEN_PHOTO = "open_photo";
    public static final String EXTRA_OPEN_PHOTO_INDEX = "open_photo_index";

    public static final int ADD_PHOTO_REQUEST_CODE = 200;

    private RecyclerView mRecyclerView;
    private PhotoAdapter mAdapter;

    private Album mAlbum;

    // from picker
    private List<Uri> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mAlbum = getIntent().getParcelableExtra(AlbumActivity.EXTRA_OPEN_ALBUM);

        mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new PhotoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        if (mAlbum != null) {
            setTitle(mAlbum.getAlbumName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPhotos();
    }

    private void refreshPhotos() {
        PhotoTask task = new PhotoTask(new WeakReference<>(this), mAdapter, mAlbum);
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            // convert media uri to file path
            if (mSelected != null && !mSelected.isEmpty()) {
                Uri uri = mSelected.get(0);
                String path = UriUtils.getImagePathFromMediaUri(this, uri);

                if (path != null) {
                    File image = new File(path);
                    Photo photo = new Photo(image, mAlbum.getAlbumName());

                    // TODO: 12/12/18 do it in worker thread
                    PhotoDao.addPhoto(photo);

                    refreshPhotos();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_photo:
                // photo picker
                Matisse.from(this)
                        .choose(MimeType.ofAll())
                        .countable(true)
                        // 1 photo selected each time
                        .maxSelectable(1)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(ADD_PHOTO_REQUEST_CODE);

                return true;
            case R.id.delete_album:
                AlertDialog deleteDialog = DialogHelper.buildDeleteDialog(this, "Delete this album?", (dialog, which) -> {
                    // TODO: 2018/12/10 do it in thread
                    if (mAlbum != null) {
                        AlbumDao.deleteAlbum(mAlbum);
                    }

                    // exit activity after album deleted
                    finish();
                });
                deleteDialog.show();
                return true;
            case R.id.rename_album:
                View root = View.inflate(this, R.layout.dialog_rename, null);
                final EditText et = root.findViewById(R.id.rename);

                if (et != null) {
                    et.setText(mAlbum.getAlbumName());
                    et.selectAll();
                    et.requestFocus();
                }

                AlertDialog renameDialog = DialogHelper.buildCustomViewDialog(this, "Rename Album", root, (dialog, which) -> {
                    if (et != null) {
                        if (et.getText().toString().trim().equals(mAlbum.getAlbumName())) {
                            Toast.makeText(this, "Rename failed. Album name cannot be the same!", Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO: 2018/12/10 do it in worker thread
                            AlbumDao.renameAlbum(mAlbum, et.getText().toString().trim());
                            finish();
                        }
                    }
                });
                renameDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private static class PhotoTask extends LoadTask<Photo> {
        Album mAlbum;

        public PhotoTask(WeakReference<Context> mContext, BaseAdapter<Photo> mAdapter, Album album) {
            super(mContext, mAdapter);
            mAlbum = album;
        }

        @Override
        protected List<Photo> doInBackground() {
            return PhotoDao.loadPhotos(mAlbum);
        }

        @Override
        protected void itemClick(List<Photo> data, int position) {
            if (mContext.get() != null) {
                Intent intent = new Intent(mContext.get(), PhotoDetailActivity.class);
                intent.putParcelableArrayListExtra(EXTRA_OPEN_PHOTO, (ArrayList<? extends Parcelable>) data);
                intent.putExtra(EXTRA_OPEN_PHOTO_INDEX, position);
                mContext.get().startActivity(intent);
            }
        }
    }
}
