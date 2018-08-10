package com.example.asus.summervacationproject.bean;

public class User {
	int id;
	long phoneNumber;
	String name;
	String password;
	String imageUrl;
	String sex;
	int age;
	String siteOfReceive;
	String idOfShoppingCart;    //购物车记录数据库的id
	String idOfBuyed;
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSiteOfReceive() {
		return siteOfReceive;
	}
	public void setSiteOfReceive(String siteOfReceive) {
		this.siteOfReceive = siteOfReceive;
	}
	public String getIdOfShoppingCart() {
		return idOfShoppingCart;
	}
	public void setIdOfShoppingCart(String idOfShoppingCart) {
		this.idOfShoppingCart = idOfShoppingCart;
	}
	public String getIdOfBuyed() {
		return idOfBuyed;
	}
	public void setIdOfBuyed(String idOfBuyed) {
		this.idOfBuyed = idOfBuyed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}	
