package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.dao.ErrorLogDAO;
import com.devserbyn.skemex.entity.ErrorLog;
import com.devserbyn.skemex.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ErrorLogServiceImpl implements ErrorLogService {

    private final ErrorLogDAO dao;

    @Autowired
    public ErrorLogServiceImpl(ErrorLogDAO dao) {
        this.dao = dao;
    }

    @Override
    public ErrorLog save(ErrorLog errorLog) {
        return dao.save(errorLog);
    }

    @Override
    @Modifying
    public void archiveOldLogs(Timestamp newLogsStartTime){
        dao.archiveOldLogs(newLogsStartTime);
    }

    @Override
    public List<ErrorLog> findAll() {
        return dao.findAll();
    }

    @Override
    public List<ErrorLog> findAllArchived() {
        return dao.findAllArchived();
    }

    @Override
    public Optional<String> findLogLocationById(long id) {
        return dao.findLogLocationById(id);
    }

    @Override
    public void deleteById(long id) {
        dao.deleteById(id);
    }

    @Override
    public Number findErrorsSize(boolean archived) {
        return dao.findErrorsSize(archived);
    }
}
