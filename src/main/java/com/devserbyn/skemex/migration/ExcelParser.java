package com.devserbyn.skemex.migration;

import com.devserbyn.skemex.migration.entities.ExcelEmployee;
import com.devserbyn.skemex.migration.entities.ExcelOffice;
import com.devserbyn.skemex.migration.entities.ExcelOrganization;
import com.devserbyn.skemex.migration.entities.ExcelRoom;
import com.devserbyn.skemex.migration.entities.ExcelWorkspace;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser {

    private static final int OFFICE = 1;
    private static final int ROOM = 2;
    private static final int WORKSPACE_NUM = 4;
    private static final int ORGANIZATION = 14;
    private static final int EMPLOYEE = 15;

    private static final String CHERNIVTSI = "Chernivtsi";

    private ExcelOffice currentOffice;
    private ExcelRoom currentRoom;
    private List<ExcelOffice> records;

    public List<ExcelOffice> parseExcel(InputStream inputStream) throws IOException {
        records = new ArrayList<>();

        Sheet sheet = WorkbookFactory.create(inputStream).getSheetAt(0);
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            parseRow(sheet.getRow(i));
        }

        return records;
    }

    private void parseRow(Row row) {

        // If office cell is not empty then parse office name
        // and save new office entity
        if (newOffice(row)) {
            if (row.getCell(OFFICE).toString().equalsIgnoreCase(CHERNIVTSI)) {
                final ExcelOffice office = new ExcelOffice(row.getCell(OFFICE).toString());

                records.add(office);
                currentOffice = office;
                return;
            } else {
                currentOffice = null;
            }
        }

        // Exit unless current room is in Chernivtsi office
        if (currentOffice == null) {
            return;
        }

        // If room cell is not empty then parse room title,
        // then find last added office and add current room to office
        if (newRoom(row)) {
            final ExcelRoom room = new ExcelRoom(row.getCell(ROOM).toString());

            currentOffice.addRoom(room);
            currentRoom = room;
            return;
        }

        ExcelOrganization organization = ExcelOrganization.builder()
                .name(row.getCell(ORGANIZATION).toString())
                .build();

        final String fullName = row.getCell(EMPLOYEE).toString();
        final String[] splitFullName = fullName.split("[ ]+");

        ExcelEmployee employee;

        if (splitFullName.length == 3) {
            employee = ExcelEmployee.builder()
                    .firstName(splitFullName[0])
                    .lastName(splitFullName[2])
                    .organization(organization)
                    .build();
        }
        else if ("None".equalsIgnoreCase(fullName)){
            employee = null;
        }
        else {
            employee = ExcelEmployee.builder()
                    .firstName(splitFullName[0])
                    .lastName(splitFullName[1])
                    .organization(organization)
                    .build();
        }

        ExcelWorkspace workspace = ExcelWorkspace.builder()
                .number(Integer.valueOf(row.getCell(WORKSPACE_NUM).toString()))
                .employee(employee)
                .build();

        currentRoom.getWorkspaces().add(workspace);
    }

    private boolean newRoom(Row row) {
        return row.getCell(ROOM) != null && !row.getCell(ROOM).toString().isEmpty();
    }

    //Checking if there is "+" symbol
    private boolean newOffice(Row row) {
        return row.getCell(OFFICE) != null && StringUtils.length(row.getCell(OFFICE).toString()) > 1;
    }
}
