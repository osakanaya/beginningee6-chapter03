package org.beginningee6.book.chapter03.ex13;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * 一方向の多対１のリレーションシップを持つ
 * 「多」側のエンティティ。
 * 
 * この例では、orderフィールドから「１」側の
 * エンティティであるOrder13エンティティを参照する
 * ようにしている。
 * 
 * また、多対１のリレーションシップをデータベースに
 * マッピングする場合、データベースでは「多」側の
 * テーブルが「１」側のテーブルのレコードの主キーを
 * 持つことでリレーションシップを表現している。
 * 
 * 従って、「多」側のエンティティであるOrderLine13エンティティが
 * マッピングされるテーブルにOrder13エンティティの主キーを
 * 格納する結合カラム：ORDER_IDが追加される。

 */
@Entity
@Table(name = "orderline_ex13")
public class OrderLine13 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	private String item;
	private Double unitPrice;
	private Integer quantity;
	
	@ManyToOne
	private Order13 order;		// 多対１のリレーションシップ
								// 結合カラムは「多」側のテーブルに
								// 生成される
	
	public OrderLine13() {}
	
	public OrderLine13(String item, Double unitPrice, Integer quantity) {
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

	public Order13 getOrder() {
		return order;
	}

	public void setOrder(Order13 order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "OrderLine13 [id=" + id + ", item=" + item + ", unitPrice="
				+ unitPrice + ", quantity=" + quantity + ", order=" + order
				+ "]";
	}

}
