package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.AdminService;
import com.devserbyn.skemex.service.MigrationConflictService;
import com.devserbyn.skemex.service.NotificationService;
import com.devserbyn.skemex.service.RoomConflictAttributesService;
import com.devserbyn.skemex.service.WorkspaceConflictAttributesService;
import com.devserbyn.skemex.entity.MigrationConflict;
import com.devserbyn.skemex.entity.Notification;
import com.devserbyn.skemex.entity.RoomConflictAttributes;
import com.devserbyn.skemex.entity.WorkspaceConflictAttributes;
import com.devserbyn.skemex.migration.ComparatorData;
import com.devserbyn.skemex.migration.ExcelParser;
import com.devserbyn.skemex.migration.entities.ExcelOffice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final NotificationService notificationService;
    private final ComparatorData comparatorData;
    private final MigrationConflictService migrationConflictService;
    private final RoomConflictAttributesService roomAttrService;
    private final WorkspaceConflictAttributesService wsAttrService;

    @Autowired
    public AdminServiceImpl(NotificationService notificationService,
                            ComparatorData comparatorData,
                            MigrationConflictService migrationConflictService,
                            RoomConflictAttributesService roomAttrService,
                            WorkspaceConflictAttributesService wsAttrService
    ) {
        this.notificationService = notificationService;
        this.comparatorData = comparatorData;
        this.migrationConflictService = migrationConflictService;
        this.roomAttrService = roomAttrService;
        this.wsAttrService = wsAttrService;
    }

    @Override
    public boolean handleExcelFile(MultipartFile file) throws IOException {
        log.debug("Start AdminService.handleExcelFile()");

        ExcelParser excelParser = new ExcelParser();
        List<ExcelOffice> offices = excelParser.parseExcel(file.getInputStream());

        List<Notification> notifications = new ArrayList<>();
        List<MigrationConflict> conflicts = new ArrayList<>();

        comparatorData.compareData(offices, notifications, conflicts);

        saveNotifications(notifications);
        saveConflicts(conflicts);

        log.debug("End AdminService.handleExcelFile()");
        return true;
    }

    private void saveNotifications(List<Notification> notifications) {
        log.debug("Save notifications. Count: " + notifications.size());
        for (Notification notification: notifications) {
            notificationService.save(notification);
        }
    }

    private void saveConflicts(List<MigrationConflict> conflicts) {
        log.debug("Save conflicts. Count: " + conflicts.size());
        for (MigrationConflict conflict: conflicts) {

            RoomConflictAttributes roomAttr = conflict.getRoomAttributes();
            if (roomAttr != null) {
                if (roomAttrService.findByOfficeAndRoom(roomAttr.getOffice().getCity(), roomAttr.getTitle()) != null) {
                    // Assume that not resolved conflict with these room attributes exist in DB
                    continue;
                }
            }

            WorkspaceConflictAttributes wsAttr = conflict.getWorkspaceAttributes();
            if (wsAttr != null) {
                if (wsAttrService.findByWorkspaceOwnerStatus(
                        wsAttr.getNewOwner().getNickname(), wsAttr.getWorkspace().getNumber(), wsAttr.getNewStatus()) != null) {
                    // Assume that not resolved conflict with these workspace attributes exist in DB
                    continue;
                }
            }

            // Create conflict
            migrationConflictService.save(conflict);

            if (conflict.getRoomAttributes() != null) {
                conflict.getRoomAttributes().setConflict(conflict);
                roomAttrService.save(conflict.getRoomAttributes());
            }
            if (conflict.getWorkspaceAttributes() != null) {
                conflict.getWorkspaceAttributes().setConflict(conflict);
                wsAttrService.save(conflict.getWorkspaceAttributes());
            }
        }
    }
}
