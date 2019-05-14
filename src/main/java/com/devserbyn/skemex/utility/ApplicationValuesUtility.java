package com.devserbyn.skemex.utility;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class ApplicationValuesUtility {

    public static LocalDateTime getJVMStartTime() {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(mxBean.getStartTime()),
                                       TimeZone.getDefault().toZoneId());
    }

    public static String getHeapMemUsageString() {
        MemoryUsage memUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        return String.format("Max: %s; Used: %s", convertByteToMegabyte(memUsage.getMax()),
                                                  convertByteToMegabyte(memUsage.getUsed()));
    }

    private static String convertByteToMegabyte(long size) {
        double m = ((size/1024.0) / 1024.0);
        DecimalFormat dec = new DecimalFormat("0.00");

        return (m > 1) ? dec.format(m).concat(" MB")
                       : dec.format((double) size).concat(" Bytes");
    }
}
