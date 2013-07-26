package example

import scalikejdbc._, SQLInterpolation._, async._
import scala.concurrent._
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import org.joda.time.DateTime

import org.scalatest._
import org.scalatest.matchers._
import org.slf4j.LoggerFactory

class MySQLExample extends FlatSpec with ShouldMatchers {

  val log = LoggerFactory.getLogger(classOf[MySQLExample])
  val column = AsyncLover.column

  // TODO mysql-async 0.2.4 doesn't work as expected.

  ConnectionPool.add('mysql, "jdbc:mysql://localhost/scalikejdbc", "sa", "sa")
  ExampleDBInitializer.initMySQL()

  AsyncConnectionPool.add('mysql, "jdbc:mysql://localhost/scalikejdbc", "sa", "sa")

  val al = AsyncLover.syntax("al")

  it should "retrieve a single value" in {
    val f: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 1) }.map(AsyncLover(al)).single.future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)
    f.value.get.get.isDefined should be(true)
  }

  it should "retrieve several values as Traversable" in {
    val f: Future[Traversable[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).limit(2) }.map(AsyncLover(al)).traversable.future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)
    f.value.get.get.size should equal(2)
  }

  it should "retrieve several values" in {
    val f: Future[List[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).limit(2) }.map(AsyncLover(al)).list.future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)
    f.value.get.get.size should equal(2)
  }

  it should "return generated key" in {

    val createdTime = DateTime.now.withMillisOfSecond(0)
    val f: Future[Long] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL {
        insert.into(AsyncLover).namedValues(
          column.name -> "Eric",
          column.rating -> 2,
          column.isReactive -> false,
          column.createdAt -> createdTime)
      }.updateAndReturnGeneratedKey().future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)
    val generatedId: Long = f.value.get.get
    log.info(s"MySQL generated key: ${generatedId}")

    val ff: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, generatedId) }.map(AsyncLover(al)).single.future()
    }
    Await.result(ff, 5.seconds)

    ff.value.get.isSuccess should be(true)
    ff.value.get.get.isDefined should be(true)

    val asyncLover = ff.value.get.get.get
    asyncLover.id should equal(generatedId)
    asyncLover.name should equal("Eric")
    asyncLover.rating should equal(2)
    asyncLover.isReactive should be(false)
    asyncLover.createdAt should equal(createdTime)
  }

  it should "update" in {
    val f: Future[Int] = NamedAsyncDB('mysql).withPool { implicit s =>
      val column = AsyncLover.column
      withSQL { delete.from(AsyncLover).where.eq(column.id, 997) }.update.future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)

    val ff: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 997) }.map(AsyncLover(al)).single.future()
    }
    Await.result(ff, 5.seconds)

    ff.value.get.isSuccess should be(true)
    ff.value.get.get.isDefined should be(false)
  }

  it should "execute" in {
    val f: Future[Boolean] = NamedAsyncDB('mysql).withPool { implicit s =>
      val column = AsyncLover.column
      withSQL { delete.from(AsyncLover).where.eq(column.id, 997) }.execute.future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)

    val ff: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 997) }.map(AsyncLover(al)).single.future()
    }
    Await.result(ff, 5.seconds)

    ff.value.get.isSuccess should be(true)
    ff.value.get.get.isDefined should be(false)
  }

  it should "update in a local transaction" in {
    val createdTime = DateTime.now.withMillisOfSecond(0)

    val f: Future[Unit] = NamedAsyncDB('mysql).localTx { implicit s =>
      withSQL { delete.from(AsyncLover).where.eq(column.id, 1002) }.update.future().flatMap { _ =>
        withSQL {
          insert.into(AsyncLover).namedValues(
            column.id -> 1002,
            column.name -> "Patric",
            column.rating -> 2,
            column.isReactive -> false,
            column.createdAt -> createdTime)
        }.update.future().map { _ => }
      }
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)

    val ff: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 1002) }.map(AsyncLover(al)).single.future()
    }
    Await.result(ff, 5.seconds)

    ff.value.get.isSuccess should be(true)
    ff.value.get.get.isDefined should be(true)

    val asyncLover = ff.value.get.get.get
    asyncLover.id should equal(1002)
    asyncLover.name should equal("Patric")
    asyncLover.rating should equal(2)
    asyncLover.isReactive should be(false)
    asyncLover.createdAt should equal(createdTime)
  }

  it should "rollback in a local transaction" in {
    val createdTime = DateTime.now.withMillisOfSecond(0)

    NamedDB('mysql).autoCommit { implicit s =>
      withSQL { delete.from(AsyncLover).where.eq(column.id, 1003) }.update.apply()
    }

    val f: Future[Unit] = NamedAsyncDB('mysql).localTx { implicit s =>
      withSQL {
        insert.into(AsyncLover).namedValues(
          column.id -> 1003,
          column.name -> "Patric",
          column.rating -> 2,
          column.isReactive -> false,
          column.createdAt -> createdTime)
      }.update.future().flatMap { _ =>
        sql"invalid_query".execute().future()
      }.map { _ => }
    }
    try {
      Await.result(f, 5.seconds)
      fail("Exception expected")
    } catch {
      case e: Exception => log.debug("expected", e)
    }

    f.value.get.isSuccess should be(false)

    val ff: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 1003) }.map(AsyncLover(al)).single.future()
    }
    Await.result(ff, 5.seconds)

    ff.value.get.isSuccess should be(true)
    ff.value.get.get.isDefined should be(false)
  }

  it should "update in a transaction" in {

    val createdTime = DateTime.now.withMillisOfSecond(0)
    val f: Future[Seq[AsyncQueryResult]] = NamedAsyncDB('mysql).withPool { implicit s =>
      AsyncTx.withBuilders(
        delete.from(AsyncLover).where.eq(column.id, 997),
        insert.into(AsyncLover).namedValues(
          column.id -> 997,
          column.name -> "Eric",
          column.rating -> 2,
          column.isReactive -> false,
          column.createdAt -> createdTime)
      ).future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)
    f.value.get.get.size should be(2)

    val ff: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 997) }.map(AsyncLover(al)).single.future()
    }
    Await.result(ff, 5.seconds)

    ff.value.get.isSuccess should be(true)
    ff.value.get.get.isDefined should be(true)

    val asyncLover = ff.value.get.get.get
    asyncLover.id should equal(997)
    asyncLover.name should equal("Eric")
    asyncLover.rating should equal(2)
    asyncLover.isReactive should be(false)
    asyncLover.createdAt should equal(createdTime)
  }

  it should "delete in a transaction" in {

    val f: Future[Seq[AsyncQueryResult]] = NamedAsyncDB('mysql).withPool { implicit s =>
      val column = AsyncLover.column
      AsyncTx.withBuilders(
        insert.into(AsyncLover).namedValues(
          column.id -> 998,
          column.name -> "Fred",
          column.rating -> 5,
          column.isReactive -> true,
          column.createdAt -> new java.util.Date),
        delete.from(AsyncLover).where.eq(column.id, 998)
      ).future()
    }
    Await.result(f, 5.seconds)

    f.value.get.isSuccess should be(true)
    f.value.get.get.size should be(2)

    val f2: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 998) }.map(AsyncLover(al)).single.future()
    }
    Await.result(f2, 5.seconds)

    f2.value.get.isSuccess should be(true)
    f2.value.get.get.isDefined should be(false)
  }

  it should "rollback in a transaction" in {
    val f: Future[Seq[AsyncQueryResult]] = NamedAsyncDB('mysql).withPool { implicit s =>
      val column = AsyncLover.column
      AsyncTx.withSQLs(
        insert.into(AsyncLover).namedValues(
          column.id -> 999,
          column.name -> "George",
          column.rating -> 1,
          column.isReactive -> false,
          column.createdAt -> DateTime.now).toSQL,
        sql"invalid_query"
      ).future()
    }
    try {
      Await.result(f, 5.seconds)
      fail("Exception expected")
    } catch {
      case e: Exception => log.debug("expected", e)
    }

    f.value.get.isSuccess should be(false)

    val f2: Future[Option[AsyncLover]] = NamedAsyncDB('mysql).withPool { implicit s =>
      withSQL { select.from(AsyncLover as al).where.eq(al.id, 999) }.map(AsyncLover(al)).single.future()
    }
    Await.result(f2, 5.seconds)

    f2.value.get.isSuccess should be(true)
    f2.value.get.get.isDefined should be(false)
  }

}
