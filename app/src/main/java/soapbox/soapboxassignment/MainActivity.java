package soapbox.soapboxassignment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import soapbox.soapboxassignment.Model.GifItems;
import soapbox.soapboxassignment.Presenter.PresenterImpl;
import soapbox.soapboxassignment.Presenter.PresenterInterface;
import soapbox.soapboxassignment.Utils.GridSpacingItemDecoration;
import soapbox.soapboxassignment.Utils.PaginationAdapter;
import soapbox.soapboxassignment.Utils.PaginationScrollListener;
import soapbox.soapboxassignment.Utils.RecycleTouchListener;
import soapbox.soapboxassignment.View.MainView;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.edittext_search)
    EditText editTextSearch;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String tagName;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    private PresenterInterface presenter;
    private PaginationAdapter adapter;

    private boolean isLoading = false;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new PresenterImpl(MainActivity.this, this);
        presenter.onCreate();

        adapter = new PaginationAdapter(this);
        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        Button btnHighRatedGifs = toolbar.findViewById(R.id.high_rated_gifs);
        btnHighRatedGifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onToolBarButtonClicked();
            }
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getResources(), 2, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecycleTouchListener(getApplicationContext(), new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                presenter.onItemClicked(adapter.getItem(position));
            }
        }));

        recyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                Log.d("offset", "" + offset);
                presenter.onLoadNextPage(offset);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void search() {
        offset = 0;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
        tagName = editTextSearch.getText().toString().trim();
        if (tagName != null) {
            Log.d("on search", "" + offset);
            adapter.clear();
            presenter.onSearchButtonClicked(tagName, offset);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setItems(List<GifItems> items) {
        adapter.addAll(items);
        adapter.addLoadingFooter();
        offset+=6;
    }

    @Override
    public void loadNextPage(List<GifItems> items) {
        adapter.removeLoadingFooter();
        isLoading = false;
        adapter.addAll(items);
        adapter.addLoadingFooter();
        offset+=6;
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        dialog.dismiss();
    }
}

