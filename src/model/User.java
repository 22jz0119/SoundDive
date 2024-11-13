package model;

import java.io.Serializable; // Serializableをインポート
import java.sql.Timestamp;

public class User implements Serializable {
    private static final long serialVersionUID = 1L; // シリアライズ用のバージョンID

    private int id; // 主キー: INT
    private String name; // VARCHAR(255) と仮定
    private String password; // VARCHAR(255) と仮定
    private String tel_number; // BIGINT
    private String address; // VARCHAR(255) と仮定
    private Timestamp createDate; // DATETIME
    private Timestamp updateDate; // DATETIME
    private String user_type; // ENUM('artist', 'livehouse') として設定

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
    
    

    public String getTel_number() {
		return tel_number;
	}

	public void setTel_number(String tel_number) {
		this.tel_number = tel_number;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public User(int id, String name, String password, String tel_number, String address,
			Timestamp createDate, Timestamp updateDate, String user_type) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.tel_number = tel_number;
		this.address = address;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.user_type = user_type;
	}
}
