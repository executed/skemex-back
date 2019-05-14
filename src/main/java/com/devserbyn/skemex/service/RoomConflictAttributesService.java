package com.devserbyn.skemex.service;

import com.devserbyn.skemex.entity.RoomConflictAttributes;

public interface RoomConflictAttributesService {

    RoomConflictAttributes save(RoomConflictAttributes attr);

    RoomConflictAttributes findByOfficeAndRoom(String office, String roomTitle);
}
