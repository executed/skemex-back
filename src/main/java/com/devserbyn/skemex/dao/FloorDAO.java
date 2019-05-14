package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.Floor;

import java.util.List;

public interface FloorDAO extends IBaseDAO<Floor, Long> {
    List<Floor> findAllFloorsByOfficeId(Long id);
}
