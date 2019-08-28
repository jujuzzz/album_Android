package pig.stinky.com.gallery.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.R;

public abstract class TagAdapter<T> extends BaseAdapter<T> {

    public TagAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false);
        return new TagViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TagViewHolder vh = (TagViewHolder) holder;
        vh.mNameTv.setText("#" + tagName(mData.get(position)));
    }

    protected abstract String tagName(T t);
}
