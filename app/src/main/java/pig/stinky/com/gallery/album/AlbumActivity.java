package pig.stinky.com.gallery.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import pig.stinky.com.gallery.R;
import pig.stinky.com.gallery.basic.RVActivity;
import pig.stinky.com.gallery.bean.Album;
import pig.stinky.com.gallery.db.AlbumDao;
import pig.stinky.com.gallery.search.SearchActivity;
import pig.stinky.com.gallery.task.AlbumTask;
import pig.stinky.com.gallery.utils.DialogHelper;

import java.lang.ref.WeakReference;

public class AlbumActivity extends RVActivity {

    public static final String EXTRA_OPEN_ALBUM = "open_album";

    private AlbumAdapter mAlbumAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAlbumAdapter = new AlbumAdapter(this);
        mRecyclerView.setAdapter(mAlbumAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAlbum();
    }

    private void refreshAlbum() {
        AlbumTask task = new AlbumTask(new WeakReference<>(this), mAlbumAdapter);
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_album:
                View root = View.inflate(this, R.layout.dialog_create, null);
                final EditText et = root.findViewById(R.id.create);

                AlertDialog dialog = DialogHelper.buildCustomViewDialog(this, "Create Album", root, (dialog1, which) -> {
                    // TODO: 2018/12/10 do it in thread
                    AlbumDao.addAlbum(new Album(et.getText().toString().trim()));

                    refreshAlbum();
                });
                dialog.show();
                return true;
            case R.id.search_album:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
