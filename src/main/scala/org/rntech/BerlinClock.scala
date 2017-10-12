package org.rntech


sealed trait LampColour

case object Yellow extends LampColour

case object Red extends LampColour

case object Off extends LampColour

case class LampState(colour: LampColour)

case class BerlinClockView(topHour: List[LampState],
                           bottomHour: List[LampState],
                           topMinute: List[LampState],
                           bottomMinute: List[LampState],
                           secondLamp: LampState) {

  override def toString: String = {
    val renderLine = (l: List[LampState]) =>  l.map(_.colour).mkString(" | ")

    s"""|${secondLamp.colour}|
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
    topHour = List.fill(4)(LampState(Red)),
    bottomHour = List.fill(4)(LampState(Red)),
    topMinute = List.tabulate(11)(i => LampState(getMinuteLampColour(i))),
    bottomMinute = List.fill(4)(LampState(Yellow)),
    secondLamp = LampState(Yellow)
  )

  def renderBerlinClock(hours: Int, minutes: Int, seconds: Int): String = {
    TimeParseValidator.parseTime(hours, minutes, seconds) match {
      case Right(time) => generate(time).toString
      case Left(error) =>
        // would usually keep Either[E,T] up to application boundaries, throw here to keep interface requested
        throw error
    }
  }

  def generate(time: Time): BerlinClockView = {

    def switchLamps(numberToKeepOn: Int, lamps: List[LampState]) =
      lamps.zipWithIndex.map {
        case (lamp, index) if index + 1 <= numberToKeepOn => lamp
        case _@(lamp, _) => lamp.copy(colour = Off)
      }

    BerlinClockView(
      topHour = switchLamps(numberToKeepOn = time.hours / 5, lamps = allLampsOn.topHour),
      bottomHour = switchLamps(numberToKeepOn = time.hours % 5, lamps = allLampsOn.bottomHour),
      topMinute = switchLamps(numberToKeepOn = time.minutes / 5, lamps = allLampsOn.topMinute),
      bottomMinute = switchLamps(numberToKeepOn = time.minutes % 5, lamps = allLampsOn.bottomMinute),
      secondLamp = LampState(colour = if (time.seconds % 2 == 0) Yellow else Off)
    )
  }
}


