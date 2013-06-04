package org.beginningee6.book.chapter03.ex12;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * ＠OneToMany・＠ManyToOneアノテーションにより、
 * 他のエンティティへの双方向の１対多の
 * リレーションシップを持つエンティティ。
 * （「１」側のエンティティに相当する）
 * 
 * ＠JoinColumnアノテーションにより、
 * 外部キー制約が設定された結合列で
 * １対多のリレーションシップをマッピング
 * するようにしている。
 * 
 * 双方向のリレーションシップを定義する場合は、
 * 
 * ・「１」側のエンティティで「多」側のエンティティ
 * 　を参照するフィールドに＠OneToManyアノテーション
 * 　を付与し、同時に、
 * ・「多」側のエンティティで「１」側のエンティティ
 * 　を参照するフィールドに＠ManyToOneアノテーション
 * 　を付与する。
 * 
 * また、１対多のリレーションシップをデータベースに
 * マッピングする場合、データベースでは「多」側の
 * テーブルが「１」側のテーブルのレコードの主キーを
 * 持つことでリレーションシップを表現するため、
 * mappedBy属性は対向のエンティティの主キーをカラム
 * として持たない「１」側のエンティティに付与する
 * ＠OneToManyアノテーションで設定する。
 * 
 * 「多」側のエンティティであるOrderLine12エンティティが
 * マッピングされるテーブルにOrder12エンティティの主キーを
 * 格納する結合カラム：ORDER_IDが追加される。
 * 
 */
@Entity
@Table(name = "order_ex12")
public class Order12 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@OneToMany(mappedBy = "order") 	// １対多のリレーションシップ
									// 結合カラムは「多」側のテーブルに
									// 生成されるため、「１」側で
									// mappedBy属性を指定する。
	private List<OrderLine12> orderLines;	

	public Order12() {
		creationDate = new Date();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<OrderLine12> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine12> orderLines) {
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
