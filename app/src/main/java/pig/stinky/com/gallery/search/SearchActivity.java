package pig.stinky.com.gallery.search;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.PermissionActivity;
import pig.stinky.com.gallery.R;
import pig.stinky.com.gallery.bean.Photo;
import pig.stinky.com.gallery.db.PhotoDao;
import pig.stinky.com.gallery.task.LoadTask;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchActivity extends PermissionActivity {

    private EditText mPersonEt;
    private EditText mLocationEt;

    private RadioGroup mGroup;
    private RecyclerView mRecyclerView;

    // default selection
    private Filter mFilter = Filter.PERSON;
    private SearchAdapter mAdapter;

    private enum Filter {
        PERSON, LOCATION, BOTH
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mPersonEt = findViewById(R.id.et_person);
        mLocationEt = findViewById(R.id.et_location);
        mGroup = findViewById(R.id.radio_group);
        mRecyclerView = findViewById(R.id.recycler_view);

        mGroup.check(R.id.rb_person_tag);
        mGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_person_tag:
                    mFilter = Filter.PERSON;
                    break;
                case R.id.rb_location_tag:
                    mFilter = Filter.LOCATION;
                    break;
                case R.id.rb_both_tag:
                    mFilter = Filter.BOTH;
                    break;
                default:
                    mFilter = null;
                    break;
            }
        });

        mAdapter = new SearchAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    private void showToast() {
        Toast.makeText(this, "Target field cannot be empty!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String personTagTrim = mPersonEt.getText().toString().trim();
        String locationTagTrim = mLocationEt.getText().toString().trim();

        switch (item.getItemId()) {
            case R.id.search:
                switch (mGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_person_tag:
                        if (TextUtils.isEmpty(personTagTrim)) {
                            showToast();
                            return true;
                        }
                        break;
                    case R.id.rb_location_tag:
                        if (TextUtils.isEmpty(locationTagTrim)) {
                            showToast();
                            return true;
                        }
                        break;
                    case R.id.rb_both_tag:
                        if (TextUtils.isEmpty(personTagTrim) || TextUtils.isEmpty(locationTagTrim)) {
                            showToast();
                            return true;
                        }
                        break;
                    default:
                        return super.onOptionsItemSelected(item);
                }
        }

        startSearch(mPersonEt.getText().toString().trim(), mLocationEt.getText().toString().trim());
        return true;
    }

    private void startSearch(String personKey, String locationKey) {
        SearchTask task = new SearchTask(new WeakReference<>(this), mAdapter, mFilter, personKey, locationKey);
        task.execute();
    }

    private static class SearchTask extends LoadTask<Photo> {

        @Nullable
        private Filter mFilter;

        @Nullable
        private String mPersonKey;

        @Nullable
        private String mLocationKey;

        public SearchTask(WeakReference<Context> mContext, BaseAdapter<Photo> adapter, Filter filter, String personKey,
                          String locationKey) {
            super(mContext, adapter);
            mFilter = filter;
            mPersonKey = personKey;
            mLocationKey = locationKey;
        }

        @Override
        protected List<Photo> doInBackground() {
            List<Photo> ret = null;
            switch (mFilter) {
                case PERSON:
                    ret = PhotoDao.searchByPersonTag(mPersonKey);
                    break;
                case LOCATION:
                    ret = PhotoDao.searchByLocationTag(mLocationKey);
                    break;
                case BOTH:
                    ret = PhotoDao.searchByTags(mPersonKey, mLocationKey);
                    break;
                default:
                    break;
            }
            return ret;
        }
    }
}
