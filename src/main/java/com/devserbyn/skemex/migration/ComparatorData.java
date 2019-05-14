package com.devserbyn.skemex.migration;

import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.MigrationConflict;
import com.devserbyn.skemex.entity.Notification;
import com.devserbyn.skemex.entity.Organization;
import com.devserbyn.skemex.entity.Reservation;
import com.devserbyn.skemex.entity.Role;
import com.devserbyn.skemex.entity.RoleName;
import com.devserbyn.skemex.entity.Room;
import com.devserbyn.skemex.entity.RoomConflictAttributes;
import com.devserbyn.skemex.entity.User;
import com.devserbyn.skemex.entity.Workspace;
import com.devserbyn.skemex.entity.WorkspaceConflictAttributes;
import com.devserbyn.skemex.entity.WorkspaceStatus;
import com.devserbyn.skemex.migration.entities.ExcelEmployee;
import com.devserbyn.skemex.migration.entities.ExcelOffice;
import com.devserbyn.skemex.migration.entities.ExcelOrganization;
import com.devserbyn.skemex.migration.entities.ExcelRoom;
import com.devserbyn.skemex.migration.entities.ExcelWorkspace;
import com.devserbyn.skemex.service.EmployeeService;
import com.devserbyn.skemex.service.OfficeService;
import com.devserbyn.skemex.service.OrganizationService;
import com.devserbyn.skemex.service.ReservationService;
import com.devserbyn.skemex.service.RoomService;
import com.devserbyn.skemex.service.UserService;
import com.devserbyn.skemex.dao.OfficeDAO;
import com.devserbyn.skemex.dao.RoleDAO;
import com.devserbyn.skemex.dao.WorkspaceDAO;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.utility.SecurityUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@Transactional
public class ComparatorData {

    private final OfficeService officeService;
    private final RoomService roomService;
    private final OrganizationService organizationService;
    private final EmployeeService employeeService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final RoleDAO roleDAO;
    private final OfficeDAO officeDAO;
    private final WorkspaceDAO workspaceDAO;

    private List<Notification> notifications;
    private List<MigrationConflict> conflicts;
    private Employee admin;

    @Autowired
    public ComparatorData(OfficeService officeService,
                          RoomService roomService,
                          OrganizationService organizationService,
                          EmployeeService employeeService,
                          ReservationService reservationService,
                          UserService userService,
                          RoleDAO roleDAO,
                          OfficeDAO officeDAO,
                          WorkspaceDAO workspaceDAO) {
        this.officeService = officeService;
        this.roomService = roomService;
        this.organizationService = organizationService;
        this.employeeService = employeeService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.roleDAO = roleDAO;
        this.officeDAO = officeDAO;
        this.workspaceDAO = workspaceDAO;
    }

    public void compareData(List<ExcelOffice> offices,
                              List<Notification> notifications,
                              List<MigrationConflict> conflicts) {
        this.notifications = notifications;
        this.conflicts = conflicts;

        String adminUsername = SecurityUtility.getUsername();
        log.debug("Admin username: " + adminUsername);

        admin = employeeService.findEntityById(adminUsername);

        if (CollectionUtils.isEmpty(offices)) {
            this.notifications.add(Notification.fatal(
                    admin, "Failed to retrieve data from excel file")
            );
        }

        for (ExcelOffice office : offices) {
            processOffice(office.getCity(), office.getRooms());
        }

        if (CollectionUtils.isEmpty(this.notifications) && CollectionUtils.isEmpty(this.conflicts)) {
            this.notifications.add(Notification.info(
                    admin,"Update data without conflicts")
            );
        }
    }

    private void processOffice(String city, List<ExcelRoom> rooms) {
        if (!officeService.existsByCity(city)) {
            notifications.add(Notification.error(
                    admin, String.format("Office %s not found in DB", city)
            ));
            return;
        }

        for (ExcelRoom room : rooms) {
            processRoom(city, room);
        }
    }

    private void processRoom(String city, ExcelRoom room) {
        if (!roomService.existsByTitleAndCity(room.getTitle(), city)) {
            conflicts.add(MigrationConflict.byRoom(
                    admin, String.format("Room %s not found in DB",
                            room.getTitle()),
                    new RoomConflictAttributes(room.getTitle(), officeDAO.getByCity(city)))
            );
            return;
        }

        Room dbRoom = roomService.findByTitleAndCity(room.getTitle(), city);
        for (ExcelWorkspace workspace : room.getWorkspaces()) {
            processWorkspace(city, room.getTitle(), workspace, dbRoom);
        }

        if (!dbRoom.getWorkspaces().isEmpty()) {
            for (Workspace workspace : dbRoom.getWorkspaces()) {
                notifications.add(Notification.error(
                        admin, String.format("Workspace %s in room %s not found in excel",
                                workspace.getNumber(), room.getTitle())
                ));
            }
        }
    }

    private void processWorkspace(String city, String title, ExcelWorkspace workspace, Room room) {
        Iterator<Workspace> iterator = room.getWorkspaces().iterator();
        while (iterator.hasNext()) {
            Workspace currentWorkspace = iterator.next();
            if (!Objects.equals(workspace.getNumber(), currentWorkspace.getNumber())) {
                continue;
            }
            iterator.remove();
            final ExcelEmployee excelEmployee = workspace.getEmployee();
            final Employee currentEmployee = currentWorkspace.getEmployee();
            if (excelEmployee == null && currentEmployee == null) {
                return;
            }

            if (excelEmployee == null || currentEmployee == null) {
                if (excelEmployee == null) {
                    conflicts.add(MigrationConflict.byWorkspace(
                            admin, String.format("Workspace %s in room %s busy in DB by %s %s but free in excel",
                                    currentWorkspace.getNumber(), title, currentEmployee.getFirstName(),
                                    currentEmployee.getLastName()),
                            new WorkspaceConflictAttributes(currentWorkspace, currentEmployee, WorkspaceStatus.FREE)
                    ));
                } else {
                    Employee employee;
                    if (!employeeService.existsById(excelEmployee.getFirstName() + excelEmployee.getLastName())) {
                        employee = createEmployeeFromExcel(excelEmployee);
                    } else {
                        employee = employeeService.findEntityById(excelEmployee.getFirstName() + excelEmployee.getLastName());
                    }
                    List<Reservation> reservations = reservationService.findByWorkspaceId(currentWorkspace.getId());
                    if (reservations.isEmpty()) {
                        conflicts.add(MigrationConflict.byWorkspace(
                                admin, String.format("Workspace %s in room %s busy in Excel by %s %s but free in DB",
                                        currentWorkspace.getNumber(), title, excelEmployee.getFirstName(),
                                        excelEmployee.getLastName()),
                                new WorkspaceConflictAttributes(currentWorkspace, employee, WorkspaceStatus.BUSY)
                        ));
                    } else {
                        conflicts.add(MigrationConflict.byWorkspace(
                                admin, String.format("Workspace %s in room %s is reserved in DB but in Excel is busy by %s %s",
                                        currentWorkspace.getNumber(), title,
                                        excelEmployee.getFirstName(), excelEmployee.getLastName()),
                                new WorkspaceConflictAttributes(currentWorkspace, employee, WorkspaceStatus.BUSY)
                        ));
                    }
                }
                return;
            }

            if (!excelEmployee.matchNames(currentEmployee))
            {
                Employee employee;
                if (!employeeService.existsById(excelEmployee.getFirstName() + excelEmployee.getLastName())) {
                    employee = createEmployeeFromExcel(excelEmployee);
                } else {
                    employee = employeeService.findEntityById(excelEmployee.getFirstName() + excelEmployee.getLastName());
                }
                conflicts.add(MigrationConflict.byWorkspace(
                        admin, String.format("Workspace %s %s employees' names mismatch! In DB busy by %s %s, in Excel busy by %s %s",
                                currentWorkspace.getNumber(), title, currentEmployee.getFirstName(),
                                currentEmployee.getLastName(), excelEmployee.getFirstName(),
                                excelEmployee.getLastName()),
                        new WorkspaceConflictAttributes(currentWorkspace, employee, WorkspaceStatus.BUSY)
                ));
                return;
            }

            if (Objects.equals(excelEmployee.getOrganization().getName(),
                    currentEmployee.getOrganization().getName())) {
                return;
            }

            notifications.add(Notification.error(
                    admin, String.format("Employee %s %s changed department %s -> %s",
                            currentEmployee.getFirstName(), currentEmployee.getLastName(),
                            currentEmployee.getOrganization().getName(),
                            excelEmployee.getOrganization().getName())
            ));
        }
        createWorkspaceFromExcel(workspace, room);
    }

    private Employee createEmployeeFromExcel(ExcelEmployee excelEmployee) {
        Employee employee = new Employee();
        employee.setFirstName(excelEmployee.getFirstName());
        employee.setLastName(excelEmployee.getLastName());
        employee.setNickname(StringUtils.replace(employee.getFirstName() + employee.getLastName(),
                " ",
                ""));
        employee.setOrganization(createOrganizationFromExcel(excelEmployee.getOrganization()));
        if (employeeService.existsById(employee.getNickname())) {
            return employeeService.findEntityById(employee.getNickname());
        }
        notifications.add(Notification.warning(
                admin, String.format("Employee %s %s was not found in DB but was created",
                        employee.getFirstName(), employee.getLastName())
        ));
        User user = new User();
        user.setNickname(employee.getNickname());
        user.setPassword(new BCryptPasswordEncoder().encode(employee.getNickname()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleDAO.findByName(RoleName.ROLE_EMPLOYEE).orElseThrow(() -> new NotFoundException(String.format("Role with name: %s not found",
                RoleName.ROLE_EMPLOYEE.toString()))));
        user.setRoles(roles);
        userService.save(user);
        return employeeService.save(employee);
    }

    private Workspace createWorkspaceFromExcel(ExcelWorkspace excelWorkspace, Room room) {

        Workspace workspace = new Workspace();
        workspace.setStatus(WorkspaceStatus.FREE);
        workspace.setNumber(excelWorkspace.getNumber());
        workspace.setRoom(room);

        notifications.add(Notification.warning(
                admin, String.format("Workspace %s in room %s was not found in DB but was created",
                        workspace.getNumber(), room.getTitle())
        ));
        return workspaceDAO.save(workspace);
    }

    private Organization createOrganizationFromExcel(ExcelOrganization excelOrganization) {
        if (organizationService.existsByName(excelOrganization.getName())) {
            return organizationService.findByName(excelOrganization.getName());
        }
        Organization organization = new Organization();
        organization.setName(excelOrganization.getName());
        notifications.add(Notification.warning(
                admin, String.format("Department %s not found in DB but was created",
                        excelOrganization.getName())
        ));
        return organizationService.save(organization);
    }
}
