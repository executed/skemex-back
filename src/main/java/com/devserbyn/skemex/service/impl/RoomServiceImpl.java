package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.entity.Floor;
import com.devserbyn.skemex.entity.Reservation;
import com.devserbyn.skemex.entity.Room;
import com.devserbyn.skemex.entity.Workspace;
import com.devserbyn.skemex.entity.WorkspaceStatus;
import com.devserbyn.skemex.mapping.RoomMapper;
import com.devserbyn.skemex.service.RoomService;
import com.devserbyn.skemex.controller.dto.RoomDTO;
import com.devserbyn.skemex.controller.search.RoomFilter;
import com.devserbyn.skemex.dao.FloorDAO;
import com.devserbyn.skemex.dao.ReservationDAO;
import com.devserbyn.skemex.dao.RoomDAO;
import com.devserbyn.skemex.dao.WorkspaceDAO;
import com.devserbyn.skemex.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomDAO roomDAO;
    private final FloorDAO floorDAO;
    private final WorkspaceDAO workspaceDAO;
    private final ReservationDAO reservationDAO;

    private final RoomMapper mapper;

    @Autowired
    public RoomServiceImpl(RoomDAO roomDAO, FloorDAO floorDAO, WorkspaceDAO workspaceDAO, ReservationDAO reservationDAO, RoomMapper mapper) {
        this.roomDAO = roomDAO;
        this.floorDAO = floorDAO;
        this.workspaceDAO = workspaceDAO;
        this.reservationDAO = reservationDAO;
        this.mapper = mapper;
    }

    @Override
    public RoomDTO save(RoomDTO dto) {
        final Room roomToSave = mapper.toEntity(dto);
        Floor floor = floorDAO.findById(dto.getFloorId())
                .orElseThrow(() -> new NotFoundException(String.format("Floor with id: %d not found",
                        dto.getFloorId())));
        roomToSave.setFloor(floor);
        return mapper.toDTO(roomDAO.save(roomToSave));
    }

    @Override
    public RoomDTO update(RoomDTO dto) {
        final Room room = roomDAO.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Room with id: %d not found",
                        dto.getId())));
        room.setTitle(dto.getTitle());
        return mapper.toDTO(roomDAO.save(room));
    }

    @Override
    public Optional<RoomDTO> findById(Long id) {
        return roomDAO.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<RoomDTO> findAllByFloorId(Long id) {
        return mapper.toDTO(roomDAO.findAllByFloor(id));
    }

    @Override
    public List<RoomDTO> findAllByOffice(Long id) {
        return mapper.toDTO(roomDAO.findAllByOffice(id));
    }

    @Override
    public List<RoomDTO> findAll() {
        return mapper.toDTO(roomDAO.findAll());
    }

    @Override
    public void deleteById(Long id) {
        roomDAO.deleteById(id);
    }

    @Override
    public boolean existsByTitleAndCity(String title, String city) {
        return roomDAO.existsByTitleAndCity(title, city);
    }

    @Override
    public Room findByTitleAndCity(String title, String city) {
        return roomDAO.findByTitleAndCity(title, city).orElseThrow(() -> new NotFoundException("Room with id: %s not found"));
    }

    @Override
    public List<RoomDTO> search(RoomFilter roomFilter) {
        if (roomFilter.isWithReserved()) {
            return mapper.toDTO(roomDAO.findAll().stream().filter(e->isRoomHaveMoreFreeSpacesThen(e,roomFilter)).collect(Collectors.toList()));
        } else if (roomFilter.getEndDate() == null) {
            return mapper.toDTO(roomDAO.findFreeAfterStartTime(roomFilter.getStartDate(),roomFilter.getNumberOfCount().longValue()));
        } else {
            return mapper.toDTO(roomDAO.findAll().stream().filter(e -> this.isRoomOk(e, roomFilter)).collect(Collectors.toList()));
        }
    }

    private boolean isRoomHaveMoreFreeSpacesThen(Room room, RoomFilter roomFilter) {
        return workspaceDAO.findAllByIdRoom(room.getId()).stream().filter(e->e.getStatus() == WorkspaceStatus.FREE).count() >= roomFilter.getNumberOfCount();
    }

    private boolean isRoomOk(Room room, RoomFilter roomFilter) {
        return workspaceDAO.findAllByIdRoom(room.getId()).stream()
                .filter(e -> e.getStatus() == WorkspaceStatus.FREE)
                .filter(e -> !this.isWorkspaceOk(e, roomFilter))
                .count() >= roomFilter.getNumberOfCount();
    }

    private boolean isWorkspaceOk(Workspace workspace, RoomFilter roomFilter) {
        return reservationDAO.findByWorkspace(workspace.getId()).stream()
                .anyMatch(e -> isReservationDoesNotOk(e, roomFilter));
    }

    private boolean isReservationDoesNotOk(Reservation reservation, RoomFilter roomFilter) {
        return  ((!roomFilter.getStartDate().isBefore(reservation.getStartTime()) && !roomFilter.getStartDate().isAfter(reservation.getEndTime())) ||
                (!roomFilter.getStartDate().isAfter(reservation.getStartTime()) && !roomFilter.getEndDate().isBefore(reservation.getEndTime())) ||
                (!roomFilter.getEndDate().isBefore(reservation.getStartTime()) && !roomFilter.getEndDate().isAfter(reservation.getEndTime())));
    }
}
