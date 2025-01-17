package model;

import java.time.LocalDate;

public class Artist_group {

    private int id;
    private int user_id;
    private String account_name;
    private String picture_image_movie; // 画像パス
    private String rating_star;
    private String group_genre;
    private int band_years;
    private LocalDate create_date;
    private LocalDate update_date;
    private boolean at_true_false;

    // ★ デフォルトコンストラクタの追加 ★
    public Artist_group() {
        // 必要に応じて初期化（例: デフォルト画像パスなど）
    }

    // ★ 既存の全フィールド用コンストラクタ ★
    public Artist_group(int id, int user_id, String account_name, String picture_image_movie,
                        String group_genre, int band_years, LocalDate create_date,
                        LocalDate update_date, String rating_star, boolean at_true_false) {
        this.id = id;
        this.user_id = user_id;
        this.account_name = account_name;
        this.picture_image_movie = picture_image_movie;
        this.group_genre = group_genre;
        this.band_years = band_years;
        this.create_date = create_date;
        this.update_date = update_date;
        this.rating_star = rating_star;
        this.at_true_false = at_true_false;
    }

    // ★ ゲッターとセッター ★
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

    public String getGroup_genre() {
        return group_genre;
    }

    public void setGroup_genre(String group_genre) {
        this.group_genre = group_genre;
    }

    public int getBand_years() {
        return band_years;
    }

    public void setBand_years(int band_years) {
        this.band_years = band_years;
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

    public boolean isAt_true_false() {
        return at_true_false;
    }

    public void setAt_true_false(boolean at_true_false) {
        this.at_true_false = at_true_false;
    }
}
