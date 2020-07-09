library(forecast)

#' @put /average
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  return(meanf(serie, 6)$mean)
}

#' @put /naive
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  return(naive(serie, 6)$mean)
}

#' @put /seasonal-naive
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  return(snaive(serie, 6)$mean)
}

#' @put /drift
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  return(rwf(serie, 6, drift=TRUE)$mean)
}

#' @put /croston
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  return(croston(serie, 6)$mean)
}

#' @put /exponential-smooting
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  return(ses(serie, 6)$mean)
}

#' @put /neural-network
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  newralNetwork <- nnetar(serie, decay=0.5, maxit=250)
  return(forecast(newralNetwork, 6)$mean)
}

#' @put /holt-winters
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  holtWintersModel <- HoltWinters(serie, gamma=FALSE)
  return(forecast(holtWintersModel, 6)$mean)
}

#' @put /structural
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  structuralModel <- StructTS(serie, type="trend")
  return(forecast(structuralModel, 6)$mean)
}

