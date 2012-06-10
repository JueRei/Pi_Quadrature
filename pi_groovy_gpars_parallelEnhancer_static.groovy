#! /usr/bin/env groovy

/*
 *  Calculation of Pi using quadrature realized with a fork/join approach with GPars ParallelEnhancer to
 *  partition the problem and hence harness all processors available to the JVM.
 *
 *  Copyright © 2010–2012 Russel Winder
 */

import groovy.transform.CompileStatic

import groovyx.gpars.ParallelEnhancer

void execute ( final int numberOfTasks ) {
  final int n = 1000000000i
  final double delta = 1.0d / n
  final startTimeNanos = System.nanoTime ( )
  final int sliceSize = n / numberOfTasks
  final items = 0i ..< numberOfTasks
  ParallelEnhancer.enhanceInstance ( items )
  final pi = 4.0d * delta * items.collectParallel { taskId ->
    PartialSum.compute ( taskId , sliceSize , delta )
  }.sumParallel ( )
  final elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9
  Output.out ( 'Groovy GPars ParallelEnhancer Static' , pi , n , elapseTime , numberOfTasks )
}

execute ( 1 )
execute ( 2 )
execute ( 8 )
execute ( 32 )
