#! /usr/bin/env python
# -*- coding:utf-8; -*-

#  Calculation of π using quadrature. Uses threads but this gives no parallelism because of the GIL.
#  This version uses a global thread-safe queue for the children to send results back to the parent.
#
#  Copyright © 2008–2013 Russel Winder

from output import out
from Queue import Queue
from threading import Thread
from time import time

def processSlice(id, sliceSize, delta):
    sum = 0.0
    for i in xrange(1 + id * sliceSize, (id + 1) * sliceSize + 1):
        x = (i - 0.5) * delta
        sum += 1.0 / (1.0 + x * x)
    results.put(sum)

def execute(threadCount):
    n = 10000000  # 100 times fewer than C due to speed issues.
    delta = 1.0 / n
    startTime = time()
    sliceSize = n / threadCount
    global results
    results = Queue(threadCount)
    threads = [Thread(target=processSlice, args=(i, sliceSize, delta)) for i in xrange(0, threadCount)]
    for thread in threads:
        thread.start()
    pi = 4.0 * delta * sum(results.get() for i in xrange(threadCount))
    elapseTime = time() - startTime
    out(__file__, pi, n, elapseTime, threadCount)

if __name__ == '__main__':
    execute(1)
    execute(2)
    execute(8)
    execute(32)
