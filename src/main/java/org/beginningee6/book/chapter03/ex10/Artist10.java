package org.beginningee6.book.chapter03.ex10;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * ＠ManyToManyアノテーションにより、
 * 他のエンティティとの双方向の
 * 多対多のリレーションシップを持つエンティティ。
 * 
 * 多対多（双方向）のリレーションシップなので、
 * このエンティティには複数のCD10エンティティを要素
 * として持つリストのフィールドがあり、一方で、
 * CD10エンティティにも、複数のArtist10エンティティを
 * 要素として持つリストのフィールドがある。
 * 
 * 多対多のリレーションシップでは
 * どちらのエンティティも「参照する側」のエンティティに
 * 指定できるが、ここでは、Artist10を「参照する側」の
 * エンティティとしている。
 * 
 * ＠JoinTableアノテーションにより、
 * 結合テーブルを用いて多対多の
 * リレーションシップをマッピングするように
 * している。
 * 
 * この例では、＠JoinTableアノテーションにより、
 * appearsOnCDsフィールドを利用して表現される
 * 多対多の参照関係を管理するための結合テーブルが
 * データベース上で生成される。
 * 
 * この結合テーブルは２つのカラムで構成され、
 * １つ目のカラムが参照側のエンティティである
 * Artist10エンティティの主キーに、
 * ２つ目のカラムが被参照側のエンティティである
 * CD10エンティティの主キーに対応する。
 * 
 */
@Entity
@Table(name = "artist_ex10")
public class Artist10 implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;				// このIDが結合テーブルの参照側のキーに対応する
    
    private String firstName;
    private String lastName;
    
    @ManyToMany 								// 多対多のリレーションシップ
    @JoinTable(name = "jnd_artist_cd", 			// 結合テーブルのテーブル名
    		joinColumns = @JoinColumn(name = "artist_fk"),	// 結合テーブルでの参照側のエンティティの主キーを持つカラム名
    		inverseJoinColumns = @JoinColumn(name = "cd_fk"))	// 結合テーブルでの被参照側のエンティティの主キーを持つカラム名
    private List<CD10> appearsOnCDs;
	
    public Artist10() {}

	public Artist10(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
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

	public List<CD10> getAppearsOnCDs() {
		return appearsOnCDs;
	}

	public void setAppearsOnCDs(List<CD10> appearsOnCDs) {
		this.appearsOnCDs = appearsOnCDs;
	}

	public Long getId() {
		return id;
	}
	
	public void appearsOn(CD10 cd) {
		if (appearsOnCDs == null) {
			appearsOnCDs = new ArrayList<CD10>();
		}
		appearsOnCDs.add(cd);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist10 other = (Artist10) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Artist10 [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}
}
