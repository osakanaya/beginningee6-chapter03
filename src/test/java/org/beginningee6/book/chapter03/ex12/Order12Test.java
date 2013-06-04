package org.beginningee6.book.chapter03.ex12;

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
 * ＠OneToManyおよび＠ManyToOneアノテーションにより
 * Order12エンティティとOrderLine12エンティティとの
 * 間で双方向の１対多のリレーションシップが定義されている
 * 時のエンティティの永続化に関するテスト。
 * 
 */
@RunWith(Arquillian.class)
public class Order12Test {
	
	private static final Logger logger = Logger.getLogger(Order12Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Order12.class.getPackage())
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
		
		em.createQuery("DELETE FROM Order12").executeUpdate();
		em.createQuery("DELETE FROM OrderLine12").executeUpdate();
		
		userTransaction.commit();
	}

	/**
	 * 
	 * ＠OneToManyおよび＠ManyToOneアノテーションにより双方向の
	 * １対多のアノテーションが定義されたエンティティを永続化する例。
	 * 
	 * ・Order12のエンティティとOrderLine12のエンティティを生成する。
	 * ・OrderLine12エンティティを２つ生成し、そのリストを生成
	 * ・OrderLine12エンティティのリストをOrder12エンティティの
	 * 　orderLinesプロパティにセット。
	 * ・Order12のエンティティをOrderLine12のエンティティの
	 * 　orderプロパティにセットする。
	 * ・Order12、OrderLine12の順で永続化してコミットする。
	 * 
	 * 永続化した後、Order12エンティティからの検索、および、
	 * OrderLine12エンティティからの検索を手段としてリレーションシップに
	 * により関連付けられる対向のエンティティへアクセスできることを
	 * 確認する。
	 * 
	 */
	@Test
	public void testCreateAOrderAndOrderlines() throws Exception {
		
		///// 準備 /////
		
		OrderLine12 orderLine1 = new OrderLine12("H2G2", 12d, 1);
		OrderLine12 orderLine2 = new OrderLine12("The White Album", 14.5d, 2);
		Order12 order = new Order12();

		List<OrderLine12> orderLines = new ArrayList<OrderLine12>();
		orderLines.add(orderLine1);
		orderLines.add(orderLine2);
		order.setOrderLines(orderLines);
		
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

		// Order12エンティティからリレーションシップを経由して
		// OrderLine12エンティティを参照できることを確認
		em.clear();

		Order12 persistedOrder = em.find(Order12.class, order.getId());

		// データのorderLinesのサイズは２
		assertThat(persistedOrder.getOrderLines().size(), is(2));
		
		OrderLine12 persistedOrderLine1 = persistedOrder.getOrderLines().get(0);
		OrderLine12 persistedOrderLine2 = persistedOrder.getOrderLines().get(1);
		// データのitemを検証
		assertThat(persistedOrderLine1.getItem(), is("H2G2"));
		// データのitemを検証
		assertThat(persistedOrderLine2.getItem(), is("The White Album"));
		
		// OrderLine12エンティティからリレーションシップを経由して
		// Order12エンティティを参照できることを確認

		em.clear();

		persistedOrderLine1 = em.find(OrderLine12.class, orderLine1.getId());
		
		persistedOrder = persistedOrderLine1.getOrder();
		// データのIDを検証
		assertThat(persistedOrder.getId(), is(order.getId()));
	}
	
}
