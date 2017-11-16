package com.framgia.fdms.screen.requestcreation;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;
import com.framgia.fdms.screen.requestcreation.assignee.SelectAssigneeRequestActivity;
import com.framgia.fdms.screen.requestcreation.requestfor.SelectRequestForActivity;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.DIVISION_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.GROUP_LEADER;
import static com.framgia.fdms.data.anotation.Permission.SECTION_MANAGER;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.NONE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_ASSIGNEE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_RELATIVE;

/**
 * Exposes the data to be used in the Requestcreation screen.
 */

public class RequestCreationViewModel extends BaseObservable
    implements RequestCreationContract.ViewModel, AdapterView.OnItemSelectedListener {

    private RequestCreationContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private String mRequestTitle;
    private String mRequestDescription;
    private Status mRequestFor;
    private Status mAssignee;
    private RequestCreatorRequest mRequest;
    private Status mDefaultAssignee;

    private String mGroup;
    private ArrayAdapter<String> mAdapter;
    private Context mContext;
    private String mTitleError;
    private String mDescriptionError;
    private String mRequestForError;
    private String mGroupError;
    private int mProgressBarVisibility = View.GONE;
    private boolean mIsManager;
    @RequestCreatorType
    private int mRequestCreatorType;
    private boolean mIsBOManager;

    public RequestCreationViewModel(AppCompatActivity activity,
        @RequestCreatorType int requestCreatorType) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mRequestCreatorType = requestCreatorType;
        mRequest = new RequestCreatorRequest();
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
    public void setPresenter(RequestCreationContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getCurrentUser();
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    @Override
    public void onCreateRequestClick() {
        mPresenter.registerRequest(mRequest);
    }

    @Override
    public void onLoadError(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressbar() {
        setProgressBarVisibility(View.GONE);
    }

    @Override
    public void showProgressbar() {
        setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        Status status = bundle.getParcelable(BUNDLE_DATA);
        assert status != null;
        switch (requestCode) {
            case REQUEST_RELATIVE:
                setRequestFor(status);
                updateDefaultAssignee();
                mPresenter.getGroupByStaffId();
                break;
            case REQUEST_ASSIGNEE:
                setAssignee(status);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetRequestSuccess(Request request) {
        Toast.makeText(mContext, R.string.msg_create_request_success, Toast.LENGTH_SHORT).show();
        finnishActivityWithResult(request);
    }

    public void finnishActivityWithResult(Request request) {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_DATA, request);
        mActivity.setResult(RESULT_OK, intent);
        mActivity.finish();
    }

    @Override
    public void onInputTitleError() {
        mTitleError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.titleError);
    }

    @Override
    public void onInputDescriptionError() {
        mDescriptionError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.descriptionError);
    }

    @Override
    public void onInputRequestForError() {
        mRequestForError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.requestForError);
    }

    @Override
    public void onInputGroupForError() {
        mGroupError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.groupError);
    }

    @Override
    public void onGetUserSuccess(User user) {
        setBOManager(user.getRole().equals(BO_MANAGER));
        setManager((user.getRole().equals(BO_MANAGER)
            || user.getRole().equals(GROUP_LEADER)
            || user.getRole().equals(DIVISION_MANAGER)
            || user.equals(SECTION_MANAGER)));
        if (isManager()) {
            if (mRequestCreatorType == RequestCreatorType.MY_REQUEST) {
                setRequestFor(new Status(user.getId(), user.getName()));
            } else {
                setRequestFor(new Status(OUT_OF_INDEX, NONE));
            }
            setAssignee(new Status(OUT_OF_INDEX, NONE));
        }
    }

    @Override
    public void onGetDefaultAssignSuccess(Status assignee) {
        mDefaultAssignee = assignee;
    }

    @Override
    public void onGetGroupSuccess(List<String> groups) {
        setAdapter(new ArrayAdapter<>(mContext, R.layout.item_group, groups));
    }

    @Bindable
    public String getRequestTitle() {
        return mRequestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        mRequest.setTitle(requestTitle);
        mRequestTitle = requestTitle;
    }

    @Bindable
    public String getRequestDescription() {
        return mRequestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        mRequest.setDescription(requestDescription);
        mRequestDescription = requestDescription;
    }

    @Bindable
    public Status getRequestFor() {
        return mRequestFor;
    }

    public void setRequestFor(Status requestFor) {
        mRequestFor = requestFor;
        mRequest.setRequestFor(requestFor.getId());
        notifyPropertyChanged(BR.requestFor);
    }

    @Bindable
    public Status getAssignee() {
        return mAssignee;
    }

    public void setAssignee(Status assignee) {
        mAssignee = assignee;
        mRequest.setAssignee(assignee.getId());
        notifyPropertyChanged(BR.assignee);
    }

    @Bindable
    public String getTitleError() {
        return mTitleError;
    }

    @Bindable
    public String getDescriptionError() {
        return mDescriptionError;
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setProgressBarVisibility(int progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    @Bindable
    public boolean isManager() {
        return mIsManager;
    }

    private void setManager(boolean manager) {
        mIsManager = manager;
        notifyPropertyChanged(BR.manager);
    }

    @Bindable
    public int getRequestCreatorType() {
        return mRequestCreatorType;
    }

    public void setRequestCreatorType(int requestCreatorType) {
        this.mRequestCreatorType = requestCreatorType;
        notifyPropertyChanged(BR.requestCreatorType);
    }

    @Bindable
    public String getRequestForError() {
        return mRequestForError;
    }

    public void setRequestForError(String requestForError) {
        mRequestForError = requestForError;
        notifyPropertyChanged(BR.requestForError);
    }

    public void onClickChooseRequestForRelativeStaff() {
        mActivity.startActivityForResult(SelectRequestForActivity.getInstance(mContext),
            REQUEST_RELATIVE);
    }

    public void updateDefaultAssignee() {
        if (mAssignee.getId() < 0 && mDefaultAssignee != null) {
            setAssignee(mDefaultAssignee);
        }
    }

    public void onClickAssignee() {
        mActivity.startActivityForResult(SelectAssigneeRequestActivity.getInstance(mContext),
            REQUEST_ASSIGNEE);
    }

    @Bindable
    public ArrayAdapter<String> getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setGroup((String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setGroup((String) parent.getItemAtPosition(0));
    }

    @Bindable
    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
        mRequest.setGroup(group);
        notifyPropertyChanged(BR.group);
    }

    @Bindable
    public String getGroupError() {
        return mGroupError;
    }

    public void setGroupError(String groupError) {
        mGroupError = groupError;
        notifyPropertyChanged(BR.groupError);
    }

    @Bindable
    public boolean isBOManager() {
        return mIsBOManager;
    }

    public void setBOManager(boolean BOManager) {
        mIsBOManager = BOManager;
        notifyPropertyChanged(BR.bOManager);
    }
}
