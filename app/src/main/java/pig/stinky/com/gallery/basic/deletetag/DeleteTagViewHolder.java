package pig.stinky.com.gallery.basic.deletetag;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pig.stinky.com.gallery.R;

public class DeleteTagViewHolder extends RecyclerView.ViewHolder {

    TextView mTagNameTv;
    Button mDeleteBtn;

    public DeleteTagViewHolder(@NonNull View itemView) {
        super(itemView);
        mTagNameTv = itemView.findViewById(R.id.tv_tag_name);
        mDeleteBtn = itemView.findViewById(R.id.btn_delete);
    }
}
