#! /usr/bin/env python
# -*- coding:utf-8; -*-

#  Calculation of π using quadrature. Use the Parallel Python package.
#
#  Copyright © 2008–2013 Russel Winder

from output import out
from pp import Server
from time import time


def processSlice(id, sliceSize, delta):
    sum = 0.0
    for i in xrange(1 + id * sliceSize, (id + 1) * sliceSize + 1):
        x = (i - 0.5) * delta
        sum += 1.0 / (1.0 + x * x)
    return sum


def execute(processCount):
    n = 10000000  # 100 times fewer than C due to speed issues.
    delta = 1.0 / n
    startTime = time()
    sliceSize = n / processCount
    server = Server(secret='blahblahblah')
    jobs = [server.submit(processSlice, (i, sliceSize, delta)) for i in xrange(0, processCount)]
    pi = 4.0 * delta * sum(job() for job in jobs)
    elapseTime = time() - startTime
    out(__file__, pi, n, elapseTime, processCount)
    server.print_stats()


if __name__ == '__main__':
    execute(1)
    execute(2)
    execute(8)
    execute(32)
