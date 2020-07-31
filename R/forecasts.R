library(forecast)

createTimeSerie <- function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
}

#' @put /average
function(req) {
  serie <- createTimeSerie(req)
  forecastResult <- meanf(serie, 6)
  print("AVERAGE")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /naive
function(req) {
  serie <- createTimeSerie(req)
  forecastResult <- naive(serie, 6)
  print("NAIVE")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /seasonal-naive
function(req) {
  serie <- createTimeSerie(req)
  forecastResult <- naive(serie, 6)
  print("SEASONAL NAIVE")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /drift
function(req) {
  serie <- createTimeSerie(req)
  forecastResult <- rwf(serie, 6, drift=TRUE)
  print("DRIFT")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /croston
function(req) {
  serie <- createTimeSerie(req)
  forecastResult <- croston(serie, 6)
  print("CROSTON")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /exponential-smooting
function(req) {
  serie <- createTimeSerie(req)
  forecastResult <- ses(serie, 6)
  print("EXPONENTIAL SMOOTING")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /neural-network
function(req) {
  serie <- createTimeSerie(req)
  newralNetwork <- nnetar(serie, decay=0.5, maxit=250)
  forecastResult <- forecast(newralNetwork, 6)
  print("NEURAL NETWORK")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /holt-winters
function(req) {
  serie <- createTimeSerie(req)
  holtWintersModel <- HoltWinters(serie, gamma=FALSE)
  forecastResult <- forecast(holtWintersModel, 6)
  print("HOLT-WINTERS")
  print(forecastResult)
  return(forecastResult$mean)
}

#' @put /structural
function(req) {
  serie <- createTimeSerie(req)
  structuralModel <- StructTS(serie, type="trend")
  forecastResult <- forecast(structuralModel, 6)
  print("STRUCTURAL")
  print(forecastResult)
  return(forecastResult$mean)
}

