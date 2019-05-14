package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.ErrorLogDAO;
import com.devserbyn.skemex.utility.DaoUtility;
import com.devserbyn.skemex.entity.ErrorLog;

import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ErrorLogDAOImpl extends AbstractDao<ErrorLog, Long> implements ErrorLogDAO {

    @Override
    public void archiveOldLogs(Timestamp newLogsStartTime) {
        createNamedQuery(ErrorLog.ARCHIVE_OLD_LOGS).setParameter("startTime", newLogsStartTime)
                                                        .executeUpdate();
    }

    @Override
    public Optional<String> findLogLocationById(long id) {
        return DaoUtility.findOrEmpty(() -> ((String) createNamedQuery(ErrorLog.FIND_LOG_LOCATION_BY_ID)
                    .setParameter("id", id)
                    .getSingleResult()));
    }

    @Override
    public Number findErrorsSize(boolean archived) {
        String query = (archived) ? ErrorLog.FIND_ERROR_ARCHIVED_LOG_SIZE
                                  : ErrorLog.FIND_ERROR_RELEVANT_LOG_SIZE;
        return (Number) createNamedQuery(query).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ErrorLog> findAll() {
        return (List<ErrorLog>) createNamedQuery(ErrorLog.FIND_ALL_RELEVANT_SORTED_BY_TIME)
                                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ErrorLog> findAllArchived() {
        return (List<ErrorLog>) createNamedQuery(ErrorLog.FIND_ALL_ARCHIVED_SORTED_BY_TIME)
                .getResultList();
    }

}
