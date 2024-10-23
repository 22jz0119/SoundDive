package model;

public class Member {
	private int id;
	private int artist_group_id;
	private String member_name;
	private String member_potision;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getArtist_group_id() {
		return artist_group_id;
	}
	public void setArtist_group_id(int artist_group_id) {
		this.artist_group_id = artist_group_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getMember_potision() {
		return member_potision;
	}
	public void setMember_potision(String member_potision) {
		this.member_potision = member_potision;
	}
	public Member(int id, int artist_group_id, String member_name, String member_potision) {
		super();
		this.id = id;
		this.artist_group_id = artist_group_id;
		this.member_name = member_name;
		this.member_potision = member_potision;
	}
	
	
}
