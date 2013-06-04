package org.beginningee6.book.chapter03.ex04;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

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
 * 
 * フェッチタイプとして遅延フェッチが指定された
 * フィールドを持つエンティティであるTrack04
 * クラスの動作確認テスト
 * 
 */
@RunWith(Arquillian.class)
public class Track04Test {
	
	private static final Logger logger = Logger.getLogger(Track04Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Track04.class.getPackage())
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
		
		em.createQuery("DELETE FROM Track04").executeUpdate();
		userTransaction.commit();
	}

	/**
	 * 
	 * Track04クラスのインスタンスを永続化する。
	 * 
	 * 永続化した後にこのインスタンスを取得し、
	 * wavフィールドの取得する。
	 * 
	 */
	@Test
	public void testCreateATrack() throws Exception {
		
		///// 準備 /////
		
		
		// 生成した複合主キーを使って永続化対象のTrack04インスタンスを生成
		Track04 track = new Track04("ELVIS - That's the way it is", 72.84f, "Elvis' concert filmed at the Hilton hotel, Las Vega, NV.");
		// wavフィールドにbyte[]型のデータをセットする
		track.setWav("Wav content".getBytes("UTF-8"));
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();

		// Track04インスタンスを永続化する
		em.persist(track);
				
		userTransaction.commit();
		
		em.clear();
		
		// 永続化したTrack04インスタンスを取得する
		Track04 persisted = em.find(Track04.class, track.getId());
		
		///// 検証 /////
		
		// データのtitleを検証
		assertThat(persisted.getTitle(), is("ELVIS - That's the way it is"));
		// データのdurationを検証
		assertThat(persisted.getDuration(), is(72.84f));
		// データのdescriptionを検証
		assertThat(persisted.getDescription(), is("Elvis' concert filmed at the Hilton hotel, Las Vega, NV."));
		// データのwavを検証
		assertThat(new String(persisted.getWav(), "UTF-8"), is("Wav content"));
	}
	
}
