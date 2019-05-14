package com.devserbyn.skemex.service;

import com.devserbyn.skemex.entity.ErrorLog;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ErrorLogService {

    ErrorLog save(ErrorLog errorLog);

    void archiveOldLogs(Timestamp newLogsStartTime);

    List<ErrorLog> findAll();

    List<ErrorLog> findAllArchived();

    Optional<String> findLogLocationById(long id);

    void deleteById(long id);

    Number findErrorsSize(boolean archived);
}
