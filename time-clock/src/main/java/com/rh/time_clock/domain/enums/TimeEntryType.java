package com.rh.time_clock.domain.enums;

public enum TimeEntryType {

    IN,
    OUT;

    public TimeEntryType reverse() {
        return this.equals(TimeEntryType.IN) ? OUT : IN;
    }

}
