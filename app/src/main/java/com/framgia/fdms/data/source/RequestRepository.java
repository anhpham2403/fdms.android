package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.AssignmentItemRequest;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by beepi on 11/05/2017.
 */

public class RequestRepository implements RequestRepositoryContract {
    private static RequestRepository sRequestRepository;
    private RequestDataSource.RemoteDataSource mRemoteDataSource;

    public RequestRepository(RequestDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public static RequestRepository getInstant(
        RequestDataSource.RemoteDataSource remoteDataSource) {
        if (sRequestRepository == null) {
            sRequestRepository = new RequestRepository(remoteDataSource);
        }
        return sRequestRepository;
    }

    @Override
    public Observable<List<Request>> getRequests(int requestType, int requestStatusId,
        int relativeId, int perPage, int page) {
        return mRemoteDataSource.getRequests(requestType, requestStatusId, relativeId, perPage,
            page);
    }

    @Override
    public Observable<List<Status>> getStatus() {
        return mRemoteDataSource.getStatus();
    }

    @Override
    public Observable<List<Dashboard>> getDashboardRequest() {
        return mRemoteDataSource.getDashboardRequest();
    }

    @Override
    public Observable<Request> registerRequest(RequestCreatorRequest request) {
        return mRemoteDataSource.registerRequest(request);
    }

    @Override
    public Observable<List<Request>> getTopRequest(int topRequest) {
        return mRemoteDataSource.getTopRequest(topRequest);
    }

    @Override
    public Observable<Respone<Request>> updateActionRequest(int requestId, int statusId) {
        return mRemoteDataSource.updateActionRequest(requestId, statusId);
    }

    @Override
    public Observable<Respone<Request>> cancelRequest(int requestId, int statusId,
        String description) {
        return mRemoteDataSource.cancelRequest(requestId, statusId, description);
    }

    @Override
    public Observable<Respone<Request>> updateRequest(Request request) {
        return mRemoteDataSource.updateRequest(request);
    }

    @Override
    public Observable<Request> getRequest(int requetsId) {
        return mRemoteDataSource.getRequest(requetsId);
    }

    @Override
    public Observable<Request> registerAssignment(AssignmentRequest request) {
        return mRemoteDataSource.registerAssignment(request);
    }

    @Override
    public Observable<String> registerAssignment(int staffId, List<AssignmentItemRequest> items) {
        return mRemoteDataSource.registerAssignment(staffId, items);
    }
}
