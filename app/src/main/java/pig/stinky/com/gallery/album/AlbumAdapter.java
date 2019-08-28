package pig.stinky.com.gallery.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.BaseAdapter;
import pig.stinky.com.gallery.R;
import pig.stinky.com.gallery.bean.Album;

public class AlbumAdapter extends BaseAdapter<Album> {

    public AlbumAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new AlbumViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        AlbumViewHolder vh = (AlbumViewHolder) holder;
        vh.mAlbumNameTv.setText(mData.get(position).getAlbumName());
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView mAlbumNameTv;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            mAlbumNameTv = itemView.findViewById(R.id.tv_album_name);
        }
    }
}

