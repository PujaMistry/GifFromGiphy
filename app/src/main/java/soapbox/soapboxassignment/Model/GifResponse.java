package soapbox.soapboxassignment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GifResponse {

    @SerializedName("data")
    @Expose
    private List<GifItems> gifItems = null;

    public List<GifItems> getGifItems() {
        return gifItems;
    }

    public void setGifItems(List<GifItems> gifItems) {
        this.gifItems = gifItems;
    }
}
