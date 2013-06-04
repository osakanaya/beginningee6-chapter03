package org.beginningee6.book.chapter03.ex14;

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
 * 一方向の多対多のリレーションシップを持つエンティティ。
 * 
 * この例では、Artist14エンティティからCD14エンティティへの
 * リレーションシップとしているので、Artist14エンティティのみに
 * 複数のCD14エンティティを要素として持つリストのフィールドを
 * 設けて参照関係の方向を規定している。
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
 * Artist14エンティティの主キーに、
 * ２つ目のカラムが被参照側のエンティティである
 * CD14エンティティの主キーに対応する。
 * 
 */
@Entity
@Table(name = "artist_ex14")
public class Artist14 implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;				// このIDが結合テーブルの参照側のキーに対応する
    
    private String firstName;
    private String lastName;
    
    @ManyToMany 								// 多対多のリレーションシップ
    @JoinTable(name = "jnd_artist_cd_ex14", 			// 結合テーブルのテーブル名
    		joinColumns = @JoinColumn(name = "artist_fk"),	// 結合テーブルでの参照側のエンティティの主キーを持つカラム名
    		inverseJoinColumns = @JoinColumn(name = "cd_fk"))	// 結合テーブルでの被参照側のエンティティの主キーを持つカラム名
    private List<CD14> appearsOnCDs;
	
    public Artist14() {}

	public Artist14(String firstName, String lastName) {
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

	public List<CD14> getAppearsOnCDs() {
		return appearsOnCDs;
	}

	public void setAppearsOnCDs(List<CD14> appearsOnCDs) {
		this.appearsOnCDs = appearsOnCDs;
	}

	public Long getId() {
		return id;
	}
	
	public void appearsOn(CD14 cd) {
		if (appearsOnCDs == null) {
			appearsOnCDs = new ArrayList<CD14>();
		}
		appearsOnCDs.add(cd);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appearsOnCDs == null) ? 0 : appearsOnCDs.hashCode());
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
		Artist14 other = (Artist14) obj;
		if (appearsOnCDs == null) {
			if (other.appearsOnCDs != null)
				return false;
		} else if (!appearsOnCDs.equals(other.appearsOnCDs))
			return false;
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
				+ ", lastName=" + lastName + ", appearsOnCDs=" + appearsOnCDs
				+ "]";
	}
}
