package org.rntech

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}
import BerlinClockSpec._
import BerlinClock._

class BerlinClockSpec extends FreeSpec
  with Matchers
  with GeneratorDrivenPropertyChecks {

  "A Berlin Clock" - {

    "generates a time" - {

      "the seconds lamp should blink 'Off' for odd seconds" in {
        forAll(oddNumberGen) { odd =>
          val clock = generate(Time(hours = 0, minutes = 0, seconds = odd))
          clock.secondLamp shouldBe LampState(Off)
        }
      }

      "the seconds lamp should blink 'Yellow' for even seconds" in {
        forAll(evenNumberGen) { even =>
          val clock = generate(Time(hours = 0, minutes = 0, seconds = even))
          clock.secondLamp shouldBe LampState(Yellow)
        }
      }

      "the first row of lamps should turn 'Red' for each 5 hours" in {
        generate(Time(hours = 2, 0, 0)).topHour shouldBe lampState(on = 0, off = 4)
        generate(Time(hours = 6, 0, 0)).topHour shouldBe lampState(on = 1, off = 3)
        generate(Time(hours = 11, 0, 0)).topHour shouldBe lampState(on = 2, off = 2)
        generate(Time(hours = 18, 0, 0)).topHour shouldBe lampState(on = 3, off = 1)
        generate(Time(hours = 23, 0, 0)).topHour shouldBe lampState(on = 4, off = 0)
      }

      "the second row of lamps should turn 'Red' for additional hours" in {
        generate(Time(hours = 0, 0, 0)).bottomHour shouldBe lampState(on = 0, off = 4)
        generate(Time(hours = 6, 0, 0)).bottomHour shouldBe lampState(on = 1, off = 3)
        generate(Time(hours = 9, 0, 0)).bottomHour shouldBe lampState(on = 4, off = 0)
        generate(Time(hours = 12, 0, 0)).bottomHour shouldBe lampState(on = 2, off = 2)
        generate(Time(hours = 23, 0, 0)).bottomHour shouldBe lampState(on = 3, off = 1)
      }

      "the third row of lamps should turn 'Yellow' for each 5 minutes and red on quarters" in {
        generate(Time(0, minutes = 0, 0)).topMinute shouldBe lampState(colours = List.empty, off = 11)
        generate(Time(0, minutes = 5, 0)).topMinute shouldBe lampState(colours = List(Yellow), off = 10)
        generate(Time(0, minutes = 12, 0)).topMinute shouldBe lampState(colours = List(Yellow, Yellow), off = 9)
        generate(Time(0, minutes = 26, 0)).topMinute shouldBe lampState(colours = List(Yellow, Yellow, Red, Yellow, Yellow), off = 6)
      }

      "the forth row of lamps should turn 'Yellow' for each additional minute" in {
        generate(Time(0, minutes = 0, 0)).bottomMinute shouldBe lampState(colour = Yellow, on = 0, off = 4)
        generate(Time(0, minutes = 6, 0)).bottomMinute shouldBe lampState(colour = Yellow, on = 1, off = 3)
        generate(Time(0, minutes = 27, 0)).bottomMinute shouldBe lampState(colour = Yellow, on = 2, off = 2)
        generate(Time(0, minutes = 48, 0)).bottomMinute shouldBe lampState(colour = Yellow, on = 3, off = 1)
        generate(Time(0, minutes = 59, 0)).bottomMinute shouldBe lampState(colour = Yellow, on = 4, off = 0)
      }
    }

    "renders a time" - {
      "the berlin clock should be rendered when called with a valid time" in {
        val output = renderBerlinClock(hours = 13, minutes = 21, seconds = 3)
        output shouldBe """|Off|
                          |Red | Red | Off | Off
                          |Red | Red | Red | Off
                          |Yellow | Yellow | Red | Yellow | Off | Off | Off | Off | Off | Off | Off
                          |Yellow | Off | Off | Off""".stripMargin
      }

      "the berlin clock should throw an error when called with an invalid time" in {
        assertThrows[InvalidTimeInput] {
          renderBerlinClock(hours = -213, minutes = 0, seconds = 31)
        }
      }
    }
  }

  private def lampState(colour: LampColour = Red, on: Int, off: Int) = List.fill(on)(LampState(colour)) ++ List.fill(off)(LampState(Off))

  private def lampState(colours: Seq[LampColour], off: Int) = colours.map(LampState) ++ List.fill(off)(LampState(Off))

}

object BerlinClockSpec {
  private val oddNumberGen = Gen.chooseNum(1, 59) suchThat (_ % 2 != 0)
  private val evenNumberGen = Gen.chooseNum(0, 59) suchThat (_ % 2 == 0)
}