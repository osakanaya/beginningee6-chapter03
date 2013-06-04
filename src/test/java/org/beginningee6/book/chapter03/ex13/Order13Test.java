package org.beginningee6.book.chapter03.ex13;

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
 * ＠ManyToOneアノテーションによりOrderLine13エンティティと
 * Order13エンティティとの間で一方向の多対１のリレーション
 * シップが定義されている時のエンティティの永続化に関するテスト。
 * 
 */
@RunWith(Arquillian.class)
public class Order13Test {
	
	private static final Logger logger = Logger.getLogger(Order13Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Order13.class.getPackage())
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
		
		em.createQuery("DELETE FROM Order13").executeUpdate();
		em.createQuery("DELETE FROM OrderLine13").executeUpdate();
		
		userTransaction.commit();
	}

	/**
	 * 
	 * ＠ManyToOneアノテーションにより一方向の多対１のアノテーションが
	 * 定義されたエンティティを永続化する例。
	 * 
	 * ・Order13のエンティティとOrderLine13のエンティティを生成する。
	 * ・Order13のエンティティをOrderLine13のエンティティの
	 * 　orderプロパティにセットする。
	 * ・Order13、OrderLine13の順で永続化してコミットする。
	 * 
	 * 永続化した後、OrderLine13エンティティからの検索を手段として
	 * リレーションシップにより関連付けられるOrder13エンティティへ
	 * アクセスできることを確認する。
	 * 
	 */
	@Test
	public void testCreateACustomerAndAddrses() throws Exception {
		
		///// 準備 /////
		
		OrderLine13 orderLine1 = new OrderLine13("H2G2", 12d, 1);
		OrderLine13 orderLine2 = new OrderLine13("The White Album", 14.5d, 2);
		Order13 order = new Order13();

		orderLine1.setOrder(order);
		orderLine2.setOrder(order);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(order);
		em.persist(orderLine1);
		em.persist(orderLine2);
		
		userTransaction.commit();
		
		///// 検証 /////

		// OrderLine13エンティティからリレーションシップを経由して
		// Order13エンティティを参照できることを確認

		em.clear();

		OrderLine13 persistedOrderLine1 = em.find(OrderLine13.class, orderLine1.getId());
		OrderLine13 persistedOrderLine2 = em.find(OrderLine13.class, orderLine2.getId());
		
		Order13 persistedOrder1 = persistedOrderLine1.getOrder();
		Order13 persistedOrder2 = persistedOrderLine2.getOrder();
		
		// データのIDを検証
		assertThat(persistedOrder1.getId(), is(order.getId()));
		// 異なるOrderLine13エンティティから同じOrder13エンティティへ
		// アクセスできることを確認
		assertThat(persistedOrder1.getId(), is(persistedOrder2.getId()));
	}
	
}
