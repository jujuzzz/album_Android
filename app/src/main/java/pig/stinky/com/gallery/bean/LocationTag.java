package pig.stinky.com.gallery.bean;

public class LocationTag {

    private String mValue;

    // TODO: 10/12/18 1 image --- 1 path?
    private Photo mPhoto;

    public LocationTag(String value, Photo photo) {
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
        this.mPhoto = photo;
    }
}
