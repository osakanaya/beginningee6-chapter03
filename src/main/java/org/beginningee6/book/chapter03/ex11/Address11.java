package org.beginningee6.book.chapter03.ex11;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Customer11エンティティと双方向の１対１のリレーションシップを持つ
 * エンティティ。
 * 
 * Customer11エンティティとは異なり、Address11エンティティでは、
 * mappedBy属性を設定した＠OneToOneアノテーションを付与し、
 * これにより双方向のリレーションシップを表現する。
 * 
 * この例では、mappedBy属性に対向のCustomer11エンティティでこの
 * Address11エンティティを参照するフィールドの名前を指定する。
 * 
 */
@Entity
@Table(name = "address_ex11")
public class Address11 implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    private Long id;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipcode;
    private String country;

    @OneToOne(mappedBy = "address")
    private Customer11 customer;
    
    public Address11() {}

	public Address11(String street1, String street2, String city, String state,
			String zipcode, String country) {
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.country = country;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public Customer11 getCustomer() {
		return customer;
	}

	public void setCustomer(Customer11 customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Address11 [id=" + id + ", street1=" + street1 + ", street2="
				+ street2 + ", city=" + city + ", state=" + state
				+ ", zipcode=" + zipcode + ", country=" + country
				+ ", customer=" + customer + "]";
	}
	
}
