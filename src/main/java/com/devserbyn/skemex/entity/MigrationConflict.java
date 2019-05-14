package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = MigrationConflict.ALL_NOT_RESOLVED,
                query = "select mc from MigrationConflict mc where mc.recipient.nickname = :nickname and mc.resolved = false"
        ),
        @NamedQuery(
                name = MigrationConflict.ALL_NOT_RESOLVED_COUNT,
                query = "select count(mc) from MigrationConflict mc where mc.recipient.nickname = :nickname and mc.resolved = false"
        )
})
@Table(name = "\"migrationconflict\"")
public class MigrationConflict {

    public static final String ALL_NOT_RESOLVED = "MigrationConflict.allNotResolved";
    public static final String ALL_NOT_RESOLVED_COUNT = "MigrationConflict.allNotResolvedCount";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    private Employee recipient;

    @Column
    private String message;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "conflict")
    private RoomConflictAttributes roomAttributes;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "conflict")
    private WorkspaceConflictAttributes workspaceAttributes;

    @Column
    private Boolean resolved = false;

    public static MigrationConflict byRoom(Employee recipient, String message, RoomConflictAttributes attributes) {
        return MigrationConflict.builder()
                .resolved(false)
                .recipient(recipient)
                .message(message)
                .roomAttributes(attributes)
                .build();
    }

    public static MigrationConflict byWorkspace(Employee recipient, String message, WorkspaceConflictAttributes attributes) {
        return MigrationConflict.builder()
                .resolved(false)
                .recipient(recipient)
                .message(message)
                .workspaceAttributes(attributes)
                .build();
    }
}
