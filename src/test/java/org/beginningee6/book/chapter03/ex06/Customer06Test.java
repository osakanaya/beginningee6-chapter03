package org.beginningee6.book.chapter03.ex06;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Date;
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
 * ＠Temporalアノテーションにより、
 * java.util.Dateやjava.util.Calenderのフィールドが
 * データベース上のDATE、TIME、TIMESTAMP型のいずれかの
 * カラム型のカラムにマッピングされるエンティティのテスト。
 * 
 */
@RunWith(Arquillian.class)
public class Customer06Test {
	
	private static final Logger logger = Logger.getLogger(Customer06Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Customer06.class.getPackage())
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
		
		em.createQuery("DELETE FROM Customer06").executeUpdate();
		userTransaction.commit();
	}

	/**
	 * 
	 * DATE型のデータベースカラムにマップされたdateOfBirth
	 * フィールドと、TIMESTAMP型のカラムにマップされた
	 * creationDateフィールドのそれぞれにDateオブジェクトを
	 * セットしてエンティティを生成し、永続化してコミットする。
	 * 
	 * 次に、次にそのエンティティをデータベースから取り出し、
	 * dateOfBirthフィールドとcreationDateフィールドが
	 * 永続化されたことを確認する。
	 * 
	 * (注）併せて、データベースのテーブル定義を確認し、
	 * dateOfBirthおよびcreationDateフィールドにマッピング
	 * されるカラムのデータ型がそれぞれDATE型および
	 * TIMESTAMP型になっていることを確認すること。
	 * 
	 */
	@Test
	public void testCreateACustomer() throws Exception {
		
		///// 準備 /////
		
		Date date = new Date();
        Customer06 customer = new Customer06("John", "Smith", 
        		"jsmith@gmail.com", "1234565", date, date);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(customer);
				
		userTransaction.commit();
		
		em.clear();
		
		Customer06 persisted = em.find(Customer06.class, customer.getId());
		
		///// 検証 /////
		assertThat(persisted.getId(), is(notNullValue()));
		// @Temporalアノテーションが付加されたフィールドが
		// 永続化されることを確認
		assertThat(persisted.getDateOfBirth(), 	is(notNullValue()));
		assertThat(persisted.getDateOfBirth(), 	is(instanceOf(Date.class)));
		assertThat(persisted.getCreationDate(), is(notNullValue()));
		assertThat(persisted.getCreationDate(), is(instanceOf(Date.class)));
	}
}
