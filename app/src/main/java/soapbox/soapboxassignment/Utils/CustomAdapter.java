package soapbox.soapboxassignment.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;

import java.util.List;

import soapbox.soapboxassignment.Model.GifItems;
import soapbox.soapboxassignment.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<GifItems> gifItems;
    private Context context;
    private boolean isRating;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.gif_imageView);
            ratingBar = view.findViewById(R.id.ratinbar);
            ratingBar.setVisibility(isRating ? View.VISIBLE : View.GONE);
        }
    }

    public CustomAdapter(Context context) {
        this.context = context;
    }

    public boolean isRating(){
        return isRating;
    }

    public void setGifItems(List<GifItems> gifItems, boolean isRating) {
        this.gifItems = gifItems;
        this.isRating = isRating;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GifItems gifItem = gifItems.get(position);

        Glide.with(context)
                .load(gifItem.getUrl())
                .into(holder.imageView);
        if(isRating)
            holder.ratingBar.setRating(gifItem.getRating());
    }

    @Override
    public int getItemCount() {
        return gifItems.size();
    }
}

