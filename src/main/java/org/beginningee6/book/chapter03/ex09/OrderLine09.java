package org.beginningee6.book.chapter03.ex09;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * Order09エンティティのorderLinesフィールド
 * から１対多のリレーションシップとして参照される、
 * 「多」側のエンティティ。
 * 
 * Order09エンティティのorderLinesフィールドには、
 * カラム結合を使用するように＠OneToManyアノテーション
 * が付与されているため、OrderLine09エンティティに
 * マッピングされるテーブル（orderline_ex09）には、
 * 「１」側のエンティティであるOrder09エンティティの
 * 主キーを外部キーとして格納するカラム（order_fk）が
 * 必要になる。
 * 
 */
@Entity
@Table(name = "orderline_ex09")
public class OrderLine09 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	private String item;
	private Double unitPrice;
	private Integer quantity;
	
	public OrderLine09() {}
	
	public OrderLine09(String item, Double unitPrice, Integer quantity) {
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
