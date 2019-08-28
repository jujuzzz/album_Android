package pig.stinky.com.gallery.basic.deletetag;

import android.content.Context;
import pig.stinky.com.gallery.bean.PersonTag;

public class DeletePersonTagAdapter extends DeleteTagAdapter<PersonTag> {

    public DeletePersonTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected String tagName(PersonTag personTag) {
        return personTag.getValue();
    }


}
