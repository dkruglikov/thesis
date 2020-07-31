library(plumber)

r <- plumb("c:/Users/dkruglikov/Documents/NetBeansProjects/thesis/R/xts.R")
r$run(port=12080)
