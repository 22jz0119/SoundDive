package model;

import java.time.LocalDate;

public class Livehouse_application_table {
	private int id;
	private int livehouse_information_id;
	private LocalDate datetime;
	private LocalDate create_date;
	private LocalDate update_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLivehouse_information_id() {
		return livehouse_information_id;
	}
	public void setLivehouse_information_id(int livehouse_information_id) {
		this.livehouse_information_id = livehouse_information_id;
	}
	public LocalDate getDatetime() {
		return datetime;
	}
	public void setDatetime(LocalDate datetime) {
		this.datetime = datetime;
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
	public Livehouse_application_table(int id, int livehouse_information_id, LocalDate datetime, LocalDate create_date,
			LocalDate update_date) {
		super();
		this.id = id;
		this.livehouse_information_id = livehouse_information_id;
		this.datetime = datetime;
		this.create_date = create_date;
		this.update_date = update_date;
	}

	
}
