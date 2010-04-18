/*
 *  Calculation of Pi using quadrature realized with a fork/join approach using an actor system.
 * 
 *  Copyright © 2009-10 Russel Winder
 */

import scala.actors.Actor

object Pi_Scala_Actors extends Application {
  def execute ( numberOfThreads : Int ) {
    val n = 1000000000l
    val delta = 1.0 / n
    val startTimeNanos = System.nanoTime
    val sliceSize = n / numberOfThreads
    val calculators = new Array[Actor] ( numberOfThreads )
    val accumulator = Actor.actor {
      var sum = 0.0
      calculators.foreach ( calculator => Actor.receive { case d => sum += d.asInstanceOf[Double] } )
      val pi = 4.0 * sum * delta
      val elapseTime = ( System.nanoTime - startTimeNanos ) / 1e9
      println ( "==== Scala Actors pi = " + pi )
      println ( "==== Scala Actors iteration count = " + n )
      println ( "==== Scala Actors elapse = " + elapseTime )
      println ( "==== Scala Actors processor count = " + Runtime.getRuntime.availableProcessors )
      println ( "==== Scala Actors threads count = " + numberOfThreads )
      sequencer ! 0
    }
    for ( index <- 0 until calculators.size ) {
      calculators ( index ) = Actor.actor {
        val start = 1 + index * sliceSize
        val end = ( index + 1 ) * sliceSize 
        var i = start
        var sum = 0.0
        while ( i <=  end ) {
          val x = ( i - 0.5 ) * delta
          sum += 1.0 / ( 1.0 + x * x )
          i += 1
        }
        accumulator ! sum
      }
    }
  }
  val sequencer = Actor.actor {
    execute ( 1 )
    Actor.receive { case d => }
    println
    execute ( 2 )
    Actor.receive { case d => }
    println
    execute ( 8 )
    Actor.receive { case d => }
    println
    execute ( 32 )
  }
}
