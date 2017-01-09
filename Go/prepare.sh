#!/bin/sh

export GOPATH=`pwd`
unset GOBIN

for d in output sequential goroutines_singleChannel goroutines_multipleChannels
do
    go install -compiler gccgo pi_quadrature_$d
done
