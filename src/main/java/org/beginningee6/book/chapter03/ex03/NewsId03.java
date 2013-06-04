package org.beginningee6.book.chapter03.ex03;

import java.io.Serializable;

/**
 * 複合主キーを表現するクラス。
 * 
 * 他のエンティティで、このクラスのフィールドを
 * 複合主キーとして使用する場合、そのエンティティクラスで
 * ＠IdClassアノテーションによってこのクラスを指定する。
 * 
 * このクラスは独自のテーブルを持つエンティティではなく、
 * その各フィールドは@IdClassアノテーションでこのクラスを
 * 主キーとして参照するエンティティ（News03）のテーブル内
 * （news_ex03）のフィールドとしてマッピングされる。
 * 
 * ＠Embeddable/@Embeddedアノテーションを使用した複合主キーの
 * 定義と異なり、このクラス自体はアノテーションを付加する必要はなく、
 * 単なるPOJOでよいが、hashCode()とequals()メソッドを適切に
 * 実装する必要がある。
 */
public class NewsId03 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String title;
	private String language;
	
	public NewsId03() {}

	public NewsId03(String title, String language) {
		this.title = title;
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public String getLanguage() {
		return language;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsId03 other = (NewsId03) obj;
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
		return "NewsId03 [title=" + title + ", language=" + language + "]";
	}
	
}
