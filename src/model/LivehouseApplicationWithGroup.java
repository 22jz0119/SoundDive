package model;

import java.time.LocalDate;
import java.util.List;

public class LivehouseApplicationWithGroup {
    private int applicationId;
    private int id;
    private LocalDate date_Time;
    private boolean trueFalse;
    private LocalDate startTime;
    private LocalDate finishTime;
    private int groupId;
    private String accountName;
    private String groupGenre;
    private String bandYears;
    private int userId;
    private String us_name;
    private List<Member> members; // メンバーリストを追加
    

    // コンストラクタを追加
    public LivehouseApplicationWithGroup(int applicationId, int id, LocalDate date_Time, boolean trueFalse,LocalDate startTime, LocalDate finishTime, int groupId,String accountName, String groupGenre, String bandYears, int userId, String us_name,List<Member> members) {
        this.applicationId = applicationId;
        this.id = id;
        this.date_Time = date_Time;
        this.trueFalse = trueFalse;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.groupId = groupId;
        this.accountName = accountName;
        this.groupGenre = groupGenre;
        this.bandYears = bandYears;
        this.userId = userId;  // userIdを設定
        this.us_name = us_name;
        this.members = members; // メンバーリストを設定
    }
    
    

    // ゲッターとセッター
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate_Time() {
        return date_Time;
    }

    public void setDate_Time(LocalDate date_Time) {
        this.date_Time = date_Time;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUs_name() {
        return us_name;
    }

    public void setUs_name(String us_name) {
        this.us_name = us_name;
    }
    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
    
}
