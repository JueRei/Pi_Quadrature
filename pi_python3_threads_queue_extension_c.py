#! /usr/bin/env python3

#  Calculation of Pi using quadrature.  Using threads and extensions.  ctypes is careful to release the GIL
#  whilst C code is running so we get real parallelism. We use a Queue as the way of receiving results since
#  that has the necessary guarantees to be thread-safe.
#
#  Copyright © 2008–2012 Russel Winder

import time
import threading
import queue
import ctypes

def processSlice ( id , sliceSize , delta , results ) :
    results.put ( processSliceModule.processSlice ( id , sliceSize , delta ) )

def execute ( threadCount ) :
    n = 1000000000
    delta = 1.0 / n
    startTime = time.time ( )
    sliceSize = n // threadCount
    results = queue.Queue ( threadCount )
    threads = [ threading.Thread ( target = processSlice , args = ( i , sliceSize , delta , results ) ) for i in range ( 0 , threadCount ) ]
    for thread in threads : thread.start ( )
    for thread in threads : thread.join ( )
    pi =  4.0 * delta * sum ( [ results.get ( ) for i in range ( threadCount ) ] )
    elapseTime = time.time ( ) - startTime
    print ( "==== Python Threads C Extension pi = " + str ( pi ) )
    print ( "==== Python Threads C Extension iteration count = " + str ( n ) )
    print ( "==== Python Threads C Extension elapse = " + str ( elapseTime ) )
    print ( "==== Python Threads C Extension thread count = " + str ( threadCount ) )

if __name__ == '__main__' :
    processSliceModule = ctypes.cdll.LoadLibrary ( 'processSlice_c.so' )
    processSliceModule.processSlice.argtypes = [ ctypes.c_int , ctypes.c_int , ctypes.c_double ]
    processSliceModule.processSlice.restype = ctypes.c_double
    execute ( 1 )
    print ( )
    execute ( 2 )
    print ( )
    execute ( 8 )
    print ( )
    execute ( 32 )
