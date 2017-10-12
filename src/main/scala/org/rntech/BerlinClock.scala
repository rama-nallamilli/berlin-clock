package org.rntech

sealed trait LampColour

case object Yellow extends LampColour

case object Red extends LampColour

case object Off extends LampColour

case class BerlinClockView(topHour: List[LampColour],
                           bottomHour: List[LampColour],
                           topMinute: List[LampColour],
                           bottomMinute: List[LampColour],
                           secondLamp: LampColour) {

  override def toString: String = {
    val renderLine = (l: List[LampColour]) =>  l.mkString(" | ")

    s"""${secondLamp}
       |${renderLine(topHour)}
       |${renderLine(bottomHour)}
       |${renderLine(topMinute)}
       |${renderLine(bottomMinute)}""".stripMargin
  }
}

object BerlinClock {

  private val getMinuteLampColour = (index: Int) =>
    if ((index + 1) % 3 == 0) //red for multiples of 3, excluding 0
      Red
    else
      Yellow

  private val allLampsOn = BerlinClockView(
    topHour = List.fill(4)(Red),
    bottomHour = List.fill(4)(Red),
    topMinute = List.tabulate(11)(i => getMinuteLampColour(i)),
    bottomMinute = List.fill(4)(Yellow),
    secondLamp = Yellow
  )

  def renderBerlinClock(hours: Int, minutes: Int, seconds: Int): String = {
    TimeParseValidator.parseTime(hours, minutes, seconds) match {
      case Right(time) => generate(time).toString
      case Left(error) => throw error
    }
  }

  def generate(time: Time): BerlinClockView = {

    def switchLamps(numberToKeepOn: Int, lamps: List[LampColour]) =
      lamps.zipWithIndex.map {
        case (lamp, index) if index + 1 <= numberToKeepOn => lamp
        case _@(lamp, _) => Off
      }

    BerlinClockView(
      topHour = switchLamps(numberToKeepOn = time.hours / 5, lamps = allLampsOn.topHour),
      bottomHour = switchLamps(numberToKeepOn = time.hours % 5, lamps = allLampsOn.bottomHour),
      topMinute = switchLamps(numberToKeepOn = time.minutes / 5, lamps = allLampsOn.topMinute),
      bottomMinute = switchLamps(numberToKeepOn = time.minutes % 5, lamps = allLampsOn.bottomMinute),
      secondLamp = if (time.seconds % 2 == 0) Yellow else Off
    )
  }
}


