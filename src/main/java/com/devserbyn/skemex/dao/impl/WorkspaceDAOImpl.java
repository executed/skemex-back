package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.WorkspaceDAO;
import com.devserbyn.skemex.entity.Workspace;
import com.devserbyn.skemex.entity.WorkspaceStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

@Repository
public class WorkspaceDAOImpl extends AbstractDao<Workspace, Long> implements WorkspaceDAO {

    private static final String RESERVATIONS = "reservations";
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String ROOM = "room";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Workspace> findAllByIdRoom(Long id) {
        return createNamedQuery(Workspace.FIND_BY_ROOM_ID)
                .setParameter(ID, id)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Workspace> findAllByIdRoomAnsStatusFree(Long id) {
        return createNamedQuery(Workspace.FIND_BY_ROOM_ID_AND_STATUS_FREE)
                .setParameter(ID, id)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Workspace> findAllByStatus(WorkspaceStatus status) {
        return createNamedQuery(Workspace.FIND_BY_STATUS)
                .setParameter(STATUS, status)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Workspace> findFreeAfterStartTime(LocalDate startDate, long roomId) {
        return createNamedQuery(Workspace.FIND_ALL_OK_FOR_ROOM)
                .setParameter(ROOM, roomId)
                .setParameter(START_TIME, startDate)
                .getResultList();
    }
}

