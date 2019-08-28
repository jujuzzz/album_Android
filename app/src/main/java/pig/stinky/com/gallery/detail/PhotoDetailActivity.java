package pig.stinky.com.gallery.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import pig.stinky.com.gallery.PermissionActivity;
import pig.stinky.com.gallery.R;
import pig.stinky.com.gallery.basic.deletetag.DeleteLocationTagActivity;
import pig.stinky.com.gallery.basic.deletetag.DeletePersonTagActivity;
import pig.stinky.com.gallery.basic.move.MovePhotoActivity;
import pig.stinky.com.gallery.bean.LocationTag;
import pig.stinky.com.gallery.bean.PersonTag;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.LocationTagDao;
import pig.stinky.com.gallery.db.PersonTagDao;
import pig.stinky.com.gallery.db.PhotoDao;
import pig.stinky.com.gallery.utils.DialogHelper;

import java.util.ArrayList;

import static pig.stinky.com.gallery.photo.PhotoActivity.EXTRA_OPEN_PHOTO;
import static pig.stinky.com.gallery.photo.PhotoActivity.EXTRA_OPEN_PHOTO_INDEX;

public class PhotoDetailActivity extends PermissionActivity {

    private ViewPager mPager;
    private PhotoDetailPagerAdapter mAdapter;

    private int mCurrentIndex;
    private ArrayList<Photo> mPhotos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mPhotos = getIntent().getParcelableArrayListExtra(EXTRA_OPEN_PHOTO);
        mCurrentIndex = getIntent().getIntExtra(EXTRA_OPEN_PHOTO_INDEX, -1);

        mPager = findViewById(R.id.pager);
        mAdapter = new PhotoDetailPagerAdapter(this, mPhotos);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mCurrentIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View root = View.inflate(this, R.layout.dialog_create, null);
        final EditText et = root.findViewById(R.id.create);

        AlertDialog dialog = null;
        Intent intent = null;

        Photo photo = mPhotos.get(mPager.getCurrentItem());

        switch (item.getItemId()) {
            case R.id.delete_photo:
                dialog = DialogHelper.buildDeleteDialog(this, "Delete this photo?", (dialog1, which) -> {
                    PhotoDao.deletePhoto(photo);

                    // exit activity after album deleted
                    finish();
                });
                dialog.show();
                return true;
            case R.id.move_photo:
                intent = new Intent(this, MovePhotoActivity.class);
                intent.putExtra(MovePhotoActivity.EXTRA_MOVE_PHOTO, photo);
                startActivityForResult(intent, MovePhotoActivity.MOVE_PHOTO_REQUEST_CODE);
                return true;
            case R.id.add_person_tag:
                dialog = DialogHelper.buildCustomViewDialog(this, "Add person tag", root,
                        (dialog1, which) -> {
                            PersonTagDao.addTag(new PersonTag(et.getText().toString().trim(), photo));
                            mAdapter.notifyDataSetChanged();
                        });
                dialog.show();
                return true;
            case R.id.add_location_tag:
                dialog = DialogHelper.buildCustomViewDialog(this, "Add location tag", root,
                        (dialog1, which) -> {
                            LocationTagDao.addTag(new LocationTag(et.getText().toString().trim(), photo));
                            mAdapter.notifyDataSetChanged();
                        });
                dialog.show();
                return true;
            case R.id.delete_person_tag:
                intent = new Intent(this, DeletePersonTagActivity.class);
                intent.putExtra(DeletePersonTagActivity.EXTRA_DELETE_PERSON_TAG_PHOTO, photo);
                startActivity(intent);
                return true;
            case R.id.delete_location_tag:
                intent = new Intent(this, DeleteLocationTagActivity.class);
                intent.putExtra(DeleteLocationTagActivity.EXTRA_DELETE_LOCATION_TAG_PHOTO, photo);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MovePhotoActivity.MOVE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }
}
