package model;

import java.time.LocalDate;

public class Livehouse_application {
	private int id;
	private int livehouse_information_id;
	private LocalDate datetime;
	private boolean true_false;
	private LocalDate start_time;
	private LocalDate finish_time;
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
	public boolean isTrueFalse() {
    return true_false;
	}
	
	public void setTrueFalse(boolean true_false) {
	    this.true_false = true_false;
	}
	public LocalDate getStart_time() {
		return start_time;
	}
	public void setStart_time(LocalDate start_time) {
		this.start_time = start_time;
	}
	public LocalDate getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(LocalDate finish_time) {
		this.finish_time = finish_time;
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
	public Livehouse_application(int id, int livehouse_information_id, LocalDate datetime, Boolean true_false, LocalDate start_time,LocalDate finish_time, LocalDate create_date,
			LocalDate update_date) {
		super();
		this.id = id;
		this.livehouse_information_id = livehouse_information_id;
		this.datetime = datetime;
		this.true_false = true_false;
		this.start_time = start_time;
		this.finish_time = finish_time;
		this.create_date = create_date;
		this.update_date = update_date;
	}
	
}
