package soapbox.soapboxassignment.Presenter;

import soapbox.soapboxassignment.Model.GifItems;

public interface PresenterInterface {

    void onCreate();
    void onSearchButtonClicked(String tagName, int itemCount);
    void onItemClicked(GifItems gifItems);
    void onLoadNextPage(int itemCount);
    void onToolBarButtonClicked();
}
