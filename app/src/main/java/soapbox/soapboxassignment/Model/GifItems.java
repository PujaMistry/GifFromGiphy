package soapbox.soapboxassignment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GifItems {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;
    private Float gifRating;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getRating() {
        return gifRating;
    }

    public void setRating(Float rating) {
        this.gifRating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
