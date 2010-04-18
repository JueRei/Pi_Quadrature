/*
 *  A C program to calculate Pi using quadrature as a threads-based algorithm.
 *
 *  Copyright © 2009 Russel Winder
 */

#include <stdio.h>
#include <pthread.h>
#include "microsecondTime.h"

long double sum ;
pthread_mutex_t sumMutex ;

typedef struct CalculationParameters {
  long start ;
  long end ;
  long double delta ;
} CalculationParameters ;

void * partialSum ( void *const arg  ) {
  long double localSum = 0.0 ;
  long i ;
  for ( i = ( (CalculationParameters *const) arg )->start ; i <= ( (CalculationParameters *const) arg )->end ; ++i ) {
    const long double x = ( i - 0.5 ) * ( (CalculationParameters *const) arg )->delta ;
    localSum += 1.0 / ( 1.0 + x * x ) ;
  }
  pthread_mutex_lock ( &sumMutex ) ;
  sum += localSum ;
  pthread_mutex_unlock ( &sumMutex ) ;
  pthread_exit ( (void *) 0 ) ;
  return 0 ;
}

void execute ( const int numberOfThreads ) {
  const long n = 1000000000l ;
  const long double delta = 1.0 / n ;
  const long long startTimeMicros = microsecondTime ( ) ;
  const long sliceSize  = n / numberOfThreads ;
  pthread_mutex_init ( &sumMutex , NULL ) ;
  pthread_attr_t attributes ;
  pthread_attr_init ( &attributes ) ;
  pthread_attr_setdetachstate ( &attributes , PTHREAD_CREATE_JOINABLE ) ;
  sum = 0.0 ; // Only one thread at this point so safe to access without locking.
  pthread_t threads[numberOfThreads] ;
  CalculationParameters parameters[numberOfThreads] ;
  int i ;
  for ( i = 0 ; i < numberOfThreads ; ++i ) {
    parameters[i].start = 1 + i * sliceSize ;
    parameters[i].end = ( i + 1 ) * sliceSize ;
    parameters[i].delta = delta ;
    pthread_create ( &threads[i] , &attributes , partialSum , (void *) &parameters[i] ) ;
  }
  pthread_attr_destroy ( &attributes ) ;
  int status ;
  for ( i = 0 ; i < numberOfThreads ; ++i ) { pthread_join ( threads[i] , (void **) &status ) ; }
  const long double pi = 4.0 * sum * delta ;
  const long double elapseTime = ( microsecondTime ( ) - startTimeMicros ) / 1e6 ;
  printf ( "==== C PThread parameters pi = %.25Lf\n" , pi ) ;
  printf ( "==== C PThread parameters iteration count = %ld\n" ,  n ) ;
  printf ( "==== C PThread parameters elapse = %Lf\n" , elapseTime ) ;
  printf ( "==== C PThread parameters thread count = %d\n" , numberOfThreads ) ;
}

int main ( ) {
  execute ( 1 ) ;
  printf ( "\n" ) ;
  execute ( 2 ) ;
  printf ( "\n" ) ;
  execute ( 8 ) ;
  printf ( "\n" ) ;
  execute ( 32 ) ;
  return 0 ;
}
