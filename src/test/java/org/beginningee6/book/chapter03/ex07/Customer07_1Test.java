package org.beginningee6.book.chapter03.ex07;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.RollbackException;
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
 * Customer07_1エンティティから
 * Address07_1エンティティへの
 * １対１（一方向）のリレーションシップ
 * が定義されているときのエンティティの
 * 永続化に関するテスト。
 * 
 * ＠JoinColumnアノテーションのnullable属性に
 * trueを設定し、Null値を許可しているため、
 * Customer07_1エンティティとこのエンティティが
 * 参照するAddress07_1エンティティの永続化の
 * 順序は任意である。
 * 
 * また、同様の理由により、Address07_1を参照
 * しないCustomer07_1エンティティを永続化する
 * ことも可能。
 * 
 */
@RunWith(Arquillian.class)
public class Customer07_1Test {
	
	private static final Logger logger = Logger.getLogger(Customer07_1Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Customer07_1.class.getPackage())
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
		
		// データベース上では、customer_ex07_1テーブルからaddress_ex07_1
		// テーブルへの外部キー参照が設定されているため、レコードの削除は
		// 外部キーを持つCustomer07_1エンティティから行う
		em.createQuery("DELETE FROM Customer07_1").executeUpdate();
		em.createQuery("DELETE FROM Address07_1").executeUpdate();
		
		userTransaction.commit();
	}
	
	/**
	 * 
	 * 外部キーにより参照される側のエンティティを先に永続化する例
	 * 
	 * Customer07_1のエンティティとAddress07_1のエンティティを生成し、
	 * Address07_1のエンティティをCustomer07_1のエンティティの
	 * addressプロパティにセットした状態で
	 * Address07_1、Customer07_1の順で永続化してコミットする。
	 * 
	 * 
	 */
	@Test
	public void testPersistAddressThenCustomer() throws Exception {
		
		///// 準備 /////
		
		Customer07_1 customer = new Customer07_1("John", "Smith", "jsmith@gmail.com", "1234565");
		Address07_1 address = new Address07_1("65B Ritherdon Rd", "At James place", "London", "LDN", "7QE554", "UK");
        customer.setAddress(address);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(address);			// address を先に永続化
		em.persist(customer);
		
		userTransaction.commit();
		
		em.clear();
		
		///// 検証 /////
		
		Customer07_1 persistedCustomer = em.find(Customer07_1.class,  customer.getId());
		// データのmailを検証
		assertThat(persistedCustomer.getEmail(), is("jsmith@gmail.com"));
		
		// Customer07_1エンティティから外部キー参照によりAddress07_1エンティティへ
		// アクセスできることを検証
		Address07_1 persistedAddress = persistedCustomer.getAddress();
		// データのidを検証
		assertThat(persistedAddress.getId(), is(notNullValue()));
		// データのstreet1を検証
		assertThat(persistedAddress.getStreet1(), is("65B Ritherdon Rd"));
	}
	
	/**
	 * 
	 * 外部キーにより参照する側のエンティティを先に永続化する例。
	 * 
	 * Customer07_1のエンティティとAddress07_1のエンティティを生成し、
	 * Address07_1のエンティティをCustomer07_1のエンティティの
	 * addressプロパティにセットした状態で
	 * Customer07_1、Address07_1の順で永続化してコミットする。
	 * 
	 * この場合、nullable=trueの設定により外部キーにより参照する側の
	 * エンティティを先に永続化しても外部キー制約に関係する例外は
	 * 発生しない。内部では、以下の3つのSQL文が順次発行されている。
	 * 
	 * （1）insert into customer_ex07_1（この時点では外部キーカラム：add_fkはNULL）
	 * （2）insert into address_ex07_1
	 * （3）update customer_ex07_1 set add_fk=XX（2で付番されたキーをセット）
	 * 
	 * 
	 */
	@Test
	public void testPersistCustomerThenAddress() throws Exception {
		
		///// 準備 /////
		
		Customer07_1 customer = new Customer07_1("John", "Smith", "jsmith@gmail.com", "1234565");
		Address07_1 address = new Address07_1("65B Ritherdon Rd", "At James place", "London", "LDN", "7QE554", "UK");
        customer.setAddress(address);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(customer);			// customer を先に永続化
		em.persist(address);
		
		userTransaction.commit();
		
		em.clear();
		
		///// 検証 /////
		
		Customer07_1 persistedCustomer = em.find(Customer07_1.class,  customer.getId());
		// データのmailを検証
		assertThat(persistedCustomer.getEmail(), is("jsmith@gmail.com"));
		
		// Customer07_1エンティティから外部キー参照によりAddress07_1エンティティへ
		// アクセスできることを検証
		Address07_1 persistedAddress = persistedCustomer.getAddress();
		// データのidを検証
		assertThat(persistedAddress.getId(), is(notNullValue()));
		// データのstreet1を検証
		assertThat(persistedAddress.getStreet1(), is("65B Ritherdon Rd"));
	}
	
	/**
	 * 
	 * 外部キーにより参照される側のエンティティへの参照を設定した場合に
	 * 参照する側のエンティティのみを永続化しようとする例。
	 * 
	 * Customer07_1のエンティティとAddress07_1のエンティティを生成し、
	 * Address07_1のエンティティをCustomer07_1のエンティティの
	 * addressプロパティにセットした状態で
	 * Customer07_1のみ永続化してコミットする。
	 * 
	 * この場合は、コミット時に例外が発生する。
	 * 
	 * つまり、setterにより外部キー参照されるエンティティをセットした場合、
	 * 参照する側と参照される側のエンティティを同時に永続化しなければ
	 * いけない、というルールがあることが分かる。
	 * 
	 */
	@Test
	public void testPersistCustomerOnlyWhenCustomerReferencesAddress() throws Exception {
		
		///// 準備 /////
		
		expectedException.expect(RollbackException.class);
		
		Customer07_1 customer = new Customer07_1("John", "Smith", "jsmith@gmail.com", "1234565");
		Address07_1 address = new Address07_1("65B Ritherdon Rd", "At James place", "London", "LDN", "7QE554", "UK");

		// Address07_1エンティティへの参照を設定する
		customer.setAddress(address);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(customer);			// addressは永続化しない
		
		userTransaction.commit();		// コミット時に例外発生
	}
	
	/**
	 * 
	 * 外部キーにより参照される側のエンティティへの参照を設定しない場合に
	 * 参照する側のエンティティのみを永続化する例。
	 * 
	 * Customer07_1のエンティティを生成し、永続化してコミットする。
	 * 
	 * この場合、nullable属性がtrueのため、Address07_1エンティティ無しに
	 * Customer07_1エンティティを永続化することができる。
	 *
	 */
	@Test
	public void testPersistCustomerOnlyWhenCustomerDoesNotReferenceAddress() throws Exception {
		
		///// 準備 /////
		// Address07_1エンティティへの参照は設定しない
		Customer07_1 customer = new Customer07_1("John", "Smith", "jsmith@gmail.com", "1234565");
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(customer);
		
		userTransaction.commit();
		
		em.clear();
		
		///// 検証 /////
		
		Customer07_1 persistedCustomer = em.find(Customer07_1.class,  customer.getId());
		// データのmailを検証
		assertThat(persistedCustomer.getEmail(), is("jsmith@gmail.com"));
		// Address07_1エンティティへの参照は無いことを確認する
		assertThat(persistedCustomer.getAddress(), is(nullValue()));
	}
}
