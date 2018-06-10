package soapbox.soapboxassignment.Presenter;

import android.app.Activity;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import soapbox.soapboxassignment.View.MainView;

@RunWith(MockitoJUnitRunner.class)
public class PresenterInterfaceTest {

    @Mock
    Activity activity;
    @Mock
    MainView view;

    private PresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new PresenterImpl(activity, view);
    }

//    @Test
//    public void checkIfShowsProgressOnButtonClicked() {
//        presenter.onSearchButtonClicked("cat", 5);
//        verify(view, times(1)).showProgress();
//    }
//
//    @Test
//    public void checkIfItemsArePassedToView() {
//        List<GifItems> items = new ArrayList<>();
//        GifItems gif = new GifItems();
//        gif.setId("1");
//        items.add(gif);
//        presenter.onSearchButtonClicked("cat", 5);
//        verify(view, times(1)).setItems(items);
//        verify(view, times(1)).hideProgress();
//    }

}