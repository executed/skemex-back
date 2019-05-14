package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = WorkspaceConflictAttributes.FIND_BY_WORKSPACE_OWNER_STATUS,
                query = "select w from WorkspaceConflictAttributes w where w.workspace.number = :workspace and " +
                        "w.newOwner.nickname = :nickname and w.newStatus = :status and w.conflict.resolved = false"
        )
})
@Table(name = "\"workspaceconflictattributes\"")
public class WorkspaceConflictAttributes {

    public static final String FIND_BY_WORKSPACE_OWNER_STATUS = "WorkspaceConflictAttributes.findByWorkspaceOwnerStatus";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE)
    MigrationConflict conflict;

    @ManyToOne
    private Workspace workspace;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "\"new_owner_nickname\"")
    private Employee newOwner;

    @Column(name = "\"new_status\"")
    @Enumerated(EnumType.STRING)
    private WorkspaceStatus newStatus;

    public WorkspaceConflictAttributes(Workspace workspace, Employee newOwner, WorkspaceStatus newStatus) {
        this.workspace = workspace;
        this.newOwner = newOwner;
        this.newStatus = newStatus;
    }
}

