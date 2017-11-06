package com.framgia.fdms.screen.request.requestmanager;

import android.content.Intent;
import com.framgia.fdms.BaseFragmentContract;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface RequestManagerContract {
    /**
     * View.
     */
    interface ViewModel extends BaseFragmentContract.ViewModel {
        void onGetRequestSuccess(List<Request> requests);

        void onLoadError(String msg);

        void getData();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onUpdateActionSuccess(Request request);

        void refreshData();

        void setCurrentUser(User user);

        void setRefresh(boolean isRefresh);

        void onGetRequestError();

        void onRegisterRequestClick();

        void setAllowLoadMore(boolean allowLoadMore);

        void onActionRequestClick(int acionId, int requestId);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BaseFragmentContract.Presenter {
        void getRequest(int requestStatusId, int relativeId, int perPage, int page);

        void updateActionRequest(int requestId, int actionId);

        void getCurrentUser();

        void cancelRequest(int reqeuestId, int actionId, String description);
    }
}
