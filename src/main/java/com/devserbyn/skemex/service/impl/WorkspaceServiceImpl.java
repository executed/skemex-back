package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.dao.EmployeeDAO;
import com.devserbyn.skemex.dao.OrganizationDAO;
import com.devserbyn.skemex.dao.ReservationDAO;
import com.devserbyn.skemex.dao.RoomDAO;
import com.devserbyn.skemex.dao.WorkspaceDAO;
import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.RequestStatus;
import com.devserbyn.skemex.entity.Reservation;
import com.devserbyn.skemex.entity.Room;
import com.devserbyn.skemex.entity.Workspace;
import com.devserbyn.skemex.entity.WorkspaceStatus;
import com.devserbyn.skemex.service.WorkspaceService;
import com.devserbyn.skemex.controller.dto.WorkspaceDTO;
import com.devserbyn.skemex.controller.search.WorkspaceFilter;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.mapping.WorkspaceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceDAO workspaceDAO;
    private final RoomDAO roomDAO;
    private final EmployeeDAO employeeDAO;
    private final OrganizationDAO organizationDAO;
    private final ReservationDAO reservationDAO;
    private final WorkspaceMapper mapper;


    @Autowired
    public WorkspaceServiceImpl(WorkspaceDAO workspaceDAO,
                                RoomDAO roomDAO,
                                EmployeeDAO employeeDAO,
                                OrganizationDAO organizationDAO,
                                ReservationDAO reservationDAO, WorkspaceMapper mapper) {
        this.workspaceDAO = workspaceDAO;
        this.roomDAO = roomDAO;
        this.employeeDAO = employeeDAO;
        this.organizationDAO = organizationDAO;
        this.reservationDAO = reservationDAO;
        this.mapper = mapper;
    }

    @Override
    public WorkspaceDTO save(WorkspaceDTO dto) {
        final Workspace workspace = new Workspace();
        workspace.setX(dto.getX());
        workspace.setY(dto.getY());
        workspace.setNumber(dto.getNumber());
        workspace.setStatus(dto.getStatus());
        workspace.setRoom(roomDAO.findById(dto.getRoomId()).orElseThrow(() -> new NotFoundException(String.format("Room with id: %d not found",
                dto.getRoomId()))));
        return mapper.toDTO(workspaceDAO.save(workspace));
    }

    @Override
    public WorkspaceDTO update(WorkspaceDTO dto) {
        final Workspace workspace = workspaceDAO.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Workspace with id: %d not found",
                        dto.getId())));
        Room room = roomDAO.findById(dto.getRoomId())
                .orElseThrow(() -> new NotFoundException(String.format("Room with id : %d not found",
                        dto.getRoomId())));
        Employee employee = null;
        if (dto.getNickname() != null) {
            employee = employeeDAO.findById(dto.getNickname())
                    .orElseThrow(() -> new NotFoundException(String.format("Employee with nickname: %s not found",
                            dto.getNickname())));
            if (dto.isCheckDelete()) {
                List<Workspace> workspaces = employee.getWorkspaces();
                for (Workspace employeeWorkspace : workspaces) {
                    employeeWorkspace.setStatus(WorkspaceStatus.FREE);
                    employeeWorkspace.setEmployee(null);
                }
            }
        }

        workspace.setNumber(dto.getNumber());
        workspace.setStatus(dto.getStatus());
        workspace.setX(dto.getX());
        workspace.setY(dto.getY());
        workspace.setRoom(room);
        workspace.setEmployee(employee);
        if (workspace.getStatus() == WorkspaceStatus.BUSY) {
            List<Reservation> reservations = reservationDAO.findByWorkspace(workspace.getId());
            reservations.forEach(e -> e.setRequestStatus(RequestStatus.DECLINED));
            reservations.forEach(reservationDAO::save);
        }

        return mapper.toDTO(workspaceDAO.save(workspace));
    }

    @Override
    public Optional<WorkspaceDTO> findById(Long id) {
        return workspaceDAO.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<WorkspaceDTO> findAllByIdRoom(Long id) {
        return mapper.toDTO(workspaceDAO.findAllByIdRoom(id));
    }

    @Override
    public boolean existsById(Long aLong) {
        return workspaceDAO.existsById(aLong);
    }

    @Override
    public void deleteById(Long id) {
        workspaceDAO.deleteById(id);
    }

    @Override
    public void delete(WorkspaceDTO dto) {
        workspaceDAO.delete(mapper.toEntity(dto));
    }

    @Override
    public List<WorkspaceDTO> search(WorkspaceFilter workspaceFilter) {
        if (workspaceFilter.isWithReserved()) {
            return mapper.toDTO(workspaceDAO.findAllByIdRoomAnsStatusFree(workspaceFilter.getRoomId()));
        } else if (workspaceFilter.getEndDate() == null) {
            return mapper.toDTO(workspaceDAO.findFreeAfterStartTime(workspaceFilter.getStartDate(), workspaceFilter.getRoomId()));
        } else {
            return mapper.toDTO(this.getOkWorkspacesByRoomId(workspaceFilter.getRoomId(), workspaceFilter));
        }
    }

    private List<Workspace> getOkWorkspacesByRoomId(long roomId, WorkspaceFilter workspaceFilter) {
        return workspaceDAO.findAllByIdRoomAnsStatusFree(roomId).stream()
                .filter(e -> !this.isWorkspaceDoesNotOk(e, workspaceFilter)).collect(Collectors.toList());
    }

    private boolean isWorkspaceDoesNotOk(Workspace workspace, WorkspaceFilter workspaceFilter) {
        return reservationDAO.findByWorkspace(workspace.getId()).stream()
                .anyMatch(e -> isReservationDoesNotOk(e, workspaceFilter));
    }

    private boolean isReservationDoesNotOk(Reservation reservation, WorkspaceFilter roomFilter) {
        return ((!roomFilter.getStartDate().isBefore(reservation.getStartTime()) && !roomFilter.getStartDate().isAfter(reservation.getEndTime())) ||
                (!roomFilter.getStartDate().isAfter(reservation.getStartTime()) && !roomFilter.getEndDate().isBefore(reservation.getEndTime())) ||
                (!roomFilter.getEndDate().isBefore(reservation.getStartTime()) && !roomFilter.getEndDate().isAfter(reservation.getEndTime())));
    }

    @Override
    public List<WorkspaceDTO> saveAll(List<WorkspaceDTO> workspaces, Long roomId) {
        List<WorkspaceDTO> allByIdRoom = findAllByIdRoom(roomId);
        allByIdRoom.stream().filter(e -> workspaces.stream().noneMatch(e1 -> e.getId().equals(e1.getId())))
                .filter(e2 -> reservationDAO.findByWorkspace(e2.getId()).isEmpty()).forEach(e2 -> workspaceDAO.deleteById(e2.getId()));
        workspaces.stream().filter(e -> e.getId() == null).forEach(this::save);
        workspaces.stream().filter(e -> e.getId() != null).forEach(this::update);
        return mapper.toDTO(workspaceDAO.findAllByIdRoom(roomId));
    }
}
