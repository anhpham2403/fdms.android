package com.framgia.fdms.screen.new_selection;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.framgia.fdms.R;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.MarkerRepository;
import com.framgia.fdms.data.source.MeetingRoomRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.CategoryRemoteDataSource;
import com.framgia.fdms.data.source.remote.MarkerRemoteDataSource;
import com.framgia.fdms.data.source.remote.MeetingRoomRemoteDataSource;
import com.framgia.fdms.data.source.remote.StatusRemoteDataSource;
import com.framgia.fdms.data.source.remote.VendorRemoteDataSource;
import com.framgia.fdms.databinding.ActivityNewStatusSelectionBinding;

import static com.framgia.fdms.screen.new_selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.new_selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.new_selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.new_selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.new_selection.SelectionType.VENDOR;

/**
 * StatusSelection Screen.
 */
public class StatusSelectionActivity extends AppCompatActivity
    implements SearchView.OnQueryTextListener {
    private static final String BUNDLE_SELECTION_TYPE = "BUNDLE_SELECTION_TYPE";
    private StatusSelectionContract.ViewModel mViewModel;
    @SelectionType
    private int mSelectionType;

    public static Intent getInstance(Context context, @SelectionType int type) {
        Intent intent = new Intent(context, StatusSelectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SELECTION_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();

        mViewModel = new StatusSelectionViewModel(this);

        StatusSelectionContract.Presenter presenter =
            new StatusSelectionPresenter(mViewModel, mSelectionType);
        initPresenter((StatusSelectionPresenter) presenter);

        mViewModel.setPresenter(presenter);

        ActivityNewStatusSelectionBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_new_status_selection);
        binding.setViewModel((StatusSelectionViewModel) mViewModel);
    }

    private void initPresenter(StatusSelectionPresenter presenter) {
        switch (mSelectionType) {
            case STATUS:
                StatusRepository statusRepository = new StatusRepository(
                    new StatusRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setStatusRepository(statusRepository);
                break;
            case CATEGORY:
                CategoryRepository categoryRepository = new CategoryRepository(
                    new CategoryRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setCategoryRepository(categoryRepository);
                break;
            case VENDOR:
                VendorRepository vendorRepository = new VendorRepository(
                    new VendorRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setVendorRepository(vendorRepository);
                break;
            case MARKER:
                MarkerRepository markerRepository = new MarkerRepository(
                    new MarkerRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setMarkerRepository(markerRepository);
                break;
            case MEETING_ROOM:
                MeetingRoomRepository meetingRoomRepository = new MeetingRoomRepository(
                    new MeetingRoomRemoteDataSource(FDMSServiceClient.getInstance()));
                presenter.setMeetingRoomRepository(meetingRoomRepository);
                break;
            default:
                break;
        }
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        mSelectionType = bundle.getInt(BUNDLE_SELECTION_TYPE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mViewModel.onSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // no ops
        return true;
    }

    /**
     * define search type
     */
}