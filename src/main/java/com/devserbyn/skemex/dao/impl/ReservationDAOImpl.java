package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.controller.search.ReservationSearch;
import com.devserbyn.skemex.service.schedule.filter.ReservationUpdateFilter;
import com.devserbyn.skemex.dao.ReservationDAO;
import com.devserbyn.skemex.entity.Reservation;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationDAOImpl extends AbstractDao<Reservation, Long> implements ReservationDAO {
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String APPROVER = "approver";
    private static final String NICKNAME = "nickname";
    private static final String EMPLOYEE = "employee";
    private static final String REQUESTER = "requester";
    private static final String WORKSPACE = "workspace";
    private static final String ID = "id";
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @SuppressWarnings("unchecked")
    public List<Reservation> findByWorkspace(Long id) {
        return createNamedQuery(Reservation.FIND_APPLICABLE).setParameter(ID, id).getResultList();
    }

    @Override
    public List<Reservation> findByParameters(ReservationSearch reservationSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> criteriaQuery = criteriaBuilder.createQuery(Reservation.class);
        Root<Reservation> reservationRoot = criteriaQuery.from(Reservation.class);
        List<Predicate> predicates = new ArrayList<>();
        if (reservationSearch.getRequestStatus() != null) {
            predicates.add(
                    criteriaBuilder.equal(reservationRoot.get(REQUEST_STATUS), reservationSearch.getRequestStatus()));
        }
        if (reservationSearch.getStartTime() != null) {
            predicates.add(
                    criteriaBuilder.equal(reservationRoot.get(START_TIME), reservationSearch.getStartTime()));
        }
        if (reservationSearch.getEndTime() != null) {
            predicates.add(
                    criteriaBuilder.equal(reservationRoot.get(END_TIME), reservationSearch.getEndTime()));
        }
        if (reservationSearch.getApproverNickname() != null) {
            predicates.add(criteriaBuilder.equal(reservationRoot.get(APPROVER).get(NICKNAME), (reservationSearch.getApproverNickname())));
        }
        if (reservationSearch.getEmployeeNickname() != null) {
            predicates.add(criteriaBuilder.equal(reservationRoot.get(EMPLOYEE).get(NICKNAME), (reservationSearch.getEmployeeNickname())));
        }
        if (reservationSearch.getRequesterNickname() != null) {
            predicates.add(criteriaBuilder.equal(reservationRoot.get(REQUESTER).get(NICKNAME), (reservationSearch.getRequesterNickname())));
        }
        if(reservationSearch.getWorkspaceId() != null){
            predicates.add(criteriaBuilder.equal(reservationRoot.get(WORKSPACE).get(ID), (reservationSearch.getWorkspaceId())));
        }
        criteriaQuery.select(reservationRoot)
                .where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void updateReservations(ReservationUpdateFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Reservation> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Reservation.class);
        Root<Reservation> reservationRoot = criteriaUpdate.from(Reservation.class);
        List<Predicate> predicates = new ArrayList<>();
    }

}
