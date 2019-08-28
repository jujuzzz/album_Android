package pig.stinky.com.gallery.basic.deletetag;

import android.content.Context;
import pig.stinky.com.gallery.bean.LocationTag;

public class DeleteLocationTagAdapter extends DeleteTagAdapter<LocationTag> {

    public DeleteLocationTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected String tagName(LocationTag locationTag) {
        return locationTag.getValue();
    }

}
