package soapbox.soapboxassignment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import soapbox.soapboxassignment.Utils.CustomAdapter;
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
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new PresenterImpl(MainActivity.this, this);
        presenter.onCreate();

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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnItemTouchListener(new RecycleTouchListener(getApplicationContext(), new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!adapter.isRating())
                    presenter.onItemClicked(position);
            }
        }));
    }

    @OnClick(R.id.btn_search)
    public void search() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
        tagName = editTextSearch.getText().toString();
        if(tagName != null)
            presenter.onSearchButtonClicked(tagName);
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
        setAdapter(items, false);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void showHighRatedGifs(List<GifItems> items) {
        setAdapter(items, true);
    }

    private void setAdapter(List<GifItems> items, boolean isRatings) {
        if(adapter == null)
            adapter = new CustomAdapter(getApplicationContext());
        adapter.setGifItems(items, isRatings);
        recyclerView.setAdapter(adapter);
    }
}
