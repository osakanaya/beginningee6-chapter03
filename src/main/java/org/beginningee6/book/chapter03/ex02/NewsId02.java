package org.beginningee6.book.chapter03.ex02;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * 複合主キーを表現するクラス。
 * 
 * ＠Embeddableアノテーションにより、
 * 他のエンティティの複合主キーとしてこのクラスの
 * オブジェクトを利用することが可能になる。
 * 
 * 主キーとして機能させるため、
 * hashCode()とequals()メソッドを適切に
 * 実装する。
 * 
 * ＠Embeddableアノテーションが付加されたクラスは
 * 独自のテーブルを持つエンティティではなく、その
 * 各フィールドはそのIDを＠EmbeddedIdアノテーション
 * で”埋め込んだ”エンティティ（News02）のテーブル
 * （news_ex02）のフィールドとしてマッピングされる。
 */
@Embeddable			// 他のエンティティの複合主キーとして指定可能であることを指定
public class NewsId02 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String title;
	private String language;
	
	public NewsId02() {}

	public NewsId02(String title, String language) {
		this.title = title;
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * hashCode()のオーバーライド（必須）
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/**
	 * equals()のオーバーライド（必須）
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsId02 other = (NewsId02) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NewsId02 [title=" + title + ", language=" + language + "]";
	}
}
