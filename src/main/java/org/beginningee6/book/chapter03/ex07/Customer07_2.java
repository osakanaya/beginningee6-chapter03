package org.beginningee6.book.chapter03.ex07;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * ＠OneToOneアノテーションにより
 * 他のエンティティへの一方向の１対１
 * のリレーションシップを持つエンティティ。
 * 
 * ＠OneToOneアノテーションにより、
 * addressフィールドが、データベーステーブル上で
 * Address07_2エンティティがマッピングされる
 * データベースレコードの主キーを外部キー参照として
 * 格納するカラム「add_fk」にマッピングされる。
 * 
 * また、＠JoinColumnアノテーションにより、
 * 外部キーのマッピングをカスタマイズ
 * することもできる。
 * 
 * ＠JoinColumnアノテーションのnullable属性に
 * falseを設定し、Null値を許可していないため、
 * Customer07_2エンティティとこのエンティティが
 * 参照するAddress07_2エンティティを永続化する場合は、
 * 参照されるエンティティされるAddress07_2エンティティを
 * 先に永続化しなければならない。
 * 
 * また、同様の理由により、Address07_2を参照
 * しないCustomer07_2エンティティを永続化することはできない。
 * 
 */
@Entity
@Table(name = "customer_ex07_2")
public class Customer07_2 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    
    @OneToOne(fetch = FetchType.LAZY)		// １対１のリレーションシップ
    										// Address07_1エンティティを参照
    										// するフィールドを持つことで、
    										// リレーションシップの方向を規定している
    @JoinColumn(name = "add_fk", nullable = false)
    private Address07_2 address;			// Null値を不許可
    
    public Customer07_2() {}

	public Customer07_2(String firstName, String lastName, String email,
			String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Address07_2 getAddress() {
		return address;
	}

	public void setAddress(Address07_2 address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Customer07_2 [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
	}
}
