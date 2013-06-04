package org.beginningee6.book.chapter03.ex11;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 * 
 * ＠OneToOneアノテーションにより
 * Customer11エンティティとAddress11
 * エンティティに双方向の１対１の
 * リレーションシップが定義されている時の
 * エンティティの永続化に関するテスト。
 * 
 */
@RunWith(Arquillian.class)
public class Customer11Test {
	
	private static final Logger logger = Logger.getLogger(Customer11Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Customer11.class.getPackage())
			.addAsManifestResource("test-persistence.xml", "persistence.xml")
			.addAsManifestResource("jbossas-ds.xml")
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		logger.info(archive.toString(true));

		return archive;
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@PersistenceContext
	EntityManager em;
	
	@Inject
	UserTransaction userTransaction;
	
	@Before
	public void setUp() throws Exception {
		clearData();
	}
	
	@After
	public void tearDown() throws Exception {
		if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
			userTransaction.rollback();
		}
	}
	
	private void clearData() throws Exception {
		userTransaction.begin();
		em.joinTransaction();

		logger.info("Dumping old records...");
		
		em.createQuery("DELETE FROM Customer11").executeUpdate();
		em.createQuery("DELETE FROM Address11").executeUpdate();
		
		userTransaction.commit();
	}
	
	/**
	 * 
	 * ＠OneToOneアノテーションにより双方向の１対１のアノテーションが
	 * 定義されたエンティティを永続化する例。
	 * 
	 * ・Customer11のエンティティとAddress11のエンティティを生成する。
	 * ・Address11のエンティティをCustomer11のエンティティの
	 * 　addressプロパティにセットする。
	 * ・Customer11のエンティティをAddress11のエンティティの
	 * 　customerプロパティにセットする。
	 * ・Customer11、Address11の順で永続化してコミットする。
	 * 
	 * 永続化した後、Customer11エンティティからの検索、および、
	 * Address11エンティティからの検索を手段としてリレーションシップに
	 * により関連付けられる対向のエンティティへアクセスできることを
	 * 確認する。
	 * 
	 */
	@Test
	public void testPersistCustomerThenAddress() throws Exception {
		
		///// 準備 /////
		
		Customer11 customer = new Customer11("John", "Smith", "jsmith@gmail.com", "1234565");
		Address11 address = new Address11("65B Ritherdon Rd", "At James place", "London", "LDN", "7QE554", "UK");
        customer.setAddress(address);
        address.setCustomer(customer);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(customer);
		em.persist(address);			// address を先に永続化
		
		userTransaction.commit();
		
		///// 検証 /////
		em.clear();
		
		Customer11 persistedCustomer = em.find(Customer11.class,  customer.getId());
		// データのmailを検証
		assertThat(persistedCustomer.getEmail(), is("jsmith@gmail.com"));
		
		// Customer11エンティティからリレーションシップによりAddress11エンティティへ
		// アクセスできることを検証
		Address11 persistedAddress = persistedCustomer.getAddress();
		// データのidを検証
		assertThat(persistedAddress.getId(), is(notNullValue()));
		// データのstreet1を検証
		assertThat(persistedAddress.getStreet1(), is("65B Ritherdon Rd"));

		// Address11エンティティからリレーションシップによりCustomer11エンティティへ
		// アクセスできることを検証
		em.clear();

		persistedAddress = em.find(Address11.class, address.getId());
		// データのstreet1を検証
		assertThat(persistedAddress.getStreet1(), is("65B Ritherdon Rd"));
		
		persistedCustomer = persistedAddress.getCustomer();
		// データのmailを検証
		assertThat(persistedCustomer.getEmail(), is("jsmith@gmail.com"));
		
	}
	
}
