package org.beginningee6.book.chapter03.ex08;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
 * ＠JoinTableアノテーションにより、
 * 結合テーブルを用いて１対多の
 * リレーションシップをマッピングするように
 * している。
 * 
 * この例では、＠JoinTableアノテーションにより、
 * orderLinesフィールドを利用して表現される
 * １対多の参照関係を管理するための結合テーブルが
 * データベース上で生成される。
 * 
 * この結合テーブルは２つのカラムで構成され、
 * １つ目のカラムが参照側のエンティティである
 * Order08エンティティの主キーに、
 * ２つ目のカラムが被参照側のエンティティである
 * OnderLine08エンティティの主キーに対応する。
 * 
 */
@Entity
@Table(name = "order_ex08")
public class Order08 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;				// このIDが結合テーブルの参照側のキーに対応する
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@OneToMany 								// １対多のリレーションシップ
	@JoinTable(name = "jnd_ord_line_ex08",	// 結合テーブルのテーブル名
			joinColumns = @JoinColumn(name = "order_fk"), // 結合テーブルでの参照側のエンティティの主キーを持つカラム名
			inverseJoinColumns = @JoinColumn(name = "order_line_fk"))	// 結合テーブルでの非参照側のエンティティの主キーを持つカラム名
	private List<OrderLine08> orderLines;
	
	public Order08() {
		creationDate = new Date();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<OrderLine08> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine08> orderLines) {
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
