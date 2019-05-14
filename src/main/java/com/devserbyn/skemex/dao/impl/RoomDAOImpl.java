package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.RoomDAO;
import com.devserbyn.skemex.entity.Room;
import com.devserbyn.skemex.utility.DaoUtility;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomDAOImpl extends AbstractDao<Room, Long> implements RoomDAO {


    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String CITY = "city";
    private static final String WORKSPACES = "workspaces";
    private static final String FLOOR = "floor";
    private static final String RESERVATIONS = "reservations";
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String STATUS = "status";
    private static final String NUMBER = "number";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Room> findAllByFloor(Long id) {
        return (List<Room>) createNamedQuery(Room.FIND_BY_FLOOR_ID)
                .setParameter(ID, id)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Room> findAllByOffice(Long id) {
        return (List<Room>) createNamedQuery(Room.FIND_BY_OFFICE_ID)
                .setParameter(ID, id)
                .getResultList();
    }
    @Override
    public boolean existsByTitleAndCity(String title, String city) {
        return DaoUtility.findOrEmpty(() -> ((Room) createNamedQuery(Room.FIND_BY_TITLE_AND_CITY)
                .setParameter(TITLE, title)
                .setParameter(CITY, city)
                .getSingleResult())).isPresent();
    }

    @Override
    public Optional<Room> findByTitleAndCity(String title, String city) {
        return DaoUtility.findOrEmpty(() -> ((Room) createNamedQuery(Room.FIND_BY_TITLE_AND_CITY)
                .setParameter(TITLE, title)
                .setParameter(CITY, city)
                .getSingleResult()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Room> findFreeAfterStartTime(LocalDate startTime, Long count) {
        return (List<Room>) createNamedQuery(Room.FIND_BY_START_TIME)
                .setParameter(START_TIME, startTime)
                .setParameter("countOfPlaces", count)
                .getResultList();
    }
}
