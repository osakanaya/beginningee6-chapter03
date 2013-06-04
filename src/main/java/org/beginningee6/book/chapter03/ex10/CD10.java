package org.beginningee6.book.chapter03.ex10;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * ＠ManyToManyアノテーションにより、
 * 他のエンティティとの双方向の
 * 多対多のリレーションシップを持つエンティティ。
 * 
 * 多対多のリレーションシップでは
 * どちらのエンティティも「参照する側」のエンティティに
 * 指定できるが、ここでは、Artist10を「参照する側」の
 * エンティティとしているため、CD10エンティティは
 * 「参照される側」のエンティティとなる。
 * 
 * このため、「参照される側」のエンティティである
 * CD10エンティティから「参照する側」のArtist10
 * エンティティへの参照を表すcreatedByArtists
 * フィールドには、mappedBy属性が指定された
 * ＠ManyToManyアノテーションを付与することで、
 * ”逆方向”のリレーションシップを定義する
 * 必要がある。
 * 
 * mappedBy属性には、「参照される」側のエンティティを
 * 参照する「参照される側」のエンティティでの
 * フィールド名を指定する。
 * 
 */
@Entity
@Table(name = "cd_ex10")
public class CD10 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    private Long id;
    private String title;
    private Float price;
    private String description;
    
    @ManyToMany(mappedBy = "appearsOnCDs")		// 多対多のリレーションシップ
    private List<Artist10> createdByArtists;	// mappedBy属性でArtist10のエンティティの
    											// appearsOnCDsが参照する側のエンティティに
    											// おけるリレーションシップの端点となる
    											// フィールドであることを明示する
    public CD10() {}

	public CD10(String title, Float price, String description) {
		this.title = title;
		this.price = price;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Artist10> getCreatedByArtists() {
		return createdByArtists;
	}

	public void setCreatedByArtists(List<Artist10> createdByArtists) {
		this.createdByArtists = createdByArtists;
	}

	public Long getId() {
		return id;
	}
	
	public void createdBy(Artist10 artist) {
		if (createdByArtists == null) {
			createdByArtists = new ArrayList<Artist10>();
		}
		createdByArtists.add(artist);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		CD10 other = (CD10) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CD10 [id=" + id + ", title=" + title + ", price=" + price
				+ ", description=" + description + "]";
	}
}
