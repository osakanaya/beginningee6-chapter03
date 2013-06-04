package org.beginningee6.book.chapter03.ex08;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * Order08エンティティのorderLinesフィールド
 * から１対多のリレーションシップとして参照される、
 * 「多」側のエンティティ。
 * 
 */
@Entity
@Table(name = "orderline_ex08")
public class OrderLine08 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;				// このIDが結合テーブルの被参照側のキーに対応する
	private String item;
	private Double unitPrice;
	private Integer quantity;
	
	public OrderLine08() {}
	
	public OrderLine08(String item, Double unitPrice, Integer quantity) {
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

	@Override
	public String toString() {
		return "OrderLine08 [id=" + id + ", item=" + item + ", unitPrice="
				+ unitPrice + ", quantity=" + quantity + "]";
	}
}
