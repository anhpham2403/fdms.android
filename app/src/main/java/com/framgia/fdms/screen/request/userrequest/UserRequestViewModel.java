package com.framgia.fdms.screen.request.userrequest;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.framgia.fdms.BR;
import com.framgia.fdms.BaseFragmentContract;
import com.framgia.fdms.BaseFragmentModel;
import com.framgia.fdms.R;
import com.framgia.fdms.data.anotation.RequestStatus;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.assignment.AssignmentActivity;
import com.framgia.fdms.screen.assignment.AssignmentType;
import com.framgia.fdms.screen.request.OnRequestClickListenner;
import com.framgia.fdms.screen.requestcreation.RequestCreationActivity;
import com.framgia.fdms.screen.requestdetail.RequestDetailActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.screen.requestcreation.RequestCreatorType.MY_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.REQUEST_CREATED_BY;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST_ALL;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_RESPONE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CREATE_ASSIGNMENT;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CREATE_REQUEST;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DETAIL;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_REQUEST_CREATED_BY;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_STATUS;

/**
 * Exposes the data to be used in the RequestManager screen.
 */

public class UserRequestViewModel extends BaseFragmentModel
        implements UserRequestContract.ViewModel, OnRequestClickListenner,
        DatePickerDialog.OnDateSetListener {

    private final Context mContext;
    private final Fragment mFragment;
    private UserRequestAdapter mAdapter;

    private Status mStatus, mRelative, mRequestBy;
    private boolean mIsRefresh;
    private ObservableField<User> mUser = new ObservableField<>();
    private int mEmptyViewVisible = View.GONE; // show empty state when no date
    private Calendar mCalendar;
    private String mFromTime;
    private String mToTime;
    private boolean mFlagTime;
    private SwipeRefreshLayout.OnRefreshListener mRefreshLayout =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mAdapter.clear();
                    getData();
                }
            };

    private int mScrollPosition;

    public UserRequestViewModel(FragmentActivity activity, Fragment fragment) {
        mContext = activity.getApplicationContext();
        mFragment = fragment;
        mAdapter = new UserRequestAdapter(mContext, new ArrayList<Request>(), this, new User());
        setStatus(new Status(OUT_OF_INDEX, mContext.getString(R.string.title_all_request)));
        setRelative(new Status(OUT_OF_INDEX, mContext.getString(R.string.title_request_relative)));
        setRequestBy(new Status(OUT_OF_INDEX, mContext.getString(R.string.text_request_for_me)));
        mCalendar = Calendar.getInstance();
    }

    @Override
    public void onStart() {
        if (mPresenter == null) return;
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    public void setPresenter(BaseFragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public UserRequestAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(UserRequestAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onGetRequestSuccess(List<Request> requests) {
        setLoadMore(false);
        mAdapter.onUpdatePage(requests);
        setEmptyViewVisible(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onGetRequestError() {
        setLoadMore(false);
        setEmptyViewVisible(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRegisterRequestClick() {
        mFragment.startActivityForResult(
                RequestCreationActivity.getInstance(mFragment.getActivity(), MY_REQUEST),
                REQUEST_CREATE_REQUEST);
    }

    @Override
    public void setAllowLoadMore(boolean allowLoadMore) {
        mIsAllowLoadMore = allowLoadMore;
    }

    @Override
    public void onLoadError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getData() {
        mPresenter.getData(null, null, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getExtras() == null || resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case REQUEST_STATUS:
                Status status = bundle.getParcelable(BUNDLE_DATA);
                if (status == null) {
                    return;
                }
                if (status.getId() == OUT_OF_INDEX) {
                    status.setName(mContext.getString(R.string.title_all_request));
                }
                setStatus(status);
                mAdapter.clear();
                mPresenter.getData(mRelative, mStatus, null);
                break;
            case REQUEST_DETAIL:
                Respone<Request> requestRespone =
                        (Respone<Request>) bundle.getSerializable(BUNDLE_RESPONE);
                if (requestRespone != null) {
                    onUpdateActionSuccess(requestRespone);
                }
                break;
            case REQUEST_REQUEST_CREATED_BY:
                status = bundle.getParcelable(BUNDLE_DATA);
                if (status == null) {
                    return;
                }
                setRequestBy(status);
                mAdapter.clear();
                mPresenter.getData(mRequestBy, mStatus, null);
                break;

            case REQUEST_CREATE_REQUEST:
                Request request = (Request) bundle.getSerializable(BUNDLE_DATA);
                if (request != null) {
                    mAdapter.addRequest(request);
                    setScrollPosition(0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdateActionSuccess(Respone<Request> requestRespone) {
        if (requestRespone == null || requestRespone.getData() == null) return;
        mAdapter.updateItem(requestRespone.getData());
        Snackbar.make(mFragment.getView(), requestRespone.getMessage(), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void refreshData() {
        mAdapter.clear();
        mPresenter.getData(mRelative, mStatus, null);
    }

    @Override
    public void setCurrentUser(User user) {
        if (user == null) return;
        mAdapter.updateUser(user);
        mUser.set(user);
    }

    public void onSelectStatusClick() {
        mFragment.startActivityForResult(
                SelectionActivity.getInstance(mContext, STATUS_REQUEST_ALL), REQUEST_STATUS);
    }

    public void onSelectTypeRequestClick() {
        mFragment.startActivityForResult(
                SelectionActivity.getInstance(mContext, REQUEST_CREATED_BY),
                REQUEST_REQUEST_CREATED_BY);
    }

    @Bindable
    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public Status getRelative() {
        return mRelative;
    }

    public void setRelative(Status relative) {
        mRelative = relative;
        notifyPropertyChanged(BR.relative);
    }

    @Override
    public void onMenuClick(View v, final UserRequestAdapter.RequestModel request) {
        if (request == null
                || request.getRequest() == null
                || request.getRequest().getRequestActionList() == null) {
            return;
        }
        Request requestModel = request.getRequest();
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        for (int i = 0; i < requestModel.getRequestActionList().size(); i++) {
            final Request.RequestAction action = requestModel.getRequestActionList().get(i);
            popupMenu.getMenu()
                    .add(action.getName())
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            onActionRequestClick(request.getRequest().getId(), action.getId());
                            return false;
                        }
                    });
        }
        popupMenu.show();
    }

    @Override
    public void onActionRequestClick(final int reqeuestId, final int actionId) {
        switch (actionId) {
            case RequestStatus.CANCELLED:
                new LovelyTextInputDialog(mContext).setTopColorRes(R.color.colorPrimary)
                        .setTitle(R.string.msg_cancel_request)
                        .setIcon(R.drawable.ic_error_white)
                        .setConfirmButton(android.R.string.ok,
                                new LovelyTextInputDialog.OnTextInputConfirmListener() {
                                    @Override
                                    public void onTextInputConfirmed(String text) {
                                        if (TextUtils.isEmpty(text)) {
                                            return;
                                        }
                                        ((UserRequestContract.Presenter) mPresenter).cancelRequest(
                                                reqeuestId, actionId, text);
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setInputFilter(R.string.error_empty_description,
                                new LovelyTextInputDialog.TextFilter() {
                                    @Override
                                    public boolean check(String s) {
                                        // TODO: 10/23/2017 test later
                                        return !TextUtils.isEmpty(s);
                                    }
                                })
                        .setCancelable(false)
                        .show();
                break;
            default:
                ((UserRequestContract.Presenter) mPresenter).updateActionRequest(reqeuestId,
                        actionId);
                break;
        }
    }

    @Override
    public void onDetailRequestClick(Request request) {
        mFragment.startActivityForResult(
                RequestDetailActivity.getInstance(mContext, request.getId()), REQUEST_DETAIL);
    }

    @Override
    public void onAddDeviceClick(int requestId) {
        mFragment.startActivityForResult(
                AssignmentActivity.getInstance(mContext, AssignmentType.ASSIGN_BY_REQUEST, requestId),
                REQUEST_CREATE_ASSIGNMENT);
    }

    @Bindable
    public boolean isRefresh() {
        return mIsRefresh;
    }

    public void setRefresh(boolean refresh) {
        mIsRefresh = refresh;
        notifyPropertyChanged(BR.refresh);
    }

    public SwipeRefreshLayout.OnRefreshListener getRefreshLayout() {
        return mRefreshLayout;
    }

    @Bindable
    public ObservableField<User> getUser() {
        return mUser;
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }

    private void datePickerDialog() {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        DatePickerDialog datePicker =
                DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show(mFragment.getActivity().getFragmentManager(), "");
    }

    public void onPickFromTime() {
        setFlagTime(true);
        datePickerDialog();
    }

    public void onPickToTime() {
        setFlagTime(false);
        datePickerDialog();
    }

    public void onClearFromTime() {
        setFromTime("");
    }

    public void onClearToTime() {
        setToTime("");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        if (mFlagTime) {
            setFromTime(time);
            return;
        }
        setToTime(time);
    }

    @Bindable
    public String getFromTime() {
        return mFromTime;
    }

    private void setFromTime(String fromTime) {
        mFromTime = fromTime;
        notifyPropertyChanged(BR.fromTime);
    }

    @Bindable
    public String getToTime() {
        return mToTime;
    }

    private void setToTime(String toTime) {
        mToTime = toTime;
        notifyPropertyChanged(BR.toTime);
    }

    @Bindable
    public boolean isFlagTime() {
        return mFlagTime;
    }

    private void setFlagTime(boolean flagTime) {
        mFlagTime = flagTime;
        notifyPropertyChanged(BR.flagTime);
    }

    @Bindable
    public Status getRequestBy() {
        return mRequestBy;
    }

    private void setRequestBy(Status requestBy) {
        mRequestBy = requestBy;
        notifyPropertyChanged(BR.requestBy);
    }

    @Bindable
    public int getScrollPosition() {
        return mScrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        mScrollPosition = scrollPosition;
        notifyPropertyChanged(BR.scrollPosition);
    }
}
