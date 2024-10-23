package model;

import java.util.Date;

public class Rating {

	private int id;
	private int user_id;
	private int rating_star;
	private String review;
	private Date create_date;
	private Date update_date;
	
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
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public Rating(int id, int user_id, int rating_star, String review, Date create_date,
			Date update_date) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.rating_star = rating_star;
		this.review = review;
		this.create_date = create_date;
		this.update_date = update_date;
	}
	
	
}


