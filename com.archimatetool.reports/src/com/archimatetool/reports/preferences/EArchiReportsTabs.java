package com.archimatetool.reports.preferences;

import java.util.LinkedList;
import java.util.List;

public enum EArchiReportsTabs {
    Documentation(1),
    Properties(2),
    Elements(3),
    Views(3);

    int seq;
    private EArchiReportsTabs(int seq) {
        this.seq = seq;
    }

    public int getSequenceNumber() {
        return seq;
    }

    public static String[] getStringValues() {
        List<String> names = new LinkedList<>();
        for (EArchiReportsTabs a : values()) {
            names.add(a.toString());
        }
        return names.toArray(new String[0]);
    }
}
