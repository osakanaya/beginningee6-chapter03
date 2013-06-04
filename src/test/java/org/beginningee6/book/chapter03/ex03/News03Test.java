package org.beginningee6.book.chapter03.ex03;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.assertThat;

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
 * ＠IdClassアノテーションを使用して複合主キーが定義された
 * News03クラスの動作確認テスト
 * 
 */
@RunWith(Arquillian.class)
public class News03Test {
	
	private static final Logger logger = Logger.getLogger(News03Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(News03.class.getPackage())
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
		
		em.createQuery("DELETE FROM News03").executeUpdate();
		userTransaction.commit();
	}

	/**
	 * 
	 * 複合主キーが定義されたNews03インスタンスを永続化する。
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
	public void testCreateANews() throws Exception {
	
		///// 準備 /////
		
		News03 news = new News03("Richard Wright has died", "EN", "The keyboard of Pink Floyd has died today");
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(news);
		
		userTransaction.commit();
		
		em.clear();
		
		// 検索用のNewsId03のインスタンス（主キー）を生成して検索
		NewsId03 persistedKey = new NewsId03("Richard Wright has died", "EN");	
		News03 persisted = em.find(News03.class, persistedKey);
		
		///// 検証 /////
		
		// データのcontentを検証
		assertThat(persisted.getContent(), is("The keyboard of Pink Floyd has died today"));
		// データのtitleを検証
		assertThat(persisted.getTitle(), is("Richard Wright has died"));
		// データのlanguageを検証
		assertThat(persisted.getLanguage(), is("EN"));
	}
	
	/**
	 * 
	 * 永続化された、複合主キーを持つNews03インスタンスが名前付きクエリにより
	 * 取得できることを確認する。
	 * 
	 */
	@Test
	public void testFindOneNewsTitle() throws Exception {
		
		///// 準備 /////
		
		News03 news = new News03("Richard Wright has died", "EN", "The keyboard of Pink Floyd has died today");
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(news);
		
		userTransaction.commit();
        
		///// テスト /////
		
        List<String> newsTitles = em.createNamedQuery("findAllNewsTitles", String.class).getResultList();
        
        ///// 検証 /////
        
        // データの総数は１
        assertThat(newsTitles.size(), is(1));
        // データのタイトルを検証
        assertThat(newsTitles.get(0), is("Richard Wright has died"));
	}

}
