package com.devserbyn.skemex.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@Builder
public class EmailMessage {

    private String target;

    private String title;

    private String content;

    private File attachment;

    @Override public String toString() {
        return "EmailMessage{" +
                "target='" + target + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ((attachment != null) ? ", attachment='true'" : "") +
                '}';
    }
}
