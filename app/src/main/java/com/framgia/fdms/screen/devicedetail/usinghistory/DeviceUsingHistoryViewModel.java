package com.framgia.fdms.screen.devicedetail.usinghistory;

import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.BaseFragmentContract;
import com.framgia.fdms.BaseFragmentModel;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.user.UserActivity;
import com.framgia.fdms.utils.navigator.Navigator;

import java.util.List;

/**
 * Exposes the data to be used in the UsingHistory screen.
 */

public class DeviceUsingHistoryViewModel extends BaseFragmentModel
        implements DeviceUsingHistoryContract.ViewModel,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<DeviceUsingHistory> {

    private BaseFragmentContract.Presenter mPresenter;
    private DeviceUsingHistoryContract.Presenter mDeviceUsingHistoryPresenter;
    private DeviceUsingAdapter mAdapter;
    private String mErrorLoadingMessage;
    private Navigator mNavigator;
    private int mEmptyViewVisible;

    public DeviceUsingHistoryViewModel(Fragment fragment) {
        mAdapter = new DeviceUsingAdapter();
        mAdapter.setListener(this);
        mNavigator = new Navigator(fragment);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    public void setPresenter(BaseFragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setDeviceUsingHistoryPresenter(DeviceUsingHistoryContract.Presenter presenter) {
        mDeviceUsingHistoryPresenter = presenter;
    }

    @Bindable
    public DeviceUsingAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(DeviceUsingAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onGetUsingHistoryDeviceSuccess(List<DeviceUsingHistory> histories) {
        mAdapter.addData(histories);
        setEmptyViewVisible(histories != null && histories.size() != 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onGetUsingHistoryDeviceFailed(String msg) {
        setEmptyViewVisible(View.VISIBLE);
        setErrorLoadingMessage(msg);
    }

    @Override
    public void onLoadData() {
        mDeviceUsingHistoryPresenter.getUsingHistoryDevice();
    }

    @Override
    public void onItemRecyclerViewClick(DeviceUsingHistory item) {
        User user = new User();
        user.setName(item.getStaffName());
        user.setEmail(item.getStaffEmail());
        mNavigator.startActivity(UserActivity.getInstance(mNavigator.getContext(), user));
    }

    @Bindable
    public String getErrorLoadingMessage() {
        return mErrorLoadingMessage;
    }

    public void setErrorLoadingMessage(String errorLoadingMessage) {
        mErrorLoadingMessage = errorLoadingMessage;
        notifyPropertyChanged(BR.errorLoadingMessage);
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }
}
