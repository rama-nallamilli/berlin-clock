package org.rntech

case class Time(hours: Int, minutes: Int, seconds: Int)

sealed trait ValidationError extends RuntimeException {
  def msg: String
}

case class InvalidTimeInput(msg: String) extends ValidationError

object TimeParseValidator {
  type ValidatedTime = Int => Either[ValidationError, Int]

  private val isValidHours: ValidatedTime = {
    case hours if hours >= 0 && hours < 24 => Right(hours)
    case _@invalidHours => Left(InvalidTimeInput(s"$invalidHours hours is not a valid time input"))
  }

  private val isValidMinutes: ValidatedTime = {
    case mins if mins >= 0 && mins < 60 => Right(mins)
    case _@invalidMins => Left(InvalidTimeInput(s"$invalidMins minutes is not a valid time input"))
  }

  private val isValidSeconds: ValidatedTime = {
    case seconds if seconds >= 0 && seconds < 60 => Right(seconds)
    case _@invalidSecs => Left(InvalidTimeInput(s"$invalidSecs seconds is not a valid time input"))
  }

  def parseTime(hours: Int, minutes: Int, seconds: Int): Either[ValidationError, Time] = for {
    h <- isValidHours(hours)
    m <- isValidMinutes(minutes)
    s <- isValidSeconds(seconds)
  } yield Time(hours = h, minutes = m, seconds = s)

}
