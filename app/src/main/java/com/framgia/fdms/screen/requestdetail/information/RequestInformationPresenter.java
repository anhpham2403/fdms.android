package com.framgia.fdms.screen.requestdetail.information;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.RequestRemoteDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tuanbg on 5/30/17.
 */
public class RequestInformationPresenter implements RequestInformationContract.Presenter {
    private RequestInformationContract.ViewModel mViewModel;
    private CompositeDisposable mSubscription;
    private RequestRepository mRequestRepository;
    private UserRepository mUserRepository;

    public RequestInformationPresenter(RequestInformationContract.ViewModel viewModel,
        UserRepository userRepository) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
        mRequestRepository =
            new RequestRepository(new RequestRemoteDataSource(FDMSServiceClient.getInstance()));
        mUserRepository = userRepository;
        getCurrentUser();
    }

    public void updateActionRequest(int requestId, int actionId) {
        Disposable subscription = mRequestRepository.updateActionRequest(requestId, actionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<Respone<Request>>() {
                @Override
                public void accept(Respone<Request> requestRespone) throws Exception {
                    mViewModel.onUpdateActionSuccess(requestRespone);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.hideProgressbar();
                    mViewModel.onLoadError(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void updateRequest(Request request) {
        if (TextUtils.isEmpty(request.getTitle())) {
            mViewModel.onRequestTitleEmpty();
            return;
        }
        Disposable subscription = mRequestRepository.updateRequest(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<Respone<Request>>() {
                @Override
                public void accept(Respone<Request> requestRespone) throws Exception {
                    mViewModel.onUpdateActionSuccess(requestRespone);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.hideProgressbar();
                    mViewModel.onUploadRequestError(error.getMessage());
                    mViewModel.hideActionRequestButton();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                    mViewModel.hideActionRequestButton();
                }
            });

        mSubscription.add(subscription);
    }

    @Override
    public void getCurrentUser() {
        Disposable subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.hideProgressbar();
                }
            })
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.setCurrentUser(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                    mViewModel.hideProgressbar();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void cancelRequest(int requestId, int actionId, String description) {
        Disposable subscription = mRequestRepository.cancelRequest(requestId, actionId, description)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<Respone<Request>>() {
                @Override
                public void accept(Respone<Request> requestRespone) throws Exception {
                    mViewModel.onUpdateActionSuccess(requestRespone);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.hideProgressbar();
                    mViewModel.onLoadError(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }
}
