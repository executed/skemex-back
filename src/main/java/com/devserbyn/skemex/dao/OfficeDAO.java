package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.Office;

public interface OfficeDAO extends IBaseDAO<Office, Long> {
    boolean existsByCity(String city);
    Office getByCity(String city);
}
