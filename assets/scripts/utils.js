function toHumanReadableDate(timestampMs) {
  var date = new Date(timestampMs);
  var year = date.getFullYear();
  var month = "0" + (date.getMonth() + 1);
  var day = "0" + date.getDate();
  var formattedTime = day.substr(-2) + '.' + month.substr(-2) + "." + year;
  return formattedTime;
}