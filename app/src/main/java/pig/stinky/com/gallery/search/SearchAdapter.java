package pig.stinky.com.gallery.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.R;
import pig.stinky.com.gallery.bean.Photo;

public class SearchAdapter extends BaseAdapter<Photo> {

    public SearchAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        SearchViewHolder vh = (SearchViewHolder) holder;

        vh.mThumbnail.setImageBitmap(mData.get(position).getThumbnail());
        vh.mPhotoName.setText(mData.get(position).getName());
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView mThumbnail;
        TextView mPhotoName;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            mPhotoName = itemView.findViewById(R.id.tv_photo_name);
        }
    }

}
