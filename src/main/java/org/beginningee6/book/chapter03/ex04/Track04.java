package org.beginningee6.book.chapter03.ex04;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * ＠Basicアノテーションによるフェッチタイプの指定。
 * 
 * このエンティティクラスでは、wavフィールドに
 * 数メガバイト規模の大きなデータが可能される想定を
 * 立てている。
 * 
 * wavフィールドのフェッチタイプに遅延フィッチ
 * （FetchType.LAZY）を指定する事で、大きなデータの
 * 読み込みをエンティティへのアクセス時ではなく、
 * getterメソッドによるwavフィールドへのアクセス時
 * まで遅延させることができる。
 * 
 * これにより、例えばtitleのようなサイズの小さいデータ
 * にだけアクセスしたいような場合に、サイズが巨大な
 * wavフィールドをアクセスする必要が無くなり、
 * エンティティ自体へのアクセス速度を保つことができる。
 * 
 * また、@Lobアノテーションをデータサイズが大きい
 * フィールドに付与することにより、データベース側では、
 * ラージオブジェクトを利用可能なデータ型でフィールド
 * が定義される。
 * 
 */
@Entity
@Table(name = "track_ex04")
public class Track04 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    private String title;
    private Float duration;
    
    @Basic(fetch = FetchType.LAZY)	// フェッチタイプの指定
    @Lob							// ラージオブジェクトの指定
    private byte[] wav;		// 遅延フェッチが指定されているため、
    							// エンティティをデータベースから取り出すときは、
    							// wavのデータはデータベースから読み取られない。
    							// getterメソッドであるgetWav()を実行する時に
    							// 初めてwavフィールドのデータがデータベースから
    							// ロードされる。

    private String description;
    
    public Track04() {}
    
    public Track04(String title, Float duration, String description) {
    	this.title = title;
    	this.duration = duration;
    	this.description = description;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	public byte[] getWav() {
		return wav;
	}

	public void setWav(byte[] wav) {
		this.wav = wav;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Track04 [id=" + id + ", title=" + title + ", duration="
				+ duration + ", wav=" + Arrays.toString(wav) + ", description="
				+ description + "]";
	}
}
