package pig.stinky.com.gallery.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    private String mAlbumName;

    public Album(String albumName) {
        mAlbumName = albumName;
    }

    protected Album(Parcel in) {
        mAlbumName = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getAlbumName() {
        return mAlbumName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAlbumName);
    }
}
