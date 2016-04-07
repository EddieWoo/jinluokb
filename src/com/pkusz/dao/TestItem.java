package com.pkusz.dao;

import java.io.Serializable;

public class TestItem implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 51131240818922749L;
	private String name;
	private String sex;
	private String marriage;
	private String age;
	private String tel;
	private String addr;
	private String data;
	private String date;
    
	public TestItem(){
		
	}
	public TestItem(String name,String age,String sex,String marriage,String tel,String addr, String date, String data){
		super();
		this.name=name;
		this.age=age;
		this.sex=sex;
		this.marriage=marriage;
		this.tel=tel;
		this.addr=addr;
		this.date=date;
		this.data=data;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getMarriage() {
		return marriage;
	}


	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}


	public String getAge() {
		return age;
	}


	public void setAge(String age) {
		this.age = age;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getAddr() {
		return addr;
	}


	public void setAddr(String addr) {
		this.addr = addr;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}
