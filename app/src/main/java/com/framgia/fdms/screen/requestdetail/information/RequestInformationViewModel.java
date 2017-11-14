package com.framgia.fdms.screen.requestdetail.information;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.assignment.AssignmentActivity;
import com.framgia.fdms.screen.requestdetail.RequestDetailActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.screen.selection.SelectionType;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.BO_STAFF;
import static com.framgia.fdms.data.anotation.RequestStatus.APPROVED;
import static com.framgia.fdms.data.anotation.RequestStatus.ASSIGNMENT;
import static com.framgia.fdms.data.anotation.RequestStatus.CANCELLED;
import static com.framgia.fdms.data.anotation.RequestStatus.DONE;
import static com.framgia.fdms.data.anotation.RequestStatus.EDIT;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_APPROVE;
import static com.framgia.fdms.data.anotation.RequestStatus.WAITING_DONE;
import static com.framgia.fdms.screen.assignment.AssignmentType.ASSIGN_BY_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.EDIT_STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_ASSIGNEE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CREATE_ASSIGNMENT;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_RELATIVE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_STATUS;
import static com.framgia.fdms.utils.Utils.hideSoftKeyboard;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;

/**
 * Created by tuanbg on 5/24/17.
 */
public class RequestInformationViewModel extends BaseObservable
    implements RequestInformationContract.ViewModel {
    private Context mContext;
    private Fragment mFragment;
    private boolean mIsEdit;
    private RequestInformationContract.Presenter mPresenter;
    private List<Request.RequestAction> mListAction = new ArrayList<>();
    private String mStatusRequest;
    private Request mRequest;
    private int mProgressBarVisibility = View.GONE;
    private User mUser;
    private FloatingActionMenu mFloatingActionsMenu;
    private Request mRequestTemp;
    private int mActionMenuVisibility;
    private String mRequestTitleEmpty;
    private boolean isAcceptStatus;
    private OnRequestUpdateSuccessListenner mListenner;
    private boolean mIsShowRequestForAndAssignee;

    public RequestInformationViewModel(Fragment fragment, List<Request.RequestAction> actions,
        String statusRequest, Request actionRequest, FloatingActionMenu floatingActionMenu,
        OnRequestUpdateSuccessListenner listenner) {
        mContext = fragment.getContext();
        mFragment = fragment;
        setRequest(actionRequest);
        mStatusRequest = statusRequest;
        setEdit(false);
        mListAction.addAll(actions);
        mFloatingActionsMenu = floatingActionMenu;
        mListenner = listenner;
    }

    @Override
    public void showProgressbar() {
        setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        setProgressBarVisibility(View.GONE);
    }

    @Override
    public void onLoadError(String message) {
        Snackbar.make(mFragment.getActivity().findViewById(android.R.id.content), message,
            Snackbar.LENGTH_LONG).show();
    }

    @Bindable
    public boolean isEdit() {
        return mIsEdit;
    }

    public void setEdit(boolean edit) {
        mIsEdit = edit;
        notifyPropertyChanged(BR.edit);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void setPresenter(RequestInformationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        Status status = bundle.getParcelable(BUNDLE_DATA);

        switch (requestCode) {
            case REQUEST_STATUS:
                if (status == null || status.getId() <= 0) {
                    return;
                }
                mRequest.setRequestStatus(status.getName());
                mRequest.setRequestStatusId(status.getId());
                break;
            case REQUEST_RELATIVE:
                if (status == null || status.getId() <= 0) {
                    return;
                }
                mRequest.setRequestForId(status.getId());
                mRequest.setRequestFor(status.getName());
                break;
            case REQUEST_ASSIGNEE:
                if (status == null || status.getId() <= 0) {
                    return;
                }
                mRequest.setAssigneeId(status.getId());
                mRequest.setAssignee(status.getName());
                break;
            case REQUEST_CREATE_ASSIGNMENT:
                onUpdateActionSuccess(null);

                Activity activity = mFragment.getActivity();
                Intent intent = new Intent();
                activity.setResult(RESULT_OK, intent);
                activity.startActivity(
                    RequestDetailActivity.getInstance(mFragment.getContext(), mRequest.getId()));
                activity.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSubmitEditClick() {
        mPresenter.updateRequest(mRequest);
        hideSoftKeyboard(mFragment.getActivity());
    }

    public void onChangeStatusClick() {
        if (!isEdit()) {
            return;
        }
        mFragment.startActivityForResult(
            SelectionActivity.newInstance(mContext, EDIT_STATUS_REQUEST,
                mRequest.getRequestStatusId()), REQUEST_STATUS);
    }

    public void onClickChooseRequestForRelativeStaff() {
        if (!isEdit() || APPROVED == mRequest.getRequestStatusId()) {
            return;
        }
        mFragment.startActivityForResult(
            SelectionActivity.getInstance(mContext, SelectionType.RELATIVE_STAFF),
            REQUEST_RELATIVE);
    }

    public void onClickAssignee() {
        mFragment.startActivityForResult(
            SelectionActivity.getInstance(mContext, SelectionType.ASSIGNEE), REQUEST_ASSIGNEE);
    }

    @Override
    public void onCancelEditClick() {
        setEdit(false);
        mFloatingActionsMenu.showMenu(true);
        if (mRequestTemp != null) {
            setRequest(mRequestTemp);
        }
        hideSoftKeyboard(mFragment.getActivity());
    }

    @Override
    public boolean onBackPressed() {
        if (isEdit()) {
            setEdit(false);
            return false;
        }
        return true;
    }

    public void initActionRequestMenu() {
        if (mRequest.getRequestStatusId() == DONE) {
            return;
        }
        if (isAllowEditAction(mUser, mRequest)) {
            Request.RequestAction editAction = new Request().new RequestAction();
            editAction.setId(EDIT);
            editAction.setName(mContext.getString(R.string.action_edit));
            mListAction.add(editAction);
        }
        if (isAllowAssignDevice(mUser.getRole(), mRequest)) {
            Request.RequestAction assignAction = new Request().new RequestAction();
            assignAction.setId(ASSIGNMENT);
            assignAction.setName(mContext.getString(R.string.title_assignment));
            mListAction.add(assignAction);
        }

        for (final Request.RequestAction requestAction : mListAction) {
            final FloatingActionButton button = new FloatingActionButton(mContext);
            switch (requestAction.getId()) {
                case CANCELLED:
                    button.setImageResource(R.drawable.ic_cancel_white_24px);
                    break;
                case WAITING_APPROVE:
                    button.setImageResource(R.drawable.ic_timer);
                    break;
                case WAITING_DONE:
                    button.setImageResource(R.drawable.ic_done_white_24dp);
                    break;
                case DONE:
                    button.setImageResource(R.drawable.ic_receive_24dp);
                    break;
                case EDIT:
                    button.setImageResource(R.drawable.ic_edit_white_24dp);
                    break;
                case ASSIGNMENT:
                    button.setImageResource(R.drawable.ic_assignment_white);
                    break;
                default:
                    break;
            }

            button.setLabelText(requestAction.getName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionRequestClick(mRequest.getId(), requestAction.getId());
                }
            });
            button.setButtonSize(SIZE_MINI);
            mFloatingActionsMenu.addMenuButton(button);
        }
    }

    public boolean isAllowAssignDevice(String permision, Request request) {
        return (permision.equals(BO_MANAGER) || permision.equals(BO_STAFF))
            && request.getRequestStatusId() == WAITING_DONE;
    }

    public boolean isAllowEditAction(User user, Request request) {
        switch (request.getRequestStatusId()) {
            case CANCELLED:
            case WAITING_DONE:
                return (user.getRole().equals(BO_MANAGER) && user.getName()
                    .equals(request.getCreater()));
            case APPROVED:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActionRequestClick(final int requestId, final int actionId) {
        switch (actionId) {
            case ASSIGNMENT:
                mFragment.startActivityForResult(
                    AssignmentActivity.getInstance(mContext, ASSIGN_BY_REQUEST, mRequest.getId()),
                    REQUEST_CREATE_ASSIGNMENT);
                break;
            case EDIT:
                initEditRequest();
                break;
            case CANCELLED:
                new LovelyTextInputDialog(mContext).setTopColorRes(R.color.colorPrimary)
                    .setTitle(R.string.msg_cancel_request)
                    .setIcon(R.drawable.ic_error_white)
                    .setConfirmButton(android.R.string.ok,
                        new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                mPresenter.cancelRequest(requestId, actionId, text);
                            }
                        })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setInputFilter(R.string.error_empty_description,
                        new LovelyTextInputDialog.TextFilter() {
                            @Override
                            public boolean check(String s) {
                                return !TextUtils.isEmpty(s);
                            }
                        })
                    .show();
                break;
            default:
                mPresenter.updateActionRequest(requestId, actionId);
                break;
        }

        mFloatingActionsMenu.close(true);
    }

    @Override
    public void onRequestTitleEmpty() {
        setRequestTitleEmpty(mContext.getString(R.string.msg_error_user_name));
    }

    @Override
    public void hideActionRequestButton() {
        mFloatingActionsMenu.showMenu(true);
        setEdit(false);
    }

    @Override
    public void onUpdateActionSuccess(Respone<Request> requestRespone) {
        Toast.makeText(mFragment.getContext(), R.string.msg_update_request_success,
            Toast.LENGTH_SHORT).show();
        mListenner.onUpdateSuccessFull(requestRespone.getData().getId());
    }

    @Override
    public void setCurrentUser(User user) {
        if (user == null) {
            return;
        }
        setUser(user);
        initActionRequestMenu();
        setActionMenuVisibility(
            mListAction != null && mListAction.size() > 0 ? View.VISIBLE : View.GONE);
        setShowRequestForAndAssignee(
            mUser.getRole().equals(BO_MANAGER) || user.getRole().equals(BO_STAFF));
    }

    @Override
    public void onUploadRequestError(String message) {
        Snackbar.make(mFragment.getActivity().findViewById(android.R.id.content), message,
            Snackbar.LENGTH_LONG).show();
        onCancelEditClick();
    }

    public void initEditRequest() {
        setEdit(true);
        if (APPROVED == mRequest.getRequestStatusId()) {
            setAcceptStatus(true);
        }
        mFloatingActionsMenu.hideMenu(true);
        try {
            mRequestTemp = (Request) mRequest.clone();
            List<Request.DeviceRequest> list = new ArrayList<>();
            for (Request.DeviceRequest deviceRequest : mRequest.getDevices()) {
                list.add((Request.DeviceRequest) deviceRequest.clone());
            }
            mRequestTemp.setDevices(list);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public String getStatusRequest() {
        return mStatusRequest;
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
    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request request) {
        mRequest = request;
        notifyPropertyChanged(BR.request);
    }

    @Bindable
    public int getActionMenuVisibility() {
        return mActionMenuVisibility;
    }

    public void setActionMenuVisibility(int actionMenuVisibility) {
        mActionMenuVisibility = actionMenuVisibility;
        notifyPropertyChanged(BR.actionMenuVisibility);
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public String getRequestTitleEmpty() {
        return mRequestTitleEmpty;
    }

    public void setRequestTitleEmpty(String requestTitleEmpty) {
        mRequestTitleEmpty = requestTitleEmpty;
        notifyPropertyChanged(BR.requestTitleEmpty);
    }

    @Bindable
    public boolean isAcceptStatus() {
        return isAcceptStatus;
    }

    public void setAcceptStatus(boolean acceptStatus) {
        isAcceptStatus = acceptStatus;
        notifyPropertyChanged(BR.acceptStatus);
    }

    @Bindable
    public boolean isShowRequestForAndAssignee() {
        return mIsShowRequestForAndAssignee;
    }

    public void setShowRequestForAndAssignee(boolean showRequestForAndAssignee) {
        mIsShowRequestForAndAssignee = showRequestForAndAssignee;
        notifyPropertyChanged(BR.showRequestForAndAssignee);
    }
}
