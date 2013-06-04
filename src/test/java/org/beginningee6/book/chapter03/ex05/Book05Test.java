package org.beginningee6.book.chapter03.ex05;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
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
 * ＠Columnアノテーションでフィールドに設定された制約の挙動を
 * 確認するテスト。
 * 
 */
@RunWith(Arquillian.class)
public class Book05Test {
	
	private static final Logger logger = Logger.getLogger(Book05Test.class.getName());
	
	// 送出される例外クラスを検証するためのルールを設定する。
	// （このフィールドはpublicである必要がある）
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Deployment
	public static Archive<?> createDeployment() {
		
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
			.addPackage(Book05.class.getPackage())
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
	
	/**
	 * 
	 * 各テストメソッドで行うエンティティの永続化で
	 * 例外がスローされ、トランザクションを表現する
	 * UserTransactionがロールバックにマークされていたら
	 * ロールバックの処理を実行する。
	 * 
	 * テストメソッドで例外が発生し、コミットもロール
	 * バックも行われていない状態で次のテストメソッドに進んで
	 * 新たにトランザクションを開始しようとすると、
	 * NotSupportedExceptionが発生するため、この処理が必要に
	 * なる。
	 * 
	 */
	@After
	public void tearDown() throws Exception {
		
		if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
			// ロールバックがマークされていたらロールバックを行う
			userTransaction.rollback();
		}
	}
	
	private void clearData() throws Exception {
		userTransaction.begin();
		em.joinTransaction();

		logger.info("Dumping old records...");
		
		em.createQuery("DELETE FROM Book05").executeUpdate();
		userTransaction.commit();
	}

	/**
	 * 
	 * ＠Columnアノテーションで示される制約が守られている
	 * エンティティが永続化できることを確認する。
	 * 
	 */
	@Test
	public void testCreateABook() throws Exception {
		
		///// 準備 /////
		
		// フィールドの＠Columnアノテーションで定義された制約が守られているエンティティを作成
		Book05 book = new Book05("The Hitchhiker's Guide to the Galaxy", 
				12.5F, "1234567890123456", "1-84023-742-2", 354, false);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(book);
		
		userTransaction.commit();		// 正常にコミットできる
		
		///// 検証 /////
		// 永続化によってIDが付番されていることを確認する。
		assertThat(book.getId(), is(notNullValue()));
	}
	
	/**
	 * Null値が不許可のtitleフィールドに
	 * nullがセットされたエンティティを永続化する。
	 * 
	 * UserTransaction.commit()によりデータベースにその永続化をコミットしようとすると、
	 * RollbackExceptionが発生し、データが永続化できないことを確認する。
	 * 
	 */
	@Test
	public void testCannotCreateABookWhenNullForNotNullableColumn() throws Exception {
		
		///// 準備 /////
		
		// userTransaction.commit()によりRollbackExceptionがスローされることを期待する
		expectedException.expect(RollbackException.class);

		// titleをnullにセットしてエンティティを生成
		Book05 book = new Book05(null, 
				12.5F, "1234567890123456", "1-84023-742-2", 354, false);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();

		// エンティティを永続化
		em.persist(book);
		
		userTransaction.commit();	// コミットしようとするとRollbackExceptionが発生
	}

	/**
	 * Null値が不許可のtitleフィールドに
	 * nullがセットされたエンティティを永続化する。
	 * 
	 * EntityManager.flush()によりエンティティのデータを
	 * データベーステーブルのレコードとして保存しようとすると
	 * PersistenceExceptionが発生し、データが永続化できないことを確認する。
	 * 
	 */
	@Test
	public void testCannotCreateABookWhenNullForNotNullableColumnByFlush() throws Exception {
		
		///// 準備 /////
		
		// EntityManager.flush()によりRollbackExceptionがスローされることを期待する
		expectedException.expect(PersistenceException.class);

		// titleをnullにセットしてエンティティを生成
		Book05 book = new Book05(null, 
				12.5F, "1234567890123456", "1-84023-742-2", 354, false);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		// エンティティを永続化
		em.persist(book);
		
		// コミットする前に、データベーステーブルのレコードとして
		// データを保存する
		em.flush();					// エンティティマネージャのflush()メソッドで
									// データベースに反映させようとすると
									// PersistenceExceptionが発生
	}

	/**
	 * 
	 * ＠Columnアノテーションで定義された制約が
	 * 指定が守られているエンティティを永続化してコミットする。
	 * 
	 * 次にそのエンティティをデータベースから取り出し、
	 * Null値が不許可のnbOfPageプロパティにnulをセットしてコミットしようと
	 * すると、NOT NULL制約により変更を保存できないことを確認する。
	 * 
	 */
	@Test
	public void testCannotUpdateABookWhenNullForNotNullableColumn() throws Exception {
		
		///// 準備 /////
		
		// userTransaction.commit()によりRollbackExceptionがスローされることを期待する
		expectedException.expect(RollbackException.class);

		// ＠Columnアノテーションの制約が守られているエンティティを作成
		Book05 book = new Book05("The Hitchhiker's Guide to the Galaxy", 
				12.5F, "1234567890123456", "1-84023-742-2", 354, false);

		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(book);
		
		userTransaction.commit();	// 正常にコミットできる
		
		em.clear();
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();

		// 前のエンティティのIdで検索してエンティティを取得
		Book05 persisted = em.find(Book05.class, book.getId());
				
		// Null値が不許可のnbOfPageプロパティにnulをセット
		persisted.setNbOfPage(null);
		
		userTransaction.commit();	// コミットしようとするとRollbackExceptionが発生
	}
	
	/**
	 * 
	 * ＠Columnアノテーションで定義された制約が
	 * 指定が守られているエンティティを永続化してコミットする。
	 * 
	 * 次にそのエンティティをデータベースから取り出し、
	 * 変更不可のtitleプロパティに変更してコミットする。
	 * 
	 * この時、例外がスローされず、エンティティに加えた変更が
	 * データベースに反映されていないことを確認する。
	 * 
	 */
	@Test
	public void testCannotUpdateABookForNotUpdatableColumn() throws Exception {
		
		///// 準備 /////
		
		// ＠Columnアノテーションの制約が守られているエンティティを作成
		Book05 book = new Book05("The Hitchhiker's Guide to the Galaxy", 
				12.5F, "1234567890123456", "1-84023-742-2", 354, false);

		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(book);
		
		userTransaction.commit();	// 正常にコミットできる
		
		em.clear();

		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();

		// 前のエンティティのIdで検索してエンティティを取得
		Book05 persisted = em.find(Book05.class, book.getId());
		
		// 変更不可のtitleプロパティの値を変更する。
		persisted.setTitle("Updated title");

		userTransaction.commit();	// 正常にコミットできる
		
		em.clear();
		
		///// 検証 /////
		
		// 前のエンティティのIdで検索してエンティティを取得
		Book05 updated = em.find(Book05.class, book.getId());
		
		// データのtitleの値が変更されていないことを確認する
		assertThat(updated.getTitle(), is("The Hitchhiker's Guide to the Galaxy"));
	}
	
	/**
	 * 
	 * 最大文字列長が16として指定されているdescriptionフィールドに
	 * 文字列長17のデータがセットされたエンティティを永続化できないことを確認する。
	 * 
	 */
	@Test
	public void testCannotCreateABookWhenViolatingColumnLengthConstraint() throws Exception {
		
		///// 準備 /////
		
		// userTransaction.commit()によりRollbackExceptionがスローされることを期待する
		expectedException.expect(RollbackException.class);

		// 最大文字列長が16に指定されているdescriptionフィールドに
		// 文字列長17の文字列がセットされたエンティティを作成
		Book05 book = new Book05(null, 
				12.5F, "12345678901234567", "1-84023-742-2", 354, false);
		
		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(book);
		
		userTransaction.commit();	// コミットしようとするとRollbackExceptionが発生
	}
	
	/**
	 * 
	 * 最大文字列長が16に指定されているdescriptionフィールドの値を
	 * 文字列長17の文字列をセットした場合に更新できないことを確認する。
	 */
	@Test
	public void testCannotUpdateABookWhenViolatingColumnLengthConstraint() throws Exception {
		
		///// 準備 /////
		
		// userTransaction.commit()によりRollbackExceptionがスローされることを期待する
		expectedException.expect(RollbackException.class);

		// ＠Columnアノテーションの制約が守られているエンティティを作成
		Book05 book = new Book05("The Hitchhiker's Guide to the Galaxy", 
				12.5F, "1234567890123456", "1-84023-742-2", 354, false);

		userTransaction.begin();
		em.joinTransaction();
		
		em.persist(book);
		
		userTransaction.commit();	// 正常にコミットできる
		
		em.clear();

		///// テスト /////
		
		userTransaction.begin();
		em.joinTransaction();

		// 前のエンティティのIdで検索してエンティティを取得
		Book05 persisted = em.find(Book05.class, book.getId());
		
		// 最大文字列長が16に指定されているdescriptionフィールドの値を
		// 文字列長17の文字列で更新
		persisted.setDescription("12345678901234567");
		
		userTransaction.commit();	// コミットしようとするとRollbackExceptionが発生
	}
	
}
