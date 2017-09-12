package com.framgia.fdms.screen.deviceusingmanager.base;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.screen.deviceusingmanager.DeviceUsingManagerContract;

import java.util.List;

/**
 * Created by lamvu on 06/09/2017.
 */

public class DeviceUsingBaseViewModel implements DeviceUsingManagerContract.ViewModel {

    private DeviceUsingManagerContract.Presenter mPresenter;

    public DeviceUsingBaseViewModel() {

    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(DeviceUsingManagerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetDeviceUsingHistorySuccess(List<DeviceUsingHistory> deviceUsingHistories) {

    }

    @Override
    public void onGetDeviceUsingHistoryFailed() {

    }
}