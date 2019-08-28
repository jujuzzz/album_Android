package pig.stinky.com.gallery.photo;

import android.content.Context;
import android.graphics.Bitmap;
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

public class PhotoAdapter extends BaseAdapter<Photo> {

    public PhotoAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        PhotoViewHolder vh = (PhotoViewHolder) holder;
        Photo photo = mData.get(position);

        vh.mPhotoName.setText(photo.getName());
        if (photo.exist()) {
            Bitmap thumbnail = photo.getThumbnail();
            if (thumbnail != null) {
                vh.mPhotoThumbnail.setImageBitmap(thumbnail);
            } else {
                vh.mPhotoThumbnail.setImageDrawable(mContext.getDrawable(R.drawable.ic_image_placeholder));
            }
        } else {
            vh.mPhotoThumbnail.setImageDrawable(mContext.getDrawable(R.drawable.ic_image_placeholder));
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView mPhotoThumbnail;
        TextView mPhotoName;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoName = itemView.findViewById(R.id.tv_photo_name);
            mPhotoThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
