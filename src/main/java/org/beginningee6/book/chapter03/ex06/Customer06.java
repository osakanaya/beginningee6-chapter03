package org.beginningee6.book.chapter03.ex06;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ＠Temporalアノテーションにより、
 * java.util.Dateやjava.util.Calenderのフィールドを
 * データベース上でDATE、TIME、TIMESTAMP型のうち
 * どのカラム型にマッピングするかを指定する。
 */
@Entity
@Table(name = "customer_ex06")
public class Customer06 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    
    @Temporal(TemporalType.DATE)		// カラムの型をDATE型に指定
    private Date dateOfBirth;
    
    @Temporal(TemporalType.TIMESTAMP)	// カラムの型をTIMESTAMP型に指定
    private Date creationDate;
    
    public Customer06() {}

	public Customer06(String firstName, String lastName, String email,
			String phoneNumber, Date dateOfBirth, Date creationDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.creationDate = creationDate;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Customer06 [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", dateOfBirth="
				+ dateOfBirth + ", creationDate=" + creationDate + "]";
	}
}
