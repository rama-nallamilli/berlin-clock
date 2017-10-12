package org.rntech.runner

import java.time.LocalTime

import org.rntech.BerlinClock

import scala.util.Try

object Main extends App {

  val numIterations = Try(args(0).toInt).getOrElse(10)
  val sleepBetweenIterations = Try(args(1).toLong).getOrElse(3000L)

  (1 to numIterations).foreach { _ =>

    Thread sleep sleepBetweenIterations

    val now = LocalTime.now()
    Console.println(s"TIME: $now")

    val berlinClock = BerlinClock.renderBerlinClock(hours = now.getHour,
        minutes = now.getMinute,
        seconds = now.getSecond)

    Console.println(s"BERLIN CLOCK: \n $berlinClock \n")
  }
}
