package pig.stinky.com.gallery.bean;

public class PersonTag {

    private String mValue;

    // TODO: 10/12/18 1 image --- 1 path?
    private Photo mPhoto;

    public PersonTag(String value, Photo photo) {
        mValue = value;
        mPhoto = photo;
    }

    public String getValue() {
        return mValue;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }
}
