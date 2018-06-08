package soapbox.soapboxassignment.Presenter;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soapbox.soapboxassignment.Api.ApiInterface;
import soapbox.soapboxassignment.Api.ApiService;
import soapbox.soapboxassignment.Model.GifItems;
import soapbox.soapboxassignment.Model.GifResponse;
import soapbox.soapboxassignment.R;
import soapbox.soapboxassignment.View.MainView;

public class PresenterImpl implements PresenterInterface{

    private MainView mainView;
    private ApiInterface apiInterface;
    private Activity activity;
    private List<GifItems> gifItems;
    DatabaseReference database;

    public GifItems getGifItems(int position) {
        return gifItems.get(position);
    }

    public void setGifItems(List<GifItems> gifItems) {
        this.gifItems = gifItems;
    }

    public PresenterImpl(Activity activity, MainView mainView) {
        this.activity = activity;
        this.mainView = mainView;
        apiInterface = ApiService.getClient().create(ApiInterface.class);
    }

    @Override
    public void onCreate() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onSearchButtonClicked(String tagName) {
        new getGifResponse().execute(tagName);
    }

    private void addRatings(GifItems item) {
        database.child("gifs").child(item.getId()).setValue(item);
    }

    @Override
    public void onItemClicked(int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageView imageView = dialog.findViewById(R.id.dialog_image);
        TextView label = dialog.findViewById(R.id.dialog_label);
        final RatingBar ratingBar = dialog.findViewById(R.id.dialog_ratingbar);
        Button btnSubmit = dialog.findViewById(R.id.dialog_btn_submit);
        Button btnClose = dialog.findViewById(R.id.dialog_btn_close);

        final GifItems gifItem = getGifItems(position);

        Glide.with(activity)
                .load(gifItem.getUrl())
                .into(imageView);
        label.setText(gifItem.getTitle());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gifItem.setRating(ratingBar.getRating());
                addRatings(gifItem);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mainView.showDialog(dialog);
    }

    @Override
    public void onToolBarButtonClicked() {

        database.child("gifs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<GifItems> gifs = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    GifItems gif = noteDataSnapshot.getValue(GifItems.class);
                    gifs.add(gif);
                }

                mainView.showHighRatedGifs(gifs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private class getGifResponse extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainView.showProgress();
        }

        @Override
        protected String doInBackground(String... strings) {
            Call<GifResponse> call = apiInterface.getGifResponse(activity.getResources().getString(R.string.giphy_api_key), strings[0]);
            call.enqueue(new Callback<GifResponse>() {
                @Override
                public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                    List<GifItems> gifItems = response.body().getGifItems();
                    setGifItems(gifItems);
                    mainView.setItems(gifItems);
                }

                @Override
                public void onFailure(Call<GifResponse> call, Throwable t) {
                    Log.e("error", t.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mainView.hideProgress();
        }
    }
}
