package sba301.java.opentalk.service;

import sba301.java.opentalk.enums.CronKey;

import java.util.Map;

public interface CronExpressionService {
    void saveCronExpression(CronKey cronKey, String expression);

    String getCronExpression(CronKey cronKey);

    Map<String, String> getAllCronExpressions();
}
