package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.RoomConflictAttributesService;
import com.devserbyn.skemex.dao.RoomConflictAttributesDAO;
import com.devserbyn.skemex.entity.RoomConflictAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoomConflictAttributesServiceImpl implements RoomConflictAttributesService {

    private final RoomConflictAttributesDAO attributeDAO;

    @Autowired
    public RoomConflictAttributesServiceImpl(final RoomConflictAttributesDAO attributeDAO) {
        this.attributeDAO = attributeDAO;
    }

    @Override
    public RoomConflictAttributes save(RoomConflictAttributes attr) {
        return attributeDAO.save(attr);
    }

    @Override
    public RoomConflictAttributes findByOfficeAndRoom(String office, String roomTitle) {
        return attributeDAO.findByOfficeAndRoom(office, roomTitle);
    }
}
