--  Haskell implementation of Pi by Quadrature.  This implementation tries to parallelize.
--
--  Copyright © 2009-10 Russel Winder

module Main where

import Data.Time.Clock ( getCurrentTime , diffUTCTime )
import GHC.Conc ( numCapabilities )
import Control.Parallel ( par )

piIter :: Int -> Int -> Double -> Double -> Double
piIter  n to delta accumulator
    | n > to = 4.0 * accumulator * delta
    | otherwise = piIter ( n + 1 ) to delta ( accumulator + 4.0 / ( 1.0 + x * x ) )        
    where
      x = ( ( fromIntegral n ) - 0.5 ) * delta

piQuadSlice :: Double -> Int -> Int -> Double
piQuadSlice delta sliceSize index = piIter start end delta 0.0
    where
      start = 1 +  index * sliceSize
      end =  ( index + 1 ) * sliceSize

parallelMap :: ( a -> b ) -> [a] -> [b]
parallelMap f ( x : xs ) =
    let r = f x
    in r `par` r : parallelMap f xs
parallelMap _ _  = [ ]

execute :: Int -> IO ( )
execute numberOfSlices = do
  let n = 1000000000
  let delta = 1.0 / ( fromIntegral n )
  startTime <- getCurrentTime
  let sliceSize = n `div` numberOfSlices
  let pi = sum ( parallelMap ( piQuadSlice delta sliceSize ) [ 0 .. ( numberOfSlices - 1 ) ] )
  --  Don't get the time here since nothing has actually been computed as yet since pi has not been used.
  print ( "==== Haskell Parallel pi = " ++ show pi )
  print ( "==== Haskell Parallel iteration count = " ++ show n )
  endTime <- getCurrentTime
  print ( "==== Haskell Parallel elapse time = " ++ show ( diffUTCTime endTime startTime ) )
  print ( "==== Haskell Parallel thread count = " ++ show numberOfSlices )
  print ( "==== Haskell Parallel processor count = " ++ show numCapabilities )

main :: IO ( )
main = do
  execute 1
  print ""
  execute 2
  print ""
  execute 8
  print ""
  execute 32
