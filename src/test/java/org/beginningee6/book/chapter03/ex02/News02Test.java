package org.beginningee6.book.chapter03.ex02;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * ＠Embeddable、＠EmbeddedIdアノテーションを使用して複合主キーが
 * 定義されたNews02クラスの動作確認テスト
 * 
 */
@RunWith(Arquillian.class)
public class News02Test {
	
	private static final Logger logger = Logger.getLogger(News02Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(News02.class.getPackage())
			.addAsManifestResource("test-persistence.xml", "persistence.xml")
			.addAsManifestResource("jbossas-ds.xml")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		logger.info(archive.toString(true));

		return archive;
	}
	
	@PersistenceContext
	EntityManager em;
	
	@Inject
	UserTransaction userTransaction;
	
	@Before
	public void setUp() throws Exception {
		clearData();
	}
	
	private void clearData() throws Exception {
		userTransaction.begin();
		em.joinTransaction();

		logger.info("Dumping old records...");
		
		em.createQuery("DELETE FROM News02").executeUpdate();
		userTransaction.commit();
	}

	/**
	 * 
	 * 複合主キーが定義されたNews02インスタンスを永続化する。
	 * 
	 * また、その複合主キーを指定して永続化コンテキスト内にある
	 * そのインスタンスを取得することでインスタンスの永続化を
	 * 確認する。
	 * 
	 */
	@Test
	public void testCreateANewsAndFind() throws Exception {
	
		///// 準備 /////

		// 複合主キーを生成
		NewsId02 id = new NewsId02("Richard Wright has died", "EN");

		// 生成した複合主キーを使って永続化対象のNews02インスタンスを生成
		News02 news = new News02(id, "The keyboard of Pink Floyd has died today");
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();

		// News02インスタンスを永続化する。
		// この時、データベースにレコードが登録されるが、メモリ上の永続化
		// コンテキストにおいても、永続化されたNews02インスタンスがキャッシュ
		// として存在している状態になる。
		em.persist(news);
		
		userTransaction.commit();
		
		// インスタンスの永続化の時に生成したNewsId02のインスタンス（主キー）を使って
		// News02のインスタンスを得る
		News02 persisted = em.find(News02.class, id);
		
		///// インスタンスが永続化できていることを検証 /////

		// データのcontentを検証
		assertThat(persisted.getContent(), is("The keyboard of Pink Floyd has died today"));
		// データのtitleを検証
		assertThat(persisted.getId().getTitle(), is("Richard Wright has died"));
		// データのlanguageを検証
		assertThat(persisted.getId().getLanguage(), is("EN"));
	}
	
	/**
	 * 
	 * 複合主キーが定義されたNews02インスタンスを永続化する。
	 * 
	 * また、その複合主キーを指定して永続化コンテキストのメモリ領域ではなく、
	 * データベースから永続化コンテキストを経由してインスタンスを取得する
	 * ことで、データベースへデータが永続化されたことを確実に確認する。
	 * 
	 */
	@Test
	public void testCreateANewsAndFindAfterClear() throws Exception {
	
		///// 準備 /////
		
		// 複合主キーを生成
		NewsId02 id = new NewsId02("Richard Wright has died", "EN");
		// 生成した複合主キーを使って永続化対象のNews02インスタンスを生成
		News02 news = new News02(id, "The keyboard of Pink Floyd has died today");
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		// News02インスタンスを永続化する。
		em.persist(news);
		
		userTransaction.commit();
		
		// 永続化されたエンティティを取得時に、キャッシュとして機能する永続化
		// コンテキストからではなくデータベースからデータが取得されるように
		// することを確実にするため、永続化コンテキスト内のキャッシュをクリア
		em.clear();
		
		// インスタンスの永続化の時に生成したNewsId02のインスタンス（主キー）を使って
		// News02のインスタンスを得る
		News02 persisted = em.find(News02.class, id);
		
		///// インスタンスがデータベースへ永続化できていることを検証 /////
		
		// データのcontentを検証
		assertThat(persisted.getContent(), is("The keyboard of Pink Floyd has died today"));
		// データのtitleを検証
		assertThat(persisted.getId().getTitle(), is("Richard Wright has died"));
		// データのlanguageを検証
		assertThat(persisted.getId().getLanguage(), is("EN"));
	}
	
	/**
	 * 
	 * 複合主キーが定義されたNews02インスタンスを永続化する。
	 * 
	 * また、インスタンスを永続化した時に生成した複合主キーのオブジェクト
	 * と同じ値を持つオブジェクトを別に作成し、このオブジェクトをキーと
	 * して永続化されたインスタンスを取得する。
	 * 
	 * これにより、キーの一意性がオブジェクトの同一性ではなく、同値性により
	 * 正しく決定されていることを確認する。
	 * 
	 */
	@Test
	public void testCreateANewsAndFindByAnother() throws Exception {
	
		///// 準備 /////
		
		// 複合主キーを生成
		NewsId02 id = new NewsId02("Richard Wright has died", "EN");
		// 生成した複合主キーを使って永続化対象のNews02インスタンスを生成
		News02 news = new News02(id, "The keyboard of Pink Floyd has died today");
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(news);
		
		userTransaction.commit();
		
		// 永続化されたエンティティを取得時に、キャッシュとして機能する永続化
		// コンテキストからではなくデータベースからデータが取得されるように
		// することを確実にするため、永続化コンテキスト内のキャッシュをクリア
		em.clear();
				
		
		// インスタンスの永続化の時に生成したNewsId02のインスタンス（主キー）を使って
		// News02のインスタンスを得る
		NewsId02 id2 = new NewsId02("Richard Wright has died", "EN");	
		News02 persisted = em.find(News02.class, id2);
		
		///// 複合主キーオブジェクトの同値性によりエンティティの一意性が決定されていることを検証 /////
		
		// データのcontentを検証
		assertThat(persisted.getContent(), is("The keyboard of Pink Floyd has died today"));
		// データのtitleを検証
		assertThat(persisted.getId().getTitle(), is("Richard Wright has died"));
		// データのlanguageを検証
		assertThat(persisted.getId().getLanguage(), is("EN"));
	}
	
	/**
	 * 
	 * 永続化された、複合主キーを持つNews02インスタンスが名前付きクエリにより
	 * 取得できることを確認する。
	 * 
	 */
	@Test
	public void testFindOneNewsTitle() throws Exception {
		
		///// 準備 /////
		
		// 複合主キーを生成
		NewsId02 id = new NewsId02("Richard Wright has died", "EN");
		// 生成した複合主キーを使って永続化対象のNews02インスタンスを生成
		News02 news = new News02(id, "The keyboard of Pink Floyd has died today");
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(news);
		
		userTransaction.commit();
        
		///// テスト /////
		
        // News02クラスで定義された名前付きクエリを使用して、全てのエンティティを取得
        List<String> newsTitles = em.createNamedQuery("findAllNewsTitles", String.class).getResultList();
        
        ///// 永続化されたエンティティが取得できたことを検証 /////
        
        // データの総数は１
        assertThat(newsTitles.size(), is(1));
        // データのタイトルを検証
        assertThat(newsTitles.get(0), is("Richard Wright has died"));
	}
}
