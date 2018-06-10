package soapbox.soapboxassignment.Presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soapbox.soapboxassignment.Api.ApiInterface;
import soapbox.soapboxassignment.Api.ApiService;
import soapbox.soapboxassignment.Model.GifItems;
import soapbox.soapboxassignment.Model.GifResponse;
import soapbox.soapboxassignment.Model.HighRatedGif;
import soapbox.soapboxassignment.R;
import soapbox.soapboxassignment.View.MainView;

public class PresenterImpl implements PresenterInterface{

    private MainView mainView;
    private ApiInterface apiInterface;
    private Activity activity;
    private DatabaseReference database;
    private String tagName;

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
    public void onSearchButtonClicked(String tagName, int itemCount) {
        this.tagName = tagName;
        callTopRatedMoviesApi(itemCount).enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                List<GifItems> gifItems = response.body().getGifItems();
                mainView.setItems(gifItems);
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }

    private void addRatings(HighRatedGif item) {
        database.child("gifs").child(item.getGifId()).setValue(item);
    }

    @Override
    public void onItemClicked(final GifItems gifItem) {
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

        if(gifItem != null) {
            Glide.with(activity)
                    .load(gifItem.getImages().getOriginal().getUrl())
                    .into(imageView);
            label.setText(gifItem.getTitle());

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HighRatedGif highRatedGif = new HighRatedGif();
                    highRatedGif.setGifId(gifItem.getId());
                    highRatedGif.setUrl(gifItem.getImages().getOriginal().getUrl());
                    highRatedGif.setRatings(ratingBar.getRating());
                    addRatings(highRatedGif);
                    dialog.dismiss();
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

        mainView.showDialog(dialog);
    }

    @Override
    public void onLoadNextPage(int itemCount) {
        callTopRatedMoviesApi(itemCount).enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {

                List<GifItems> gifItems = response.body().getGifItems();
                mainView.loadNextPage(gifItems);
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }

    private Call<GifResponse> callTopRatedMoviesApi(int itemCount) {
        Log.d("calltopRatedMoviesApi", ""+itemCount);
        return apiInterface.getGifResponse(activity.getResources().getString(R.string.giphy_api_key), tagName, itemCount+6, itemCount);
    }

    @Override
    public void onToolBarButtonClicked() {

        database.child("gifs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HighRatedGif> gifs = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    HighRatedGif gif = noteDataSnapshot.getValue(HighRatedGif.class);
                    gifs.add(gif);
                }

                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list_layout);
                dialog.setCanceledOnTouchOutside(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);

                ListView list = dialog.findViewById(R.id.dialog_list);
                Button btnClose = dialog.findViewById(R.id.dialog_list_layout_close_button);
                Collections.sort(gifs, Collections.reverseOrder());
                ListAdapter adapter = new ListAdapter(gifs);
                list.setAdapter(adapter);
                mainView.showDialog(dialog);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private class ListAdapter extends BaseAdapter {

        private List<HighRatedGif> gifItems;

        public ListAdapter(List<HighRatedGif> gifItems) {
            this.gifItems = gifItems;
        }

        @Override
        public int getCount() {
            return gifItems.size();
        }

        @Override
        public Object getItem(int i) {
            return gifItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_list_row, null);

            ImageView imageView= view.findViewById(R.id.dialog_list_imageView);
            RatingBar ratingBar = view.findViewById(R.id.dialog_list_ratingBar);

            HighRatedGif item = gifItems.get(i);

            if(item != null) {
                Glide.with(activity)
                        .load(item.getUrl())
                        .into(imageView);
                ratingBar.setRating(item.getRatings());
            }

            return view;
        }
    }
}
