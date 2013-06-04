package org.beginningee6.book.chapter03.ex12;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * Order12エンティティのorderLinesフィールド
 * から１対多のリレーションシップとして参照される、
 * 「多」側のエンティティ。
 * 
 * 双方向のリレーションシップを表現するために、
 * orderフィールドでOrder09エンティティへの参照を
 * ＠ManyToOneアノテーションを付与して作成している。
 * 
 */
@Entity
@Table(name = "orderline_ex12")
public class OrderLine12 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	private String item;
	private Double unitPrice;
	private Integer quantity;
	
	@ManyToOne
	private Order12 order;		// 多対１のリレーションシップ
								// 結合カラムは「多」側のテーブルに
								// 生成されるため、こちら側では
								// mappedBy属性を指定しない。
	
	public OrderLine12() {}
	
	public OrderLine12(String item, Double unitPrice, Integer quantity) {
		this.item = item;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public Order12 getOrder() {
		return order;
	}

	public void setOrder(Order12 order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderLine12 [id=" + id + ", item=" + item + ", unitPrice="
				+ unitPrice + ", quantity=" + quantity + ", order=" + order
				+ "]";
	}
}
