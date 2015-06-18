package scalikejdbc

import scalikejdbc._
import scala.concurrent.{ ExecutionContext, Future }
import scalikejdbc.async.ShortenedNames._
/**
 * Default package to import
 *
 * {{{
 *   import scalikejdbc._, async._
 * }}}
 */
package object async {

  // ---------------------
  // implicit conversions
  // ---------------------

  implicit def makeSQLExecutionAsync(sql: SQLExecution): AsyncSQLExecution = {
    new AsyncSQLExecutionImpl(sql)
  }

  implicit def makeSQLUpdateAsync(sql: SQLUpdate): AsyncSQLUpdate = {
    new AsyncSQLUpdateImpl(sql)
  }

  implicit def makeSQLUpdateAndReturnGeneratedKeyAsync(sql: SQLUpdateWithGeneratedKey): AsyncSQLUpdateAndReturnGeneratedKey = {
    new AsyncSQLUpdateAndReturnGeneratedKeyImpl(sql)
  }

  // --------------
  // one-to-x -> Option
  // --------------

  implicit def makeSQLToOptionAsync[A, B1, B2, B3, B4, B5, Z](sql: SQLToOption[A, HasExtractor]): AsyncSQLToOption[A] = {
    new AsyncSQLToOptionImpl[A](sql)
  }
  implicit def makeSQLToOptionAsync[A, B1, B2, B3, B4, B5, Z](sql: SQLToOptionImpl[A, HasExtractor]): AsyncSQLToOption[A] = {
    new AsyncSQLToOptionImpl[A](sql)
  }
  implicit def makeOneToOneSQLToOptionAsync[A, B, Z](sql: OneToOneSQLToOption[A, B, HasExtractor, Z]): AsyncOneToOneSQLToOption[A, B, Z] = {
    new AsyncOneToOneSQLToOption[A, B, Z](sql)
  }
  implicit def makeOneToManySQLToOptionAsync[A, B, Z](sql: OneToManySQLToOption[A, B, HasExtractor, Z]): AsyncOneToManySQLToOption[A, B, Z] = {
    new AsyncOneToManySQLToOption[A, B, Z](sql)
  }
  implicit def makeOneToManies2SQLToOptionAsync[A, B1, B2, Z](sql: OneToManies2SQLToOption[A, B1, B2, HasExtractor, Z]): AsyncOneToManies2SQLToOption[A, B1, B2, Z] = {
    new AsyncOneToManies2SQLToOption[A, B1, B2, Z](sql)
  }
  implicit def makeOneToManies3SQLToOptionAsync[A, B1, B2, B3, Z](sql: OneToManies3SQLToOption[A, B1, B2, B3, HasExtractor, Z]): AsyncOneToManies3SQLToOption[A, B1, B2, B3, Z] = {
    new AsyncOneToManies3SQLToOption[A, B1, B2, B3, Z](sql)
  }
  implicit def makeOneToManies4SQLToOptionAsync[A, B1, B2, B3, B4, Z](sql: OneToManies4SQLToOption[A, B1, B2, B3, B4, HasExtractor, Z]): AsyncOneToManies4SQLToOption[A, B1, B2, B3, B4, Z] = {
    new AsyncOneToManies4SQLToOption[A, B1, B2, B3, B4, Z](sql)
  }
  implicit def makeOneToManies5SQLToOptionAsync[A, B1, B2, B3, B4, B5, Z](sql: OneToManies5SQLToOption[A, B1, B2, B3, B4, B5, HasExtractor, Z]): AsyncOneToManies5SQLToOption[A, B1, B2, B3, B4, B5, Z] = {
    new AsyncOneToManies5SQLToOption[A, B1, B2, B3, B4, B5, Z](sql)
  }

  // --------------
  // one-to-x -> Traversable
  // --------------

  implicit def makeSQLToTraversableAsync[A, B1, B2, B3, B4, B5, Z](sql: SQLToTraversable[A, HasExtractor]): AsyncSQLToTraversable[A] = {
    new AsyncSQLToTraversableImpl[A](sql)
  }
  implicit def makeSQLToTraversableAsync[A, B1, B2, B3, B4, B5, Z](sql: SQLToTraversableImpl[A, HasExtractor]): AsyncSQLToTraversable[A] = {
    new AsyncSQLToTraversableImpl[A](sql)
  }
  implicit def makeOneToOneSQLToTraversableAsync[A, B, Z](sql: OneToOneSQLToTraversable[A, B, HasExtractor, Z]): AsyncOneToOneSQLToTraversable[A, B, Z] = {
    new AsyncOneToOneSQLToTraversable[A, B, Z](sql)
  }
  implicit def makeOneToManySQLToTraversableAsync[A, B, Z](sql: OneToManySQLToTraversable[A, B, HasExtractor, Z]): AsyncOneToManySQLToTraversable[A, B, Z] = {
    new AsyncOneToManySQLToTraversable[A, B, Z](sql)
  }
  implicit def makeOneToManies2SQLToTraversableAsync[A, B1, B2, Z](sql: OneToManies2SQLToTraversable[A, B1, B2, HasExtractor, Z]): AsyncOneToManies2SQLToTraversable[A, B1, B2, Z] = {
    new AsyncOneToManies2SQLToTraversable[A, B1, B2, Z](sql)
  }
  implicit def makeOneToManies3SQLToTraversableAsync[A, B1, B2, B3, Z](sql: OneToManies3SQLToTraversable[A, B1, B2, B3, HasExtractor, Z]): AsyncOneToManies3SQLToTraversable[A, B1, B2, B3, Z] = {
    new AsyncOneToManies3SQLToTraversable[A, B1, B2, B3, Z](sql)
  }
  implicit def makeOneToManies4SQLToTraversableAsync[A, B1, B2, B3, B4, Z](sql: OneToManies4SQLToTraversable[A, B1, B2, B3, B4, HasExtractor, Z]): AsyncOneToManies4SQLToTraversable[A, B1, B2, B3, B4, Z] = {
    new AsyncOneToManies4SQLToTraversable[A, B1, B2, B3, B4, Z](sql)
  }
  implicit def makeOneToManies5SQLToTraversableAsync[A, B1, B2, B3, B4, B5, Z](sql: OneToManies5SQLToTraversable[A, B1, B2, B3, B4, B5, HasExtractor, Z]): AsyncOneToManies5SQLToTraversable[A, B1, B2, B3, B4, B5, Z] = {
    new AsyncOneToManies5SQLToTraversable[A, B1, B2, B3, B4, B5, Z](sql)
  }

  // --------------
  // one-to-x -> List
  // --------------

  implicit def makeSQLToListAsync[A, B1, B2, B3, B4, B5, Z](sql: SQLToList[A, HasExtractor]): AsyncSQLToList[A] = {
    new AsyncSQLToListImpl[A](sql)
  }
  implicit def makeSQLToListAsync[A, B1, B2, B3, B4, B5, Z](sql: SQLToListImpl[A, HasExtractor]): AsyncSQLToList[A] = {
    new AsyncSQLToListImpl[A](sql)
  }
  implicit def makeOneToOneSQLToListAsync[A, B, Z](sql: OneToOneSQLToList[A, B, HasExtractor, Z]): AsyncOneToOneSQLToList[A, B, Z] = {
    new AsyncOneToOneSQLToList[A, B, Z](sql)
  }
  implicit def makeOneToManySQLToListAsync[A, B, Z](sql: OneToManySQLToList[A, B, HasExtractor, Z]): AsyncOneToManySQLToList[A, B, Z] = {
    new AsyncOneToManySQLToList[A, B, Z](sql)
  }
  implicit def makeOneToManies2SQLToListAsync[A, B1, B2, Z](sql: OneToManies2SQLToList[A, B1, B2, HasExtractor, Z]): AsyncOneToManies2SQLToList[A, B1, B2, Z] = {
    new AsyncOneToManies2SQLToList[A, B1, B2, Z](sql)
  }
  implicit def makeOneToManies3SQLToListAsync[A, B1, B2, B3, Z](sql: OneToManies3SQLToList[A, B1, B2, B3, HasExtractor, Z]): AsyncOneToManies3SQLToList[A, B1, B2, B3, Z] = {
    new AsyncOneToManies3SQLToList[A, B1, B2, B3, Z](sql)
  }
  implicit def makeOneToManies4SQLToListAsync[A, B1, B2, B3, B4, Z](sql: OneToManies4SQLToList[A, B1, B2, B3, B4, HasExtractor, Z]): AsyncOneToManies4SQLToList[A, B1, B2, B3, B4, Z] = {
    new AsyncOneToManies4SQLToList[A, B1, B2, B3, B4, Z](sql)
  }
  implicit def makeOneToManies5SQLToListAsync[A, B1, B2, B3, B4, B5, Z](sql: OneToManies5SQLToList[A, B1, B2, B3, B4, B5, HasExtractor, Z]): AsyncOneToManies5SQLToList[A, B1, B2, B3, B4, B5, Z] = {
    new AsyncOneToManies5SQLToList[A, B1, B2, B3, B4, B5, Z](sql)
  }

  // ---------------------
  // utilities
  // ---------------------

  object updateFuture {
    def apply(sql: => SQLBuilder[_])(implicit session: AsyncDBSession, cxt: EC = ECGlobal): Future[Int] = {
      withSQL(sql).update.future
    }
  }

  object executeFuture {
    def apply(sql: => SQLBuilder[_])(implicit session: AsyncDBSession, cxt: EC = ECGlobal): Future[Boolean] = {
      withSQL(sql).execute.future
    }
  }

}
