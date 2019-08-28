package pig.stinky.com.gallery.detail;

import android.content.Context;
import pig.stinky.com.gallery.bean.LocationTag;

public class LocationTagAdapter extends TagAdapter<LocationTag> {

    public LocationTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected String tagName(LocationTag locationTag) {
        return locationTag.getValue();
    }
}
