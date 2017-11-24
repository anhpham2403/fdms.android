package com.framgia.fdms.utils;

/**
 * Created by Age on 4/3/2017.
 */
public class Constant {
    // TODO: 4/3/2017 develop later
    public static final String END_POINT_URL = "http://stg-dms.framgia.vn/";
    public static final int OUT_OF_INDEX = -1;
    public static final int PER_PAGE = 20;
    public static final int FIRST_PAGE = 1;
    public static final int PICK_IMAGE_REQUEST = 4;
    public static final int ALL_REQUEST_STATUS_ID = -1;
    public static final int ALL_RELATIVE_ID = -1;
    public static final String PERCENT = " %";
    public static final String NOT_SEARCH = "NOT_SEARCH";
    public static final int USING = 1;
    public static final int AVAIABLE = 2;
    public static final int BROKEN = 3;
    public static final String TYPE_DIALOG = "TYPE_DIALOG";
    public static final String FOLDER_NAME_FAMS = "Report FAMS";
    public static final String TYPE_PDF = "application/pdf";
    public static final String TYPE_WORD = "application/msword";
    public static final String TITLE_NOW = "Now";
    public static final String TAG_PRODUCER_DIALOG = "PRODUCER_DIALOG";
    public static final String DRAWER_IS_CLOSE = "CLOSE";
    public static final String DRAWER_IS_OPEN = "OPEN";
    public static final String TITLE_ALL = "All";
    public static final String NONE = "None";

    public static final class BundleConstant {
        public static final String BUNDLE_TYPE = "BUNDLE_TYPE";
        public static final String BUNDLE_DEVICE = "BUNDLE_DEVICE";
        public static final String BUNDLE_MEETING_ROOM = "BUNDLE_MEETING_ROOM";
        public static final String BUNDLE_CONTENT = "BUNDLE_CONTENT";
        public static final String BUNDLE_RESPONE = "BUNDLE_RESPONE";
        public static final String EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID";
        public static final String EXTRA_DEVICE_CODE = "EXTRA_DEVICE_CODE";
        public static final String BUNDLE_TAB = "BUNDLE_TAB";
        public static final String BUNDLE_USER = "USER_BUND";
        public static final String BUNDLE_PRODUCER = "BUNDLE_PRODUCER";
        public static final String BUNDLE_TITLE = "BUNDLE_TITLE";
        public static final String BUNDLE_ACTION_CALLBACK = "BUNDLE_ACTION_CALLBACK";
        public static final String BUNDLE_DEVICES = "BUNDLE_DEVICES";
        public static final String BUNDLE_DEIVER_USER = "BUNDLE_DEIVER_USER";
        public static final String BUNDLE_RECEIVER_USER = "BUNDLE_RECEIVER_USER";
        public static final String BUNDLE_CATEGORY_ID = "BUNDLE_CATEGORY_ID";
        public static final String BUNDLE_SUCCESS = "BUNDLE_SUCCESS";
        public static final String BUNDLE_SHOW_GROUP_DEVICE = "BUNDLE_SHOW_GROUP_DEVICE";
        public static final String BUNDLE_DEVICE_GROUP_TYPE = "BUNDLE_DEVICE_GROUP_TYPE";
        public static final String BUNDLE_PRODUCER_TYPE = "BUNDLE_PRODUCER_TYPE";
        public static final String BUNDLE_PRODUCER_DIALOG_TYPE = "BUNDLE_PRODUCER_DIALOG_TYPE";
    }

    public static final class RequestConstant {
        public static final int REQUEST_SELECTION = 1;
        public static final int REQUEST_STATUS = 2;
        public static final int REQUEST_CATEGORY = 3;
        public static final int REQUEST_RELATIVE = 4;
        public static final int REQUEST_ASSIGNER = 5;
        public static final int REQUEST_DETAIL = 6;
        public static final int REQUEST_SCANNER = 7;
        public static final int REQUEST_CREATE_REQUEST = 8;
        public static final int REQUEST_CREATE_ASSIGNMENT = 9;
        public static final int REQUEST_BRANCH = 10;
        public static final int REQUEST_VENDOR = 11;
        public static final int REQUEST_MAKER = 12;
        public static final int REQUEST_DEVICE_GROUPS = 13;
        public static final int REQUEST_CATEGORIES = 14;
        public static final int REQUEST_DEVICE = 15;
        public static final int REQUEST_MEETING_ROOM = 16;
        public static final int REQUEST_DEVICE_USING_STATUS = 17;
        public static final int REQUEST_REQUEST_CREATED_BY = 18;
        public static final int REQUEST_ASSIGNEE = 19;
        public static final int REQUEST_USER_BORROW = 20;
        public static final int REQUEST_DEVICE_GROUPS_DIALOG = 21;
        public static final int REQUEST_EDIT = 22;
        public static final int REQUEST_MANAGE_DEVICE = 23;
        public static final int REQUEST_CREATE_DEVICE = 24;
        public static final int REQUEST_ASSIGN_DEVICE_FOR_MEETING_ROOM = 25;
    }

    public static final class DeviceUsingStatus {
        public static final String ALL = "all";
        public static final String USING = "using";
        public static final String RETURN = "returned";
    }

    public class ApiParram {
        public static final String CATEGORY_ID = "device[category_id]";
        public static final String STATUS_ID = "device[status_id]";
        public static final String VENDOR_ID = "device[vendor_id]";
        public static final String MAKER_ID = "device[maker_id]";
        public static final String WARRANTY = "device[warranty]";
        public static final String RAM = "device[ram]";
        public static final String HARD_DRIVE = "device[hard_driver]";
        public static final String DESCRIPTION = "device[description]";
        public static final String INVOICE_NUMBER = "device[invoice_number]";
        public static final String IS_BAR_CODE = "device[is_barcode]";
        public static final String IS_MEETING_ROOM = "device[is_meeting_room]";
        public static final String MEETING_ROOM_ID = "device[meeting_room_id]";
        public static final String DEVICE_BRANCH_ID = "device[branch]";
        public static final String PAGE = "page";
        public static final String REQUEST_TYPE = "manager_request";
        public static final String PER_PAGE = "per_page";
        public static final String PRODUCTION_NAME = "device[production_name]";
        public static final String DEVICE_STATUS_ID = "device[device_status_id]";
        public static final String DEVICE_CATEGORY_ID = "device[device_category_id]";
        public static final String BOUGHT_DATE = "device[bought_date]";
        public static final String ORIGINAL_PRICE = "device[original_price]";
        public static final String SERIAL_NUMBER = "device[serial_number]";
        public static final String MODEL_NUMBER = "device[model_number]";
        public static final String DEVICE_CODE = "device[device_code]";
        public static final String PICTURE = "device[picture]";
        public static final String REQUEST_STATUS_ID = "request_status_id";
        public static final String RELATIVE_ID = "relative_id";
        public static final String DEVICE_NAME = "device[production_name]";
        public static final String STAFF_USING_NAME = "device[using]";
        public static final String ROOM_NAME = "name";
        public static final String REQUEST_TITLE = "request[title]";
        public static final String REQUEST_DESCRIPTION = "request[description]";
        public static final String REQUEST_FOR_USER_ID = "request[for_user_id]";
        public static final String REQUEST_ASSIGNEE_ID = "request[assignee_id]";
        public static final String REQUEST_STATUS_ID_EDIT = "request[request_status_id]";
        public static final String REQUEST_REQUEST_DETAILS = "request[request_details_attributes]";
        public static final String ASSIGNMENT_REQUEST_ID = "assignment[request_id]";
        public static final String ASSIGNMENT_ASSIGNEE_ID = "assignment[assignee_id]";
        public static final String ASSIGNMENT_DESCRIPTION = "assignment[description]";
        public static final String ASSIGNMENT_STAFF_ID = "staff_id";
        public static final String ASSIGNMENT_ASSIGNMENT_DETAILS =
            "assignment[assignment_details_attributes]";
        public static final String ASSIGNMENT_ASSIGNMENT_DEVICE_ID = "device_ids[]";
        public static final String DEVICE_GROUP_ID = "device_group_id";
        public static final String USER_NAME = "username";
        public static final String NAME = "name";
        public static final String DEVICE_CATEGORY_NAME = "device_category_name";
        public static final String DEVICE_CATEGORY_NAME_MANAGER = "device_category[name]";
        public static final String DEVICE_CATEGORY_DESCRIPTION_MANAGER =
            "device_category[description]";
        public static final String DEVICE_CATEGORY_GROUP_ID_MANAGER =
            "device_category[device_group_id]";
        public static final String STATUS = "status";
        public static final String TEXT_USER_SEARCH = "text_user_search";
        public static final String TEXT_DEVICE_SEARCH  = "text_device_search";
        public static final String BRANCH_ID = "branch_id";
    }

    public class BundleRequest {
        public static final String BUND_REQUEST = "BUND_REQUEST";
        public static final String BUNDLE_REQUEST_TYPE = "BUNDLE_REQUEST_TYPE";
    }

    public class LocaleLanguage {
        public static final String ENGLISH = "en";
        public static final String VIETNAMESE = "vi";
        public static final String JAPANESE = "ja";
        public static final int ENGLISH_POSITION = 0;
        public static final int VIETNAMESE_POSITION = 1;
        public static final int JAPANESE_POSITION = 2;
    }
}
