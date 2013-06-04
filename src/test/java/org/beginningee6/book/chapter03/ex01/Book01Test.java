package org.beginningee6.book.chapter03.ex01;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.beginningee6.book.chapter03.ex01.Book01;
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
 * Book01クラスの動作確認テスト
 * 
 */
@RunWith(Arquillian.class)
public class Book01Test {
	
	private static final Logger logger = Logger.getLogger(Book01Test.class.getName());
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Book01.class.getPackage())
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
		
		em.createQuery("DELETE FROM Book01").executeUpdate();
		userTransaction.commit();
	}
	
	/**
	 * Book01エンティティクラスのインスタンスを１つ生成し、
	 * 対応するデータベースのテーブルに１行追加する。
	 */
	@Test
	public void testCreateABook() throws Exception {
		
		///// 準備 /////
		
		Book01 book = new Book01();
        book.setTitle("The Hitchhiker's Guide to the Galaxy");
        book.setPrice(12.5F);
        book.setDescription("Science fiction comedy book");
        book.setIsbn("1-84023-742-2");
        book.setNbOfPage(354);
        book.setIllustrations(false);

        ///// テスト /////
        
        userTransaction.begin();
        em.joinTransaction();
        
        // 対応するデータベースのテーブルに1行追加する
        em.persist(book);
        
        userTransaction.commit();
        
        ///// 検証 /////
        
        // persistしたBook01インスタンスにIDが付番されていることを確認
        assertThat(book.getId(), is(notNullValue()));        
	}
}
