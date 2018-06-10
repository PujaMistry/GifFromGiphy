package soapbox.soapboxassignment.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import soapbox.soapboxassignment.Model.GifItems;
import soapbox.soapboxassignment.R;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<GifItems> gifItems;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        gifItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.grid_layout, parent, false);
        viewHolder = new GifVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GifItems gifItem = gifItems.get(position);
        if(gifItem != null) {
            switch (getItemViewType(position)) {
                case ITEM:
                    final GifVH gifVH = (GifVH) holder;
                    String url = gifItem.getImages().getOriginal().getUrl();
                    Glide.with(context)
                            .load(url)
                            .into(gifVH.image);
                    gifVH.title.setText(gifItem.getTitle());
                    break;

                case LOADING:
//                Do nothing
                    break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return gifItems == null ? 0 : gifItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == gifItems.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(GifItems r) {
        gifItems.add(r);
        notifyItemInserted(gifItems.size() - 1);
    }

    public void addAll(List<GifItems> moveResults) {
        for (GifItems result : moveResults) {
            add(result);
        }
    }

    public void remove(GifItems r) {
        int position = gifItems.indexOf(r);
        if (position > -1) {
            gifItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new GifItems());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = gifItems.size() - 1;
        GifItems result = getItem(position);

        if (result != null) {
            gifItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public GifItems getItem(int position) {
        return gifItems.get(position);
    }

    protected class GifVH extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;

        public GifVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
