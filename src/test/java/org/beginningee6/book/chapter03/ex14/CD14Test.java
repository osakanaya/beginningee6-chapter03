package org.beginningee6.book.chapter03.ex14;

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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * 
 * ＠ManyToManyアノテーションにより
 * Artist14エンティティからCD14エンティティへの
 * 一方向の多対多のリレーションシップ
 * が定義されているときのエンティティの
 * 永続化に関するテスト。
 * 
 */
@RunWith(Arquillian.class)
public class CD14Test {
	
	private static final Logger logger = Logger.getLogger(CD14Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(CD14.class.getPackage())
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
		
		em.createQuery("DELETE FROM Artist14").executeUpdate();
		em.createQuery("DELETE FROM CD14").executeUpdate();
		
		userTransaction.commit();
	}

	/**
	 * 
	 * いくつかのArtist14エンティティとCD14エンティティを生成し、
	 * Artist14のappearsOnメソッドを使用してArtist14とCD14の
	 * それぞれのエンティティを関連付け、全てのエンティティを永続化してコミットする。
	 * 
	 * その後、Artist14エンティティから関連付けられたCD14エンティティティのリストを
	 * 取得し、エンティティ間のリレーションシップが正しく永続化できていることを
	 * 確認する。
	 * 
	 */
	@Test
	public void testCreateArtistsAndCDs() throws Exception {
		
		///// 準備 /////
		
        Artist14 ringo 	= new Artist14("Ringo", "Starr");
        Artist14 john 	= new Artist14("John", "Lenon");
        Artist14 franck = new Artist14("Franck", "Zappa");
        Artist14 jimi 	= new Artist14("Jimi", "Hendrix");

        CD14 zoot 		= new CD14("Zoot Allures", 12.5F, "Released in October 1976, it is mostly a studio album");
        CD14 sgtpepper 	= new CD14("Sergent Pepper", 28.5F, "Best Beatles Album");
        CD14 heyjoe 	= new CD14("Hey Joe", 32F, "Hendrix live with friends");

        // アーティストとCDの関連付け
        
        ringo.appearsOn(sgtpepper);
        john.appearsOn(sgtpepper);
        john.appearsOn(heyjoe);
        franck.appearsOn(zoot);
        franck.appearsOn(heyjoe);
        jimi.appearsOn(sgtpepper);
        jimi.appearsOn(heyjoe);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();

		em.persist(ringo);
		em.persist(john);
		em.persist(franck);
		em.persist(jimi);

		em.persist(zoot);
		em.persist(sgtpepper);
		em.persist(heyjoe);

		userTransaction.commit();
		
		em.clear();
		
		///// 検証 /////
		
		Artist14 persistedRingo 	= em.find(Artist14.class, ringo.getId());
		Artist14 persistedJohn 		= em.find(Artist14.class, john.getId());
		Artist14 persistedFranck 	= em.find(Artist14.class, franck.getId());
		Artist14 persistedJimi 		= em.find(Artist14.class, jimi.getId());

		CD14 persistedZoot 		= em.find(CD14.class, zoot.getId());
		CD14 persistedSgtpepper = em.find(CD14.class, sgtpepper.getId());
		CD14 persistedHeyjoe 	= em.find(CD14.class, heyjoe.getId());

		// Artist10エンティティから、関連付けられたCD10エンティティティのリストを取得

		// アーティストRingoに関連付けられたCDの数は１
		assertThat(persistedRingo.getAppearsOnCDs().size()	, is(1));
		// CDとしてsgtpepperのみ関連付けられいていることを検証
		// （hasItem Matcherによるオブジェクトの等価性チェックが有効に機能するように、
		// 　Artist14エンティティ、CD14エンティティでequalsとhashCodeメソッドを
		// 　実装している）
		assertThat(persistedRingo.getAppearsOnCDs(), hasItem(persistedSgtpepper));
		
		// アーティストJohnに関連付けられたCDの数は２
		assertThat(persistedJohn.getAppearsOnCDs().size()	, is(2));
		// CDとしてsgtpepperとheyjoeに関連付けられいていることを検証
		assertThat(persistedJohn.getAppearsOnCDs(), hasItems(persistedSgtpepper, persistedHeyjoe));

		// アーティストFranckに関連付けられたCDの数は２
		assertThat(persistedFranck.getAppearsOnCDs().size()	, is(2));
		// CDとしてzootとheyjoeに関連付けられいていることを検証
		assertThat(persistedFranck.getAppearsOnCDs(), hasItems(persistedZoot, persistedHeyjoe));

		// アーティストJimiに関連付けられたCDの数は２
		assertThat(persistedJimi.getAppearsOnCDs().size()	, is(2));
		// CDとしてsgtpepperとheyjoeに関連付けられいていることを検証
		assertThat(persistedJimi.getAppearsOnCDs(), hasItems(persistedSgtpepper, persistedHeyjoe));

	}
	
}
