# π by Quadrature

This directory contains various implementations in various programming languages of the embarrassingly
parallel problem of calculating an approximation of the value of π using quadrature. (Quadrature in this
case is the process of finding the area under a curve using the approximation of filling the area with
rectangles and summing the areas of the rectangles.)

The integral equation:
```tex
\fraction{\pi}{4} = \int_{0}^{1} \fraction{1}{1 + x^2} dx
```
leads to the following summation as an approximation:
```tex
\pi \approx \fraction{4}{n} \sum_{i = 1}^{n} \fraction{1}{1 + (\fraction{i - 0.5}{n})^2}
```
This summation can be partitioned into partial sums that are then summed.  This is an embarrassingly
parallel, data parallel problem that can check scalability. As well as actual data parallel solutions there
are some simple scatter/gather variants to highlight the use of other tools for concurrency and
parallelism. In all cases, if the speed of execution does not increase linearly with the number of
processors available, there is an issue to investigate.

Various different idioms and techniques in the various languages are tried as part of showing which
languages are better than others for this problem.  Also the examples investigate the properties of, and
indeed idioms and techniques appropriate to, the various languages.

A point on JVM "warm up" (i.e. the JIT).  If loop variables are longs then there is a noticeable "warm up"
effect and two executions prior to any timing executions are needed to have the code JITed.  Using int loop
variables there appears to be no such affect – in fact there is, but it is a relatively small effect and can
effectively be ignored.
