package model;

import java.time.LocalDate;

public class Rating_table {

	private int id;
	private int user_id;
	private int rating_star;
	private String review;
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
	public int getRating_star() {
		return rating_star;
	}
	public void setRating_star(int rating_star) {
		this.rating_star = rating_star;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
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
	public Rating_table(int id, int user_id, int rating_star, String review, LocalDate create_date,
			LocalDate update_date) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.rating_star = rating_star;
		this.review = review;
		this.create_date = create_date;
		this.update_date = update_date;
	}
	
	
}


