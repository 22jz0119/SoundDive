package model;

import java.util.Date;

public class User {
    private int id; // NUMBER → int
    private String name; // VARCHAR2(255) → String
    private String password; // VARCHAR2(255) → String
    private long telNumber; // NUMBER → long (電話番号などの大きい値)
    private String address; // VARCHAR2(255) → String
    private Date createDate; // DATE → java.util.Date
    private Date updateDate; // DATE → java.util.Date

    // コンストラクタ
    public User() {
    }

    public User(int id, String name, String password, long telNumber, String address, Date createDate, Date updateDate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.telNumber = telNumber;
        this.address = address;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    // IDのゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // 名前のゲッターとセッター
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // パスワードのゲッターとセッター
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 電話番号のゲッターとセッター
    public long getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(long telNumber) {
        this.telNumber = telNumber;
    }

    // 住所のゲッターとセッター
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // 作成日のゲッターとセッター
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    // 更新日のゲッターとセッター
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

