package org.beginningee6.book.chapter03.ex14;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * ＠ManyToManyアノテーションにより、
 * 他のエンティティとの一方向の
 * 多対多のリレーションシップを持つエンティティ。
 * 
 * この例では、CD14エンティティは「参照される側」
 * のエンティティであるため、リレーションシップに
 * 関連するアノテーションを付与する必要はない。
 * 
 * 
 */
@Entity
@Table(name = "cd_ex14")
public class CD14 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    private Long id;
    private String title;
    private Float price;
    private String description;
    
    public CD14() {}

	public CD14(String title, Float price, String description) {
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

	public Long getId() {
		return id;
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
		CD14 other = (CD14) obj;
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
		return "CD14 [id=" + id + ", title=" + title + ", price=" + price
				+ ", description=" + description + "]";
	}
	
}
