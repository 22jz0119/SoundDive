package model;

public class Member {
    private int id;
    private int artist_group_id;
    private String member_name;
    private String member_position; // 修正: member_posision → member_position

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

    public String getMember_position() { // 修正: member_posision → member_position
        return member_position;
    }

    public void setMember_position(String member_position) { // 修正: member_posision → member_position
        this.member_position = member_position;
    }

    public Member(int id, int artist_group_id, String member_name, String member_position) { // 修正: member_posision → member_position
        super();
        this.id = id;
        this.artist_group_id = artist_group_id;
        this.member_name = member_name;
        this.member_position = member_position; // 修正: member_posision → member_position
    }
}
