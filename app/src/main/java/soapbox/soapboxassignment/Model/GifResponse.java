package soapbox.soapboxassignment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GifResponse {

    @SerializedName("data")
    @Expose
    private List<GifItems> gifItems = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<GifItems> getGifItems() {
        return gifItems;
    }

    public void setGifItems(List<GifItems> gifItems) {
        this.gifItems = gifItems;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
