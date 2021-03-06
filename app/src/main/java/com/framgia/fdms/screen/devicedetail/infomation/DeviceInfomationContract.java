package com.framgia.fdms.screen.devicedetail.infomation;

import android.content.Intent;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;

/**
 * This specifies the contract between the view and the presenter.
 */
interface DeviceInfomationContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetDeviceSuccess(Device device);

        void onError();

        void onEditDevice();

        void onDeleteDevice();

        void onDeleteDeviceSuccess(Device device, String message);

        void onDeleteDeviceError(String message);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onLoadData();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void deleteDevice(Device device);
    }
}
