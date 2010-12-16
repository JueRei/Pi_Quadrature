/*
 *  Calculation of Pi using quadrature realized with an approached based on using parallel map from
 *  Functional Java.
 *
 *  Copyright © 2010 Russel Winder
 */

import fj.F ;
import fj.F2 ;
import fj.Unit ;
import fj.data.Array ;
import fj.control.parallel.ParModule ;
import fj.control.parallel.Strategy ;

import java.util.concurrent.Executors ;

public class Pi_Java_FunctionalJava_ParMap {
  private static void execute ( final int numberOfTasks ) {
    final long n = 1000000000l ;
    final double delta = 1.0 / n ;
    final long startTimeNanos = System.nanoTime ( ) ;
    final long sliceSize = n / numberOfTasks ;
    final Array<Integer> inputData = Array.range ( 0 , numberOfTasks ) ;
    final F<Integer,Double> sliceCalculator = new F<Integer,Double> ( ) {
      @Override public Double f ( final Integer taskId ) {
        final long start = 1 + taskId * sliceSize ;
        final long end = ( taskId + 1 ) * sliceSize ;
        double sum = 0.0 ;
        for ( long i = start ; i <= end ; ++i ) {
          final double x = ( i - 0.5 ) * delta ;
          sum += 1.0 / ( 1.0 + x * x ) ;
        }
        return sum ;
      }
    } ;
    final F2<Double,Double,Double> add = new F2<Double,Double,Double> ( ) {
      @Override public Double f ( final Double a , final Double b ) { return a + b ; }
    } ;
    //final Strategy<Unit> strategy = Strategy.executorStrategy ( Executors.newCachedThreadPool ( ) ) ;
    final Strategy<Unit> strategy = Strategy.simpleThreadStrategy ( ) ;
    final double pi = 4.0 * ParModule.parModule ( strategy ).parMap ( inputData , sliceCalculator ).claim ( ).foldLeft ( add , 0.0 ) * delta ;
    final double elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9 ;
    System.out.println ( "==== Java FunctionalJava ParMap pi = " + pi ) ;
    System.out.println ( "==== Java FunctionalJava ParMap iteration count = " + n ) ;
    System.out.println ( "==== Java FunctionalJava ParMap elapse = " + elapseTime ) ;
    System.out.println ( "==== Java FunctionalJava ParMap processor count = " + Runtime.getRuntime ( ).availableProcessors ( ) ) ;
    System.out.println ( "==== Java FunctionalJava ParMap thread count = " + numberOfTasks ) ;
  }
  public static void main ( final String[] args ) {
    Pi_Java_FunctionalJava_ParMap.execute ( 1 ) ;
    System.out.println ( ) ;
    Pi_Java_FunctionalJava_ParMap.execute ( 2 ) ;
    System.out.println ( ) ;
    Pi_Java_FunctionalJava_ParMap.execute ( 8 ) ;
    System.out.println ( ) ;
    Pi_Java_FunctionalJava_ParMap.execute ( 32 ) ;
  }
}
