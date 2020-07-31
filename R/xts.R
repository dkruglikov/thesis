library(xts)
library(forecast)

#* @param timestamps
#* @param values
#* @put /xts/arima
function(timestamps, values) {
  posixTimestamps <- as.POSIXct(unlist(strsplit(timestamps, split = ";")), tz = "UTC")
  numericValues <- as.numeric(unlist(strsplit(values, split = ";")))
  xtsData <- xts(numericValues, order.by = posixTimestamps)
  arimaData <- auto.arima(xtsData, seasonal = FALSE)
  forecastResult <- forecast(arimaData, h=24)
  print("ARIMA")
  print(forecastResult)
  plot(forecastResult)
  return(forecastResult$mean)
}

#* @param timestamps
#* @param values
#* @put /xts/neural
function(timestamps, values) {
  posixTimestamps <- as.POSIXct(unlist(strsplit(timestamps, split = ";")), tz = "UTC")
  numericValues <- as.numeric(unlist(strsplit(values, split = ";")))
  xtsData <- xts(numericValues, order.by = posixTimestamps)
  neuralNetwork <- nnetar(xtsData, decay=0.5, maxit=25000)
  forecastResult <- forecast(neuralNetwork, h=24)
  print("NEURAL NETWORK")
  print(forecastResult)
  plot(forecastResult)
  return(forecastResult$mean)
}
