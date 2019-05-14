package com.devserbyn.skemex.constants;

/** Class, which contains basic response messages for client and related strings */
public final class MSG_CONSTANTS {

    //Email content identifiers
    public static final String EMAIL_MAIN_TITLE_ID = "${MAIN_TITLE}";
    public static final String EMAIL_CONTENT_ID = "${CONTENT}";

    // Email titles
    public static final String EMAIL_ERROR_REPORT_TITLE = "Application work report";

    public static final String EMAIL_RESERV_EXPIRED_REQUESTER_TITLE = "Reservation expired";

    public static final String EMAIL_RESERV_OPEN_TITLE = "New reservation";

    public static final String EMAIL_RESERV_DECLINED_TITLE = "Reservation declined";

    public static final String EMAIL_RESERV_APPR_REQUESTER_TITLE = "Reservation approved";

    public static final String EMAIL_RESERV_EXPIRED_APPROVER_TITLE = "Reservation expiration report";

    //Email messages
    public static final String WELCOME_WORDS = "<h3>Dear %s,</h3><br>";

    public static final String EMAIL_ERROR_REPORT_MSG =
            "<h3>" +
                    "Application start time: %s" +
                    "<br>Errors count: %d" +
                    "<br>Archived errors: %d" +
                    "<br>Heap memory usage: %s" +
                    "<br>Please visit corresponding error log page on %s to get more info." +
            "</h3>";

    public static final String EMAIL_RESERV_EXPIRED_REQUESTER =
            "<h3> Dear %s,<br>Your reservation in room %s on workspace %d expired</h3>";

    public static final String EMAIL_RESERV_DECLINED =
            "<h3> " +
                    "Dear %s,<br>Your reservation on room %s, workspace %d has been declined" +
            "</h3>";

    public static final String EMAIL_RESERV_EXPIRED_APPROVER =
            "<h3> Reservation of requester %s in room %s on workspace %d expired</h3>";

    public static final String EMAIL_RESERV_APPR =
            "<h3> " +
                    "Your reservation on room %s, workspace %d has been approved<br>" +
                    "Reservation time period: from %s to %s" +
            "</h3>";

    public static final String EMAIL_RESERV_OPEN =
            "<h3> " +
                    "Dear %s,<br><br>" +
                    "New reservation on room %s, workspace %d<br>" +
                    "Requester nickname: %s<br>" +
                    "Reservation time period: from %s to %s" +
            "</h3>";
}
