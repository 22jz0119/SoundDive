package model;

import java.time.LocalDate;

public class Live_artist {
	private int id;
	private int user_id;
	private int livehouse_application_id;
	private LocalDate create_date;
	private LocalDate update_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getLivehouse_application_id() {
		return livehouse_application_id;
	}
	public void setLivehouse_application_id(int livehouse_application_id) {
		this.livehouse_application_id = livehouse_application_id;
	}
	public LocalDate getCreate_date() {
		return create_date;
	}
	public void setCreate_date(LocalDate create_date) {
		this.create_date = create_date;
	}
	public LocalDate getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(LocalDate update_date) {
		this.update_date = update_date;
	}
	public Live_artist(int id, int user_id, int livehouse_application_id, LocalDate create_date,
			LocalDate update_date) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.livehouse_application_id = livehouse_application_id;
		this.create_date = create_date;
		this.update_date = update_date;
	}

	
}
