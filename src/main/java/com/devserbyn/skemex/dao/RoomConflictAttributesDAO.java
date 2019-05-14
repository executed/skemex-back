package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.RoomConflictAttributes;

public interface RoomConflictAttributesDAO extends IBaseDAO<RoomConflictAttributes, Long> {

    RoomConflictAttributes findByOfficeAndRoom(String office, String roomTitle);
}
