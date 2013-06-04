package org.beginningee6.book.chapter03.ex09;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * ＠OneToManyアノテーションにより、
 * 他のエンティティへの一方向の１対多の
 * リレーションシップを持つエンティティ。
 * （「１」側のエンティティに相当する）
 * 
 * ＠JoinColumnアノテーションにより、
 * 外部キー制約が設定された結合列で
 * １対多のリレーションシップをマッピング
 * するようにしている。
 * 
 * この例では、＠JoinColumnアノテーションにより、
 * orderLinesフィールドを介した１対多の参照関係を
 * 表現するものとして、「多」側のエンティティである
 * OrderLine09エンティティがマッピングされる
 * テーブルにOrder09エンティティの主キーを格納する
 * 結合列が追加される。
 * 
 */
@Entity
@Table(name = "order_ex09")
public class Order09 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@OneToMany(fetch = FetchType.EAGER) 	// １対多のリレーションシップ（EAGERフェッチ指定）
	@JoinColumn(name = "order_fk")			// 結合列によりリレーションシップをマッピング。
	private List<OrderLine09> orderLines;	// Order09エンティティの主キーを持つ結合列がOrderline09
											// エンティティのマッピングテーブルであるorderline_ex09
											// テーブルに含められる
	public Order09() {
		creationDate = new Date();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<OrderLine09> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine09> orderLines) {
		this.orderLines = orderLines;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Order08 [id=" + id + ", creationDate=" + creationDate
				+ ", orderLines=" + orderLines + "]";
	}
}
