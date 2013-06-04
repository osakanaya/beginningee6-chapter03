package org.beginningee6.book.chapter03.ex13;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * ＠ManyToOneアノテーションにより、
 * 他のエンティティからの一方向の多対１の
 * リレーションシップを持つエンティティ。
 * （「１」側のエンティティに相当する）
 * 
 * 「１」側のエンティティに対しては、
 * リレーションシップに関連するアノテーション
 * を付与する必要はない。
 * 
 */
@Entity
@Table(name = "order_ex13")
public class Order13 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	public Order13() {
		creationDate = new Date();
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
		return "Order13 [id=" + id + ", creationDate=" + creationDate + "]";
	}

}
