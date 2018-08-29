package software.addigma.carifilm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable {

    private String id;
    private String title;
    private String overview;
    private String release_data;
    private String poster_path;
    public Film(){}
    public Film(String id, String title, String overview, String release_data, String poster_path){
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_data = release_data;
        this.poster_path = poster_path;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_data() {
        return release_data;
    }

    public void setRelease_data(String release_data) {
        this.release_data = release_data;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.release_data);
        dest.writeString(this.poster_path);
    }

    protected Film(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.release_data = in.readString();
        this.poster_path = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}
