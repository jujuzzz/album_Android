package pig.stinky.com.gallery.detail;

import android.content.Context;
import pig.stinky.com.gallery.bean.PersonTag;

public class PersonTagAdapter extends TagAdapter<PersonTag> {

    public PersonTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected String tagName(PersonTag personTag) {
        return personTag.getValue();
    }

}
