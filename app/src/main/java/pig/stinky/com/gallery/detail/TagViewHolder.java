package pig.stinky.com.gallery.detail;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.R;

public class TagViewHolder extends RecyclerView.ViewHolder {
    TextView mNameTv;

    public TagViewHolder(@NonNull View itemView) {
        super(itemView);
        mNameTv = itemView.findViewById(R.id.tv_tag_name);
    }
}
