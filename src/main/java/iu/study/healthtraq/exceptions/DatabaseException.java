package iu.study.healthtraq.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatabaseException extends RuntimeException {
    private final String entityName;
    private final String criteriaLabel;
    private final String criteriaValue;

    public DatabaseException(String entityName, String criteriaLabel, long criteriaValue) {
        this.entityName = entityName;
        this.criteriaLabel = criteriaLabel;
        this.criteriaValue = String.valueOf(criteriaValue);
    }

    @Override
    public String toString() {
        return "Could not find " + entityName + " by " + criteriaLabel + " of value " + criteriaValue;
    }
}
