package pig.stinky.com.gallery.basic.move;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.R;
import pig.stinky.com.gallery.bean.Album;
import pig.stinky.com.gallery.bean.Photo;

public class MovePhotoAdapter extends BaseAdapter<Album> {

    private OnPhotoMovedListener mListener;

    private Photo mPhoto;

    public MovePhotoAdapter(Context context, Photo photo) {
        super(context);
        mPhoto = photo;
    }

    public interface OnPhotoMovedListener {
        void onPhotoMoved(Album dst);
    }

    public void setOnPhotoMovedListener(OnPhotoMovedListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.move_photo_item, parent, false);
        return new MovePhotoViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovePhotoViewHolder vh = (MovePhotoViewHolder) holder;
        Album album = mData.get(position);

        vh.mNameTv.setText(album.getAlbumName());

        if (album.getAlbumName().equals(mPhoto.getAlbumName())) {
            vh.mMoveBtn.setEnabled(false);
        } else {
            vh.mMoveBtn.setEnabled(true);
            vh.mMoveBtn.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onPhotoMoved(album);
                }
            });
        }
    }

    static class MovePhotoViewHolder extends RecyclerView.ViewHolder {

        TextView mNameTv;
        Button mMoveBtn;

        public MovePhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.tv_photo_name);
            mMoveBtn = itemView.findViewById(R.id.btn_move);
        }
    }
}
