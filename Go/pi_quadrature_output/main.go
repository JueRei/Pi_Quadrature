// Package output contains result outputting function for the various versions of calculating π using
// quadrature.
package output

//  Copyright © 2012–2013, 2015  Russel Winder

import (
	"fmt"
	"runtime"
	"time"
)

// Out outputs a calculation result. This function is used explicitly for sequential codes and implicitly
// for parallel codes.
func Out(banner string, π float64, n int32, δt time.Duration) {
	fmt.Printf("======================== %s\n", banner)
	fmt.Printf("\tπ = %.18f\n", π)
	fmt.Printf("\titeration count = %d\n", n)
	fmt.Printf("\telapse time = %v\n", δt)
}

// OutN outputs a calculation result from a parallel code using many tasks.
func OutN(banner string, π float64, n int32, δt time.Duration, numberOfTasks int) {
	Out(banner+fmt.Sprintf(", number of tasks: %d", numberOfTasks), π, n, δt)
	fmt.Printf("\tnumber of processors = %d\n", runtime.NumCPU())
}
