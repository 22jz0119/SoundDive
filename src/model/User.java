package model;

import java.util.Date;

public class User {

    private Long id;
    private String name;
    private String password;
    private Long telNumber;
    private String address;
    private Date createDate;
    private Date updateDate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(Long telNumber) {
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

	public User(Long id, String name, String password, Long telNumber, String address, Date createDate,
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


