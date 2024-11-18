package model;

import java.time.LocalDate;

public class LivehouseApplicationWithGroup {
    private int applicationId;
    private LocalDate datetime;
    private boolean trueFalse;
    private LocalDate startTime;
    private LocalDate finishTime;
    private int groupId;
    private String accountName;
    private String groupGenre;
    private String bandYears;
    
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDate getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDate datetime) {
        this.datetime = datetime;
    }

    public boolean isTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(boolean trueFalse) {
        this.trueFalse = trueFalse;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDate finishTime) {
        this.finishTime = finishTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getGroupGenre() {
        return groupGenre;
    }

    public void setGroupGenre(String groupGenre) {
        this.groupGenre = groupGenre;
    }

    public String getBandYears() {
        return bandYears;
    }

    public void setBandYears(String bandYears) {
        this.bandYears = bandYears;
    }

    

    public LivehouseApplicationWithGroup(int applicationId, LocalDate datetime, boolean trueFalse, LocalDate startTime, LocalDate finishTime, int groupId, String accountName, String groupGenre, String bandYears) {
        this.applicationId = applicationId;
        this.datetime = datetime;
        this.trueFalse = trueFalse;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.groupId = groupId;
        this.accountName = accountName;
        this.groupGenre = groupGenre;
        this.bandYears = bandYears;
    }
}

