package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.controller.search.EmployeeSearch;
import com.devserbyn.skemex.dao.EmployeeDAO;
import com.devserbyn.skemex.entity.Employee;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeDAOImpl extends AbstractDao<Employee, String> implements EmployeeDAO {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String WORKSPACES = "workspaces";
    private static final String ROOM = "room";
    private static final String ID = "id";
    private static final String ORGANIZATION = "organization";
    private static final String NAME = "name";
    private static final String ACTIVE = "active";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Employee> findByOrganizationId(Long id) {
        return (List<Employee>) createNamedQuery(Employee.FIND_BY_ORGANIZATIONS)
                .setParameter(ID, id)
                .getResultList();
    }

    @Override
    public List<Employee> search(EmployeeSearch employeeSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();
        if (employeeSearch.getFirstName() != null) {
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get(FIRST_NAME), "%" + employeeSearch.getFirstName() + "%"));
        }
        if (employeeSearch.getLastName() != null) {
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get(LAST_NAME), "%" + employeeSearch.getLastName() + "%"));
        }
        if (employeeSearch.getEmail() != null) {
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get(EMAIL), "%" + employeeSearch.getEmail() + "%"));
        }
        if (employeeSearch.getNickname() != null) {
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get(NICKNAME), "%" + employeeSearch.getNickname() + "%"));
        }
        if (employeeSearch.getRoomId() != null) {
            final Join workspace = employeeRoot.join(WORKSPACES);
            Join room = workspace.join(ROOM);
            predicates.add(criteriaBuilder.equal(room.get(ID), employeeSearch.getRoomId()));
        }
        if (employeeSearch.getOrganizationName() != null) {
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get(ORGANIZATION).get(NAME), "%" + employeeSearch.getOrganizationName() + "%"));
        }
        if (employeeSearch.getActive() != null) {
            predicates.add(
                    criteriaBuilder.equal(employeeRoot.get(ACTIVE), employeeSearch.getActive()));
        }
        criteriaQuery.select(employeeRoot)
                .where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).setFirstResult(employeeSearch.getFirstElement()).setMaxResults(employeeSearch.getMaxElements()).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Employee> findAllAdminsByOffice(long organizationId) {
        return (List<Employee>) createNamedQuery(Employee.FIND_ADMIN_BY_OFFICE)
                                .setParameter(ORGANIZATION, organizationId)
                                .getResultList();
    }
}
