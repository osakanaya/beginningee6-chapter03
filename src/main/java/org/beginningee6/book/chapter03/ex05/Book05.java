package org.beginningee6.book.chapter03.ex05;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ＠Columnアノテーションによるデータベーステーブル側での
 * 明示的なカラム名の指定とカラムに対するサイズ等の制約
 * を行うサンプル。
 * 
 * ・name属性でデータベーステーブル側でのカラム名を明示的に設定する。
 * ・nullable属性でNOT NULL制約を設定する。（falseで不許可）
 * ・updatable属性で更新可能かどうかを設定する。（falseで読み出しのみ）
 * ・length属性でカラムのサイズを設定する。
 * 
 */
@Entity
@Table(name = "book_ex05")			// データベース上のテーブル名は book_ex05
public class Book05 implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "book_title", nullable = false, updatable = false)
    private String title;			// データベース上のカラム名は book_title
    								// Null値は不許可
    								// 値の変更は不許可
    private Float price;
    
    @Column(length = 16)
    private String description;	// 最大文字列長は16
    
    private String isbn;
    
    @Column(name = "nb_of_page", nullable = false)
    private Integer nbOfPage;		// データベース上のカラム名は nb_of_page
    								// Null値は不許可
    private Boolean illustrations;
	
    public Book05() {}

	public Book05(String title, Float price, String description, String isbn,
			Integer nbOfPage, Boolean illustrations) {
		this.title = title;
		this.price = price;
		this.description = description;
		this.isbn = isbn;
		this.nbOfPage = nbOfPage;
		this.illustrations = illustrations;
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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getNbOfPage() {
		return nbOfPage;
	}

	public void setNbOfPage(Integer nbOfPage) {
		this.nbOfPage = nbOfPage;
	}

	public Boolean getIllustrations() {
		return illustrations;
	}

	public void setIllustrations(Boolean illustrations) {
		this.illustrations = illustrations;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Book05 [id=" + id + ", title=" + title + ", price=" + price
				+ ", description=" + description + ", isbn=" + isbn
				+ ", nbOfPage=" + nbOfPage + ", illustrations=" + illustrations
				+ "]";
	}
}
