package pig.stinky.com.gallery.task;

import android.content.Context;
import android.os.AsyncTask;
import pig.stinky.com.gallery.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class LoadTask<T> extends AsyncTask<Void, Void, List<T>> {

    protected WeakReference<Context> mContext;
    protected BaseAdapter<T> mAdapter;

    public LoadTask(WeakReference<Context> mContext, BaseAdapter<T> mAdapter) {
        this.mContext = mContext;
        this.mAdapter = mAdapter;
    }

    @Override
    protected List<T> doInBackground(Void... voids) {
        return doInBackground();
    }

    protected abstract List<T> doInBackground();

    protected void itemClick(List<T> data, int position) {

    }

    @Override
    protected void onPostExecute(final List<T> data) {
        final Context ctx = mContext.get();

        mAdapter.setOnItemClickListener((view, position) -> {
            if (ctx != null) {
                itemClick(data, position);
            }
        });
        mAdapter.setData(data);
    }

}
