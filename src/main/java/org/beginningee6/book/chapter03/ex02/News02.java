package org.beginningee6.book.chapter03.ex02;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 複合主キーの指定（＠EmbeddedIdアノテーション使用）。
 * 
 * 主キーとして＠Embeddableアノテーションが付与された
 * クラスのオブジェクトを参照することで複合主キーを
 * 定義する。
 * 
 * （注）複合主キーの定義は、＠EmbeddedIdと＠IdClassの
 * 2種類の方法があるが、複合主キーのフィールドを検索する
 * 場合においてクエリの指定方法が異なることに注意。
 * 
 * ＠EmbeddedIdを使用して複合主キーを定義した場合は、
 * 以下のようにクエリを指定する。
 * 
 * SELECT n.id.title FROM News03 n
 * 
 */
@Entity
@Table(name = "news_ex02")
@NamedQuery(name = "findAllNewsTitles", query = "SELECT n.id.title FROM News02 n")
public class News02 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId					// ＠EmbeddedIdアノテーションにより
	private NewsId02 id;		// NewsId02クラスのオブジェクトを
								// 主キーに指定
	
	private String content;
	
	public News02() {}
	
	public News02(NewsId02 id, String content) {
		this.id = id;
		this.content = content;
	}

	public NewsId02 getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "NewsEmbeddedId02 [id=" + id + ", content=" + content + "]";
	}
	
}
