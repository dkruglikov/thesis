library(plumber)
r <- plumb("c:/Users/dkruglikov/Documents/NetBeansProjects/thesis/R/forecasts.R")
r$run(port=12080)
