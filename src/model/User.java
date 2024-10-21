package model;
import java.sql.Date;

public class User {
    private int id; // 主キー: INT
    private String name; // VARCHAR(255) と仮定
    private String password; // VARCHAR(255) と仮定
    private String telNumber; // BIGINT
    private String address; // VARCHAR(255) と仮定
    private Date createDate; // DATETIME
    private Date updateDate; // DATETIME


    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

	public User(int id, String name, String password, String telNumber, String address, Date createDate,
			Date updateDate) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.telNumber = telNumber;
		this.address = address;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
    
    
}
