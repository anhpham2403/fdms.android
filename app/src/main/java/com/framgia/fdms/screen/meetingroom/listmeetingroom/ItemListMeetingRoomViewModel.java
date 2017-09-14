package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.databinding.BaseObservable;
import android.view.View;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.Producer;

/**
 * Created by ASUS on 08/09/2017.
 */

public class ItemListMeetingRoomViewModel extends BaseObservable {
    private final BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Producer>
        mItemClickListener;
    private Producer mMeetingRoom;

    public ItemListMeetingRoomViewModel(Producer meetingRoom,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Producer> itemClickListener) {
        mMeetingRoom = meetingRoom;
        mItemClickListener = itemClickListener;
    }

    public void onItemClicked(View view) {
        if (mItemClickListener == null) {
            return;
        }
        mItemClickListener.onItemRecyclerViewClick(mMeetingRoom);
    }

    public void onEditMeetingRoom() {
        // TODO: Open Edit form
    }

    public void onDeleteMeetingRoom() {
        // TODO: Delete room
    }

    public String getRoomName() {
        if (mMeetingRoom != null) {
            return mMeetingRoom.getName();
        }
        return "";
    }

    public String getRoomDescription() {
        if (mMeetingRoom != null) {
            return mMeetingRoom.getDescription();
        }
        return "";
    }
}
