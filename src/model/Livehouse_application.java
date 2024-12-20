package model;

import java.time.LocalDate;

public class Livehouse_application {
    private int id;
    private int user_id;
    private int livehouse_information_id;
    private LocalDate date_time;
    private boolean true_false;
    private LocalDate start_time;
    private LocalDate finish_time;
    private LocalDate create_date;
    private LocalDate update_date;
    private int cogig_or_solo;  // 追加されたプロパティ
    private int artist_group_id;  // 追加されたプロパティ

    
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

    public int getLivehouse_information_id() {
        return livehouse_information_id;
    }

    public void setLivehouse_information_id(int livehouse_information_id) {
        this.livehouse_information_id = livehouse_information_id;
    }

    public LocalDate getDate_time() {
        return date_time;
    }

    public void setDatetime(LocalDate date_time) {
        this.date_time = date_time;           
    }

    public boolean isTrue_False() {
        return true_false;
    }

    public void setTrue_False(boolean true_false) {
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

    // 新しいゲッターとセッターを追加
    public int getCogig_or_solo() {
        return cogig_or_solo;
    }

    public void setCogig_or_solo(int cogig_or_solo) {
        this.cogig_or_solo = cogig_or_solo;
    }

    public int getArtist_group_id() {
        return artist_group_id;
    }

    public void setArtist_group_id(int artist_group_id) {
        this.artist_group_id = artist_group_id;
    }

    // コンストラクタにartist_group_idとcogig_or_soloを追加
    public Livehouse_application(int id, int user_id, int livehouse_information_id, LocalDate date_time, Boolean true_false, LocalDate start_time, LocalDate finish_time, 
                                 LocalDate create_date, LocalDate update_date, int cogig_or_solo, int artist_group_ide) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.livehouse_information_id = livehouse_information_id;
        this.date_time = date_time;
        this.true_false = true_false;
        this.start_time = start_time;
        this.finish_time = finish_time;
        this.create_date = create_date;
        this.update_date = update_date;
        this.cogig_or_solo = cogig_or_solo;  // 追加されたプロパティの初期化
        this.artist_group_id = artist_group_id;  // 追加されたプロパティの初期化
    }
}
