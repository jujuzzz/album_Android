package pig.stinky.com.gallery;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    protected OnItemClickListener mOnItemClickListener;
    protected List<T> mData = new ArrayList<>();
    protected Context mContext;

    public BaseAdapter(Context context) {
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setData(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.OnItemClick(v, position);
            }
        });
    }
}
