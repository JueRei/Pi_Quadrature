/*
 *  Parallel implementation of π by quadrature using Rayon's parallel iterator in an unbatched way.
 *
 *  Copyright © 2016–2017, 2019  Russel Winder
 */

use rayon::iter::IntoParallelIterator;
use rayon::iter::ParallelIterator;

use time::precise_time_s;
use output::output;

fn main() {
    let n = 1_000_000_000u64;
    let delta = 1.0 / n as f64;
    let start_time = precise_time_s();
    let total: f64 = (0..n as u32).into_par_iter().map(move |i| {
    	let x = (i as f64 - 0.5) * delta;
    	1.0 / (1.0 + x * x)
    }).sum();
    let pi = 4.0 * delta * total;
    let elapse_time = precise_time_s() - start_time;
    output("Parallel Rayon Iter Notbatched", pi, n, elapse_time)
}
