package org.beginningee6.book.chapter03.ex11;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * ＠OneToOneアノテーションにより
 * 他のエンティティへの双方向の１対１
 * のリレーションシップを持つエンティティ。
 * 
 * 双方向のリレーションシップを定義する場合は、
 * 両方のエンティティで他方のエンティティを参照
 * するフィールドに＠OneToOneアノテーションを
 * 付与する必要がある。
 * 
 * また、mappedBy属性が設定されない＠OneToOne
 * アノテーションが付与されたエンティティが
 * マッピングされるテーブルに他方のエンティティが
 * マッピングされるテーブルの主キーを外部キー
 * 参照として持つカラムが付加される。
 * 
 * この例では、Customer11エンティティがマッピング
 * されるcustomer_ex11テーブルにaddress_ex11テーブル
 * の主キーを格納するデータベースカラム：ADDRESS_IDが
 * 生成される。
 * 
 */
@Entity
@Table(name = "customer_ex11")
public class Customer11 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    
    @OneToOne(fetch = FetchType.LAZY)		// １対１のリレーションシップ
    private Address11 address;			
    
    public Customer11() {}

	public Customer11(String firstName, String lastName, String email,
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

	public Address11 getAddress() {
		return address;
	}

	public void setAddress(Address11 address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Customer11 [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
	}
}
