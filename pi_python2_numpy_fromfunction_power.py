#! /usr/bin/env python
# -*- mode:python; coding:utf-8; -*-

#  Calculation of Pi using quadrature. Sequential algorithm. Use NumPy.
#
#  Copyright © 2008–2012 Russel Winder

from numpy import float , fromfunction
from output import out
from time import time

if __name__ == '__main__' :
    n = 100000000 # 10 times fewer than C due to speed issues.
    delta = 1.0 / n
    startTime = time ( )
    pi = 4.0 * delta * fromfunction ( lambda i : 1.0 / ( 1.0 + ( ( i - 0.5 ) * delta ) ** 2 ) , ( n , ) , dtype = float ).sum ( )
    elapseTime = time ( ) - startTime
    out ( __file__ , pi , n , elapseTime )
