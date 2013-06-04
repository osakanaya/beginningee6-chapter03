package org.beginningee6.book.chapter03.ex03;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 複合主キーの指定（＠IdClassアノテーション使用）。
 * 
 * クラス内のフィールドの内、複数のフィールドを複合主キー
 * として機能させる場合、＠IdClassアノテーションにより、
 * 主キークラスを指定する。
 * 
 * また、エンティティクラスでは、複合主キーのクラスの
 * フィールドを重複する形で定義し、かつ、これらのフィールド
 * に＠Idアノテーションを付加しておく必要がある。
 * 
 * (注）複合主キーの定義は、＠EmbeddedIdと＠IdClassの
 * 2種類の方法があるが、複合主キーのフィールドを検索する
 * 場合においてクエリの指定方法が異なることに注意。
 * 
 * ＠IdClassを使用して複合主キーを定義した場合は、
 * 以下のようにクエリを指定する。
 * 
 * "SELECT n.title FROM News03 n"
 * との違いに注意。
 */
@Entity
@Table(name = "news_ex03")
@IdClass(NewsId03.class)		// NewsId03を主キークラスに指定
@NamedQuery(name = "findAllNewsTitles", query = "SELECT n.title FROM News03 n")
public class News03 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id							// languageと併せて主キーとなる
	private String title;
	@Id							// titleと併せて主キーとなる
	private String language;
	
	private String content;
	
	public News03() {}

	public News03(String title, String language, String content) {
		this.title = title;
		this.language = language;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getLanguage() {
		return language;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "News03 [title=" + title + ", language=" + language
				+ ", content=" + content + "]";
	}	
}
