package pig.stinky.com.gallery.basic.deletetag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.R;

public abstract class DeleteTagAdapter<T> extends BaseAdapter<T> {

    private OnPhotoDeleteListener<T> mListener;

    public DeleteTagAdapter(Context context) {
        super(context);
    }

    public interface OnPhotoDeleteListener<T> {
        void onTagDeleted(T tag);
    }

    public void setOnPhotoDeleteListener(OnPhotoDeleteListener<T> listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.delete_tag_item, parent, false);
        return new DeleteTagViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeleteTagViewHolder vh = (DeleteTagViewHolder) holder;

        T tag = mData.get(position);

        vh.mTagNameTv.setText(tagName(tag));
        vh.mDeleteBtn.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTagDeleted(tag);
            }
        });
    }

    protected abstract String tagName(T t);
}
