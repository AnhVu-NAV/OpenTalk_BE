package sba301.java.opentalk.enums;

public enum CronKey {
    RANDOM("random"),
    SYNC("sync");

    private final String key;

    CronKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}