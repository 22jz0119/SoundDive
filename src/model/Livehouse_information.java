package model;

import java.util.Date;

public class Livehouse_information {

    // フィールド定義
    private int id;
    private String owner_name;  // 修正されたフィールド名
    private String equipment_information;
    private String livehouse_explanation_information;
    private String livehouse_detailed_information;
    private String livehouse_name;
    private String live_address;
    private String live_tel_number;
    private Date createDate;  // java.util.Date
    private Date updateDate;  // java.util.Date

    // コンストラクタ
    public Livehouse_information(int id, String owner_name, String equipment_information,
            String livehouse_explanation_information, String livehouse_detailed_information, String livehouse_name,
            String live_address, String live_tel_number, Date createDate, Date updateDate) {
        this.id = id;
        this.owner_name = owner_name;
        this.equipment_information = equipment_information;
        this.livehouse_explanation_information = livehouse_explanation_information;
        this.livehouse_detailed_information = livehouse_detailed_information;
        this.livehouse_name = livehouse_name;
        this.live_address = live_address;
        this.live_tel_number = live_tel_number;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getEquipment_information() {
        return equipment_information;
    }

    public void setEquipment_information(String equipment_information) {
        this.equipment_information = equipment_information;
    }

    public String getLivehouse_explanation_information() {
        return livehouse_explanation_information;
    }

    public void setLivehouse_explanation_information(String livehouse_explanation_information) {
        this.livehouse_explanation_information = livehouse_explanation_information;
    }

    public String getLivehouse_detailed_information() {
        return livehouse_detailed_information;
    }

    public void setLivehouse_detailed_information(String livehouse_detailed_information) {
        this.livehouse_detailed_information = livehouse_detailed_information;
    }

    public String getLivehouse_name() {
        return livehouse_name;
    }

    public void setLivehouse_name(String livehouse_name) {
        this.livehouse_name = livehouse_name;
    }

    public String getLive_address() {
        return live_address;
    }

    public void setLive_address(String live_address) {
        this.live_address = live_address;
    }

    public String getLive_tel_number() {
        return live_tel_number;
    }

    public void setLive_tel_number(String live_tel_number) {
        this.live_tel_number = live_tel_number;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
