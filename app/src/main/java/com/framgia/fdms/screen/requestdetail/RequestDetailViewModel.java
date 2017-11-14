package com.framgia.fdms.screen.requestdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.screen.requestdetail.information.RequestInformationFragment;
import com.framgia.fdms.screen.requestdetail.listdeviceassignment.ListDeviceAssignmentFragment;
import com.framgia.fdms.utils.navigator.Navigator;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_RESPONE;

/**
 * Exposes the data to be used in the Devicedetail screen.
 */

public class RequestDetailViewModel extends BaseObservable
    implements RequestDetailContract.ViewModel {

    private RequestDetailContract.Presenter mPresenter;
    private ObservableField<RequestDetailPagerAdapter> mAdapter = new ObservableField<>();
    private Context mContext;
    private AppCompatActivity mActivity;
    private Navigator mNavigator;
    private ObservableField<Integer> mProgressBarVisibility = new ObservableField<>();

    public RequestDetailViewModel(AppCompatActivity activity, Navigator navigator) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        mNavigator = navigator;
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
    public void setPresenter(RequestDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public ObservableField<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    public void onClickArrowBack() {
        mNavigator.finishActivity();
    }

    @Override
    public void onGetRequestSuccess(Request request) {
        RequestDetailPagerAdapter adapter =
            new RequestDetailPagerAdapter(mContext, mActivity.getSupportFragmentManager());
        adapter.addFragment(RequestInformationFragment.newInstance(request));
        adapter.addFragment(ListDeviceAssignmentFragment.newInstance(request));
        mAdapter.set(adapter);
        setResultUpdateActivity(request);
    }

    private void setResultUpdateActivity(Request request) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_RESPONE, request);
        intent.putExtras(bundle);
        mActivity.setResult(RESULT_OK, intent);
    }

    @Bindable
    public ObservableField<RequestDetailPagerAdapter> getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ObservableField<RequestDetailPagerAdapter> adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onGetRequestFailure(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onUpdateSuccessFull(int requestId) {
        mPresenter.getRequest(requestId);
    }
}
