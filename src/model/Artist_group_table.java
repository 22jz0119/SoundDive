package model;

import java.time.LocalDate;

public class Artist_group_table {
	private int id;
	private int user_id;
	private String account_name;
	private String picture_image_movie;
	private LocalDate create_date;
	private LocalDate update_date;
	private String rating_star;
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
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getPicture_image_movie() {
		return picture_image_movie;
	}
	public void setPicture_image_movie(String picture_image_movie) {
		this.picture_image_movie = picture_image_movie;
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
	public String getRating_star() {
		return rating_star;
	}
	public void setRating_star(String rating_star) {
		this.rating_star = rating_star;
	}
	public Artist_group_table(int id, int user_id, String account_name, String picture_image_movie,
			LocalDate create_date, LocalDate update_date, String rating_star) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.account_name = account_name;
		this.picture_image_movie = picture_image_movie;
		this.create_date = create_date;
		this.update_date = update_date;
		this.rating_star = rating_star;
	}
	
	
}
