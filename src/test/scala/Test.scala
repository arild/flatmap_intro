import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.Tables.Table
import org.scalatrain._

class Problems extends FunSpec with ShouldMatchers with BeforeAndAfter {
  describe("Time") {
    it("minus") {
      val a: Time = Time(2, 20)
      val b: Time = Time(1, 0)
      a.minus(b) should be(80)
      a - b should be(80)
      b - a should be(-80)
    }
    it("from minutes") {
      // Compares members of instances since Time is case class
      Time.fromMinutes(150) should be(Time(2, 30))
    }
    it("illegal parameters") {
      val invalidCombos = Table(("hours", "minutes"),
        (-1, 0), (0, -1), (-1, -1), (24, 0), (0, 60), (24, 60))

      forAll(invalidCombos) { (hours: Int, minutes: Int) =>
        evaluating {
          Time(hours, minutes)
        } should produce[IllegalArgumentException]
      }
    }
  }

  val schedule = Seq((Time(), Station("Oslo")), (Time(), Station("Hamar")), (Time(), Station("Trondheim")))
  val schedule2 = Seq((Time(), Station("Stavanger")), (Time(), Station("Bergen")), (Time(), Station("Oslo")))

  val train = Train(schedule)
  val train2 = Train(schedule2)

  describe("Train") {
    evaluating {
      Train(Seq((Time(), Station(""))))
    } should produce[IllegalArgumentException]
    train.stations should be(Seq(Station("Oslo"), Station("Hamar"), Station("Trondheim")))
  }

  describe("JourneyPlanner") {
    val planner = JourneyPlanner(Set(train, train2))

    it("unique stations") {
      planner.stations.size should be(5) // "Oslo" should only occur once due to Set data structure
    }
    it("trains at station") {
      planner.trainsAt(Station("Hamar")) should be(Set(train))
      planner.trainsAt(Station("Oslo")) should be(Set(train2, train))
    }
    it("trains stop at") {
      val tuple = (Time(), train)
      val tuple2 = (Time(), train2)
      planner.stopsAt(Station("Hamar")) should be(Set(tuple))
      planner.stopsAt(Station("Oslo")) should be(Set(tuple, tuple2))
    }
    it("short trip if no more than 2 hops away") {
      planner.isShortTrip(Station("Oslo"), Station("Hamar")) should be(true)
      planner.isShortTrip(Station("Oslo"), Station("Trondheim")) should be(true)
      planner.isShortTrip(Station("Oslo"), Station("Bergen")) should be(false)
    }
  }
}