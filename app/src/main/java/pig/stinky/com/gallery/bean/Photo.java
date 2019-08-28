package pig.stinky.com.gallery.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.List;

public class Photo implements Parcelable {

    private File mImage;
    private String mAlbumName;

    public Photo(File image, String albumName) {
        mImage = image;
        mAlbumName = albumName;
    }

    protected Photo(Parcel in) {
        mImage = (File) in.readSerializable();
        mAlbumName = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mImage);
        dest.writeString(mAlbumName);
    }

    public void setPersonTag(List<PersonTag> personTags) {
        for (PersonTag tag : personTags) {
            tag.setPhoto(this);
        }
    }

    public void setLocationTags(List<LocationTag> locationTags) {
        for (LocationTag tag : locationTags) {
            tag.setPhoto(this);
        }
    }

    public boolean exist() {
        return mImage != null && mImage.exists();
    }

    public String getName() {
        return mImage.getName();
    }

    public String getFullPath() {
        return mImage.getAbsolutePath();
    }

    public Bitmap getOriginBitmap() {
        if (!exist()) {
            return null;
        }

        // TODO: 2018/12/14 potential OOM
        return BitmapFactory.decodeFile(getFullPath());
    }

    public Bitmap getScaledBitmap() {
        if (!exist()) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        return BitmapFactory.decodeFile(getFullPath(), options);
    }

    public Bitmap getThumbnail() {
        if (!exist()) {
            return null;
        }

        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mImage.getAbsolutePath()), 50, 50);
    }

    public String getAlbumName() {
        return mAlbumName;
    }
}
