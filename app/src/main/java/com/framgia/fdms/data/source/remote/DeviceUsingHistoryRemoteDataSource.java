package com.framgia.fdms.data.source.remote;

import android.content.Context;
import android.text.TextUtils;

import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.anotation.UsingStatus;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.DeviceUsingHistoryDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.screen.deviceusingmanager.DeviceUsingHistoryFilter;
import com.framgia.fdms.utils.Constant;
import com.framgia.fdms.utils.Utils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.framgia.fdms.utils.Constant.ApiParram.BRANCH_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.PER_PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.STATUS;
import static com.framgia.fdms.utils.Constant.ApiParram.TEXT_USER_SEARCH;
import static com.framgia.fdms.utils.Constant.ApiParram.TEXT_DEVICE_SEARCH;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by framgia on 14/07/2017.
 */
public class DeviceUsingHistoryRemoteDataSource extends BaseRemoteDataSource
        implements DeviceUsingHistoryDataSource.RemoteDataSource {
    private static DeviceUsingHistoryRemoteDataSource sInstances;

    public DeviceUsingHistoryRemoteDataSource(FDMSApi fdmsApi) {
        super(fdmsApi);
    }

    public static DeviceUsingHistoryRemoteDataSource getInstances() {
        if (sInstances == null) {
            sInstances = new DeviceUsingHistoryRemoteDataSource(FDMSServiceClient.getInstance());
        }
        return sInstances;
    }

    @Override
    public Observable<List<DeviceUsingHistory>> getListDeviceHistory(
            DeviceUsingHistoryFilter filter, int page, int perPage) {
        return mFDMSApi.getAllDeviceUsingHistory(getParams(filter, page, perPage))
                .flatMap(
                        new Function<Respone<List<DeviceUsingHistory>>,
                                ObservableSource<List<DeviceUsingHistory>>>() {
                            @Override
                            public ObservableSource<List<DeviceUsingHistory>> apply(
                                    Respone<List<DeviceUsingHistory>> listRespone) throws Exception {
                                return Utils.getResponse(listRespone);
                            }
                        });
    }

    @Override
    public Observable<List<Status>> getUsingStatuses() {
        final Context context = FDMSApplication.getInstant();
        return Observable.create(new ObservableOnSubscribe<List<Status>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Status>> observableEmitter)
                    throws Exception {
                List<Status> statuses = new ArrayList<>();
                statuses.add(new Status(UsingStatus.ALL,
                        context.getString(R.string.title_using_statuses_all)));
                statuses.add(new Status(UsingStatus.USING,
                        context.getString(R.string.title_using_statuses_using)));
                statuses.add(new Status(UsingStatus.RETURNED,
                        context.getString(R.string.title_using_statuses_returned)));
                observableEmitter.onNext(statuses);
                observableEmitter.onComplete();
            }
        });
    }

    private Map<String, String> getParams(DeviceUsingHistoryFilter filter, int page, int perPage) {
        Map<String, String> param = new HashMap<>();
        if (page != OUT_OF_INDEX) {
            param.put(PAGE, String.valueOf(page));
        }
        if (perPage != OUT_OF_INDEX) {
            param.put(PER_PAGE, String.valueOf(perPage));
        }
        if (filter == null) {
            return param;
        }

        if (filter.getStatus() != null && filter.getStatus().getId() != -1) {
            switch (filter.getStatus().getId()) {
                case UsingStatus.USING:
                    param.put(STATUS, Constant.DeviceUsingStatus.USING);
                    break;
                case UsingStatus.RETURNED:
                    param.put(STATUS, Constant.DeviceUsingStatus.RETURN);
                default:
                    break;
            }
        }
        if (filter.getBranch() != null && filter.getBranch().getId() != -1) {
            param.put(BRANCH_ID, String.valueOf(filter.getBranch().getId()));
        }
        if (!TextUtils.isEmpty(filter.getStaffName())) {
            param.put(TEXT_USER_SEARCH, filter.getStaffName());
        }
        if (!TextUtils.isEmpty(filter.getDeviceCode())) {
            param.put(TEXT_DEVICE_SEARCH, filter.getDeviceCode());
        }

        return param;
    }
}
