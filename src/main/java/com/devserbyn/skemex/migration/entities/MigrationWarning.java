package com.devserbyn.skemex.migration.entities;

import com.devserbyn.skemex.entity.MigrationConflict;

public class MigrationWarning {
    private String messageWarning;
    private MigrationWarningLevel level;
    private MigrationConflict attribute;

    public String getMessageWarning() {
        return messageWarning;
    }

    public void setMessageWarning(String messageWarning) {
        this.messageWarning = messageWarning;
    }

    public MigrationWarningLevel getLevel() {
        return level;
    }

    public MigrationConflict getAttribute() {
        return attribute;
    }

    private MigrationWarning(MigrationWarningLevel level, String messageWarning, MigrationConflict attribute){
        this.level = level;
        this.messageWarning = messageWarning;
        this.attribute = attribute;
    }
    public static MigrationWarning error(String messageWarning){
        return new MigrationWarning(MigrationWarningLevel.ERROR, messageWarning, null);
    }
    public static MigrationWarning warning(String messageWarning){
        return new MigrationWarning(MigrationWarningLevel.WARNING, messageWarning, null);
    }
    public static MigrationWarning conflict(String messageWarning, MigrationConflict attribute){
        return new MigrationWarning(MigrationWarningLevel.CONFLICT, messageWarning, attribute);
    }
    public static MigrationWarning fatal(String messageWarning){
        return new MigrationWarning(MigrationWarningLevel.FATAL, messageWarning, null);
    }
}
