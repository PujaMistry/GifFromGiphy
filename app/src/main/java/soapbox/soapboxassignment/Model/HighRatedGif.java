package soapbox.soapboxassignment.Model;

import android.support.annotation.NonNull;

public class HighRatedGif implements Comparable<HighRatedGif> {

    private String gifId;
    private String Url;

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    private Float ratings;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        this.ratings = ratings;
    }

    @Override
    public int compareTo(@NonNull HighRatedGif highRatedGif) {
        if (ratings.floatValue() > highRatedGif.ratings.floatValue()) {
            return 1;
        }
        else if (ratings.floatValue() <  highRatedGif.ratings.floatValue()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
