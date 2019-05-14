package com.devserbyn.skemex.constants;

public class REGEX_CONSTANTS {

    public static final String ROOM_TITLE = "(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$";
    public static final String ROOM_TITLE_ERR_MSG = "Room title isn't correct";
}
