package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries ({
        @NamedQuery (name = ErrorLog.ARCHIVE_OLD_LOGS,
                     query = "UPDATE ErrorLog e  SET e.archived = true WHERE e.time < :startTime"),
        @NamedQuery (name = ErrorLog.FIND_ALL_RELEVANT_SORTED_BY_TIME,
                     query = "SELECT e FROM ErrorLog e WHERE e.archived = false ORDER BY e.time"),
        @NamedQuery (name = ErrorLog.FIND_LOG_LOCATION_BY_ID,
                     query = "SELECT e.location FROM ErrorLog e WHERE e.id = :id"),
        @NamedQuery (name = ErrorLog.FIND_ERROR_RELEVANT_LOG_SIZE,
                     query = "SELECT count(e) FROM ErrorLog e WHERE e.archived = false"),
        @NamedQuery (name = ErrorLog.FIND_ERROR_ARCHIVED_LOG_SIZE,
                query = "SELECT count(e) FROM ErrorLog e WHERE e.archived = true")
})
public class ErrorLog {

    public static final String ARCHIVE_OLD_LOGS = "ErrorLog.archiveOldLogs";
    public static final String FIND_ALL_RELEVANT_SORTED_BY_TIME = "ErrorLog.findAllRelTimeSorted";
    public static final String FIND_ALL_ARCHIVED_SORTED_BY_TIME = "ErrorLog.findAllArchTimeSorted";
    public static final String FIND_LOG_LOCATION_BY_ID = "ErrorLog.findLogLocationById";
    public static final String FIND_ERROR_RELEVANT_LOG_SIZE = "ErrorLog.findErrorRelLogSize";
    public static final String FIND_ERROR_ARCHIVED_LOG_SIZE = "ErrorLog.findErrorArchLogSize";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column
    private String name;
    @NotNull
    @Column
    private String appId;
    @NotNull
    @Column
    private String username;
    @Past
    @Column
    private Timestamp time;
    @NotNull
    @Column(columnDefinition = "TEXT", unique = true)
    private String fullId;
    @NotNull
    @Column(columnDefinition = "TEXT", unique = true)
    private String location;
    @Column
    private String url;
    @Column
    private int status;
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String message;
    @Column
    @JsonIgnore
    private boolean archived;
}
