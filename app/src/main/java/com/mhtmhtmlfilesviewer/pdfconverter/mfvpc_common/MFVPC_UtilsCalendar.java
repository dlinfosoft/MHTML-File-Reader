package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import java.util.Date;

public class MFVPC_UtilsCalendar {


    public static int getDifferenceInDays(Date date, Date date2) {
        return Math.abs(((int) (date.getTime() - date2.getTime())) / 86400000);
    }
}
