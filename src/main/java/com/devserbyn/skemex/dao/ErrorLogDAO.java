package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.ErrorLog;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ErrorLogDAO extends IBaseDAO<ErrorLog, Long> {

    void archiveOldLogs(Timestamp newLogsStartTime);

    Optional<String> findLogLocationById(long id);

    List<ErrorLog> findAllArchived();

    Number findErrorsSize(boolean archived);
}
