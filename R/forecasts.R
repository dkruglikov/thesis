library(forecast)

#' @put /average
function(req) {
  plainValues <- as.numeric(unlist(strsplit(req$postBody, " ")))
  serie <- ts(plainValues, start=0)
  return(meanf(serie, 2)$mean)
}
