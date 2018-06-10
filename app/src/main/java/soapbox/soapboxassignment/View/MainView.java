package soapbox.soapboxassignment.View;

import android.app.Dialog;

import java.util.List;

import soapbox.soapboxassignment.Model.GifItems;

public interface MainView {

    void showProgress();

    void hideProgress();

    void setItems(List<GifItems> items);

    void loadNextPage(List<GifItems> items);

    void showDialog(Dialog dialog);

    void dismissDialog(Dialog dialog);
}
