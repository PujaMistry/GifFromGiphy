package soapbox.soapboxassignment.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import soapbox.soapboxassignment.Model.GifResponse;

public interface ApiInterface {

//    https://api.giphy.com/v1/gifs/search?api_key=ipbh8N1IyQEXtRsSbSFkgsTlcSpYfV27&q=funny cat&limit=3&offset=0&rating=G&lang=en

    @GET("v1/gifs/search?")
    Call<GifResponse> getGifResponse(@Query("api_key") String api_key,
                                     @Query("q") String tagName,
                                     @Query("limit") int limit,
                                     @Query("offset") int offset);
}
