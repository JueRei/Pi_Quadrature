/*
 *  Parallel implementation of π by quadrature using Rayon's parallel iterator with a batched approach.
 *
 *  Copyright © 2016–2017, 2019  Russel Winder
 */

use rayon::iter::IntoParallelIterator;
use rayon::iter::ParallelIterator;

use time::precise_time_s;
use output::output_n;

fn execute(number_of_threads: u64) {
    let n = 1_000_000_000u64;
    let delta = 1.0 / n as f64;
    let start_time = precise_time_s();
    let slice_size = n / number_of_threads;
    let total: f64 = (0..number_of_threads).into_par_iter().map(move |id| {
   		((1 + id * slice_size) .. ((id + 1) * slice_size)).fold(0.0, |acc, i| {
    	    let x = (i as f64 - 0.5) * delta;
    	    acc + 1.0 / (1.0 + x * x)
    	})
    }).sum();
    let pi = 4.0 * delta * total;
    let elapse_time = precise_time_s() - start_time;
    output_n("Parallel Rayon Iter Batched", pi, n, elapse_time, number_of_threads)
}

fn main() {
    execute(1);
    execute(2);
    execute(8);
    execute(32)
}
