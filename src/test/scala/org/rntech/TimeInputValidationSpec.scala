package org.rntech

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}

class TimeInputValidationSpec extends FreeSpec with Matchers with GeneratorDrivenPropertyChecks {

  "Time input validation" - {

    "should succeed for valid time input" in {
      val validTimeGen = for {
        h <- Gen.chooseNum(0, 23)
        m <- Gen.chooseNum(0, 59)
        s <- Gen.chooseNum(0, 59)
      } yield (h, m, s)

      forAll(validTimeGen) { case (h, m, s) =>
        val result = TimeParseValidator.parseTime(h, m, s)
        result shouldBe Right(Time(hours = h, minutes = m, seconds = s))
      }
    }

    "should fail for invalid hours" in {
      val invalidHoursGen = Gen.chooseNum(0, Int.MaxValue) suchThat (_ > 23)
      forAll(invalidHoursGen) { hours =>
        val result = TimeParseValidator.parseTime(hours, 0, 0)
        result shouldBe Left(InvalidTimeInput(s"$hours hours is not a valid time input"))
      }
    }

    "should fail for invalid minutes" in {
      val invalidMinutesGen = Gen.chooseNum(0, Int.MaxValue) suchThat (_ > 59)
      forAll(invalidMinutesGen) { mins =>
        val result = TimeParseValidator.parseTime(0, minutes = mins, 0)
        result shouldBe Left(InvalidTimeInput(s"$mins minutes is not a valid time input"))
      }
    }


    "should fail for invalid seconds" in {
      val invalidSecondsGen = Gen.chooseNum(0, Int.MaxValue) suchThat (_ > 59)
      forAll(invalidSecondsGen) { secs =>
        val result = TimeParseValidator.parseTime(0, 0, seconds = secs)
        result shouldBe Left(InvalidTimeInput(s"$secs seconds is not a valid time input"))
      }
    }
  }
}
