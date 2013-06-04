package org.beginningee6.book.chapter03.ex10;

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
 * ＠ManyToManyアノテーションにより
 * Artist10エンティティとCD10エンティティに
 * 双方向の多対多のリレーションシップ
 * が定義されているときのエンティティの
 * 永続化に関するテスト。
 * 
 */
@RunWith(Arquillian.class)
public class CD10Test {
	
	private static final Logger logger = Logger.getLogger(CD10Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(CD10.class.getPackage())
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
		
		em.createQuery("DELETE FROM Artist10").executeUpdate();
		em.createQuery("DELETE FROM CD10").executeUpdate();
		
		userTransaction.commit();
	}

	/**
	 * 
	 * いくつかのArtist10エンティティとCD10エンティティを生成し、
	 * Artist10のappearsOnメソッドを使用してArtist10とCD10の
	 * それぞれのエンティティを関連付け、全てのエンティティを永続化してコミットする。
	 * 
	 * その後、Artist10エンティティから関連付けられたCD10エンティティティのリストを、
	 * 逆に、CD10エンティティから関連付けられたArtist10エンティティのリストを
	 * それぞれ取得し、エンティティ間のリレーションシップが正しく永続化できていることを
	 * 確認する。
	 * 
	 */
	@Test
	public void testCreateArtistsAndCDs() throws Exception {
		
		///// 準備 /////
		
        Artist10 ringo = new Artist10("Ringo", "Starr");
        Artist10 john = new Artist10("John", "Lenon");
        Artist10 franck = new Artist10("Franck", "Zappa");
        Artist10 jimi = new Artist10("Jimi", "Hendrix");

        CD10 zoot = new CD10("Zoot Allures", 12.5F, "Released in October 1976, it is mostly a studio album");
        CD10 sgtpepper = new CD10("Sergent Pepper", 28.5F, "Best Beatles Album");
        CD10 heyjoe = new CD10("Hey Joe", 32F, "Hendrix live with friends");

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
		
		Artist10 persistedRingo 	= em.find(Artist10.class, ringo.getId());
		Artist10 persistedJohn 		= em.find(Artist10.class, john.getId());
		Artist10 persistedFranck 	= em.find(Artist10.class, franck.getId());
		Artist10 persistedJimi 		= em.find(Artist10.class, jimi.getId());

		CD10 persistedZoot 		= em.find(CD10.class, zoot.getId());
		CD10 persistedSgtpepper = em.find(CD10.class, sgtpepper.getId());
		CD10 persistedHeyjoe 	= em.find(CD10.class, heyjoe.getId());
		
		// Artist10エンティティから、関連付けられたCD10エンティティティのリストを取得

		// アーティストRingoに関連付けられたCDの数は１
		assertThat(persistedRingo.getAppearsOnCDs().size()	, is(1));
		// CDとしてsgtpepperのみ関連付けられいていることを検証
		// （hasItem Matcherによるオブジェクトの等価性チェックが有効に機能するように、
		// 　Artist10エンティティ、CD10エンティティでequalsとhashCodeメソッドを
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
		
		// CD10エンティティから、関連付けられたArtist10エンティティのリストを取得
		
		// CD Zootに関連付けられたアーティストの数は１ 
		assertThat(persistedZoot.getCreatedByArtists().size()		, is(1));
		// アーティストとしてFranckのみに関連付けられていることを検証
		assertThat(persistedZoot.getCreatedByArtists(), hasItem(persistedFranck));
		
		// CD Sgtpepperに関連付けられたアーティストの数は３ 
		assertThat(persistedSgtpepper.getCreatedByArtists().size()	, is(3));
		// アーティストとしてRingo,John,Jimiに関連付けられていることを検証
		assertThat(persistedSgtpepper.getCreatedByArtists(), 
				hasItems(persistedRingo, persistedJohn, persistedJimi));

		// CD Heyjoeに関連付けられたアーティストの数は３
		assertThat(persistedHeyjoe.getCreatedByArtists().size()		, is(3));
		// アーティストとしてJohn,Franck,Jimiに関連付けられていることを検証
		assertThat(persistedHeyjoe.getCreatedByArtists(), 
				hasItems(persistedJohn, persistedFranck, persistedJimi));
	}
	
}
