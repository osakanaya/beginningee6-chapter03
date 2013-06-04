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
 * Address07_1エンティティがマッピングされる
 * データベースレコードの主キーを外部キー参照として
 * 格納するカラム「add_fk」にマッピングされる。
 * 
 * また、＠JoinColumnアノテーションにより、
 * 外部キーのマッピングをカスタマイズ
 * することもできる。
 * 
 * ＠JoinColumnアノテーションのnullable属性に
 * trueを設定し、Null値を許可しているため、
 * Customer07_1エンティティとこのエンティティが
 * 参照するAddress07_1エンティティの永続化の
 * 順序は任意である。
 * 
 * また、同様の理由により、Address07_1を参照
 * しないCustomer07_1エンティティを永続化する
 * ことも可能。
 * 
 */
@Entity
@Table(name = "customer_ex07_1")
public class Customer07_1 implements Serializable {

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
    @JoinColumn(name = "add_fk"/*, nullable = true*/)
    private Address07_1 address;			// Null値を許可（デフォルトなので省略可）
    
    public Customer07_1() {}

	public Customer07_1(String firstName, String lastName, String email,
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

	public Address07_1 getAddress() {
		return address;
	}

	public void setAddress(Address07_1 address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Customer07_1 [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
	}
}
