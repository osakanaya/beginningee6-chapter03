package org.beginningee6.book.chapter03.ex09;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;
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
 * 
 * ＠OneToManyアノテーションにより
 * Order09エンティティから
 * OrderLine09エンティティへの
 * １対多（一方向）のリレーションシップが
 * 定義されているエンティティの永続化に関する
 * テスト。
 * （リレーションシップは結合列への
 * 　マッピングとして実現）
 * 
 */
@RunWith(Arquillian.class)
public class Order09Test {
	
	private static final Logger logger = Logger.getLogger(Order09Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Order09.class.getPackage())
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
		
		em.createQuery("DELETE FROM Order09").executeUpdate();
		em.createQuery("DELETE FROM OrderLine09").executeUpdate();
		
		userTransaction.commit();
	}

	/**
	 * 
	 * １対多のリレーションシップが定義されたエンティティを永続化する例。
	 * 
	 * ・Order09エンティティを生成。
	 * ・OrderLine09エンティティを２つ生成し、そのリストを生成
	 * ・OrderLine09エンティティのリストをOrder09エンティティの
	 * 　orderLinesプロパティにセット。
	 * ・これらのエンティティをすべて永続化し、コミットする。
	 * 
	 */
	@Test
	public void testCreateAOrderAndOrderLines() throws Exception {
		
		///// 準備 /////
		
		OrderLine09 orderLine1 = new OrderLine09("H2G2", 12d, 1);
		OrderLine09 orderLine2 = new OrderLine09("The White Album", 14.5d, 2);
		List<OrderLine09> orderLines = new ArrayList<OrderLine09>();
		orderLines.add(orderLine1);
		orderLines.add(orderLine2);

		Order09 order = new Order09();
		order.setOrderLines(orderLines);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(order);				// 永続化する順は変えてもよい
		em.persist(orderLine1);
		em.persist(orderLine2);
		
		userTransaction.commit();
		
		em.clear();
		
		///// 検証 /////
		
		Order09 persistedOrder = em.find(Order09.class, order.getId());
		// データのidはnullではない
		assertThat(persistedOrder.getId(), is(notNullValue()));

		// Order09エンティティからリレーションシップを経由して
		// OrderLine09エンティティを参照できることを確認
		
		// データのorderLinesのサイズは２
		assertThat(persistedOrder.getOrderLines().size(), is(2));
		
		OrderLine09 persistedOrderLine1 = persistedOrder.getOrderLines().get(0);
		OrderLine09 persistedOrderLine2 = persistedOrder.getOrderLines().get(1);
		// データのitemを検証
		assertThat(persistedOrderLine1.getItem(), is("H2G2"));
		// データのitemを検証
		assertThat(persistedOrderLine2.getItem(), is("The White Album"));
	}
	
}
