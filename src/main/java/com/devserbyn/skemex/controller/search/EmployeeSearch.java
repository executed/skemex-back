package com.devserbyn.skemex.controller.search;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSearch {
    private String nickname;
    private String firstName;
    private String lastName;
    private String organizationName;
    private String email;
    private Long roomId;
    private Integer firstElement;
    private Integer maxElements;
    private Boolean active;
}
