package model;

import java.time.LocalDate;

public class Livehouse_information {
	private int id;
	private String Owner_name;
	private String equipment_information;
	private String house_explanation_information;
	private String livehouse_detailed_information;
	private String livehouse_name;
	private String live_address;
	private int live_tel_number;
	private LocalDate create_date;
	private LocalDate update_date;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOwner_name() {
		return Owner_name;
	}
	public void setOwner_name(String owner_name) {
		Owner_name = owner_name;
	}
	public String getEquipment_information() {
		return equipment_information;
	}
	public void setEquipment_information(String equipment_information) {
		this.equipment_information = equipment_information;
	}
	public String getHouse_explanation_information() {
		return house_explanation_information;
	}
	public void setHouse_explanation_information(String house_explanation_information) {
		this.house_explanation_information = house_explanation_information;
	}
	public String getLivehouse_detailed_information() {
		return livehouse_detailed_information;
	}
	public void setLivehouse_detailed_information(String livehouse_detailed_information) {
		this.livehouse_detailed_information = livehouse_detailed_information;
	}
	public String getLivehouse_name() {
		return livehouse_name;
	}
	public void setLivehouse_name(String livehouse_name) {
		this.livehouse_name = livehouse_name;
	}
	public String getLive_address() {
		return live_address;
	}
	public void setLive_address(String live_address) {
		this.live_address = live_address;
	}
	public int getLive_tel_number() {
		return live_tel_number;
	}
	public void setLive_tel_number(int live_tel_number) {
		this.live_tel_number = live_tel_number;
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
	public Livehouse_information(int id, String owner_name, String equipment_information,
			String house_explanation_information, String livehouse_detailed_information, String livehouse_name,
			String live_address, int live_tel_number, LocalDate create_date, LocalDate update_date) {
		super();
		this.id = id;
		Owner_name = owner_name;
		this.equipment_information = equipment_information;
		this.house_explanation_information = house_explanation_information;
		this.livehouse_detailed_information = livehouse_detailed_information;
		this.livehouse_name = livehouse_name;
		this.live_address = live_address;
		this.live_tel_number = live_tel_number;
		this.create_date = create_date;
		this.update_date = update_date;
	}

	
	
	
	
}
