function wrapWithTag(tag, idValue, classValue, contentArray) {
  var classArg = classValue == "" ? "" : "class=\"" + classValue + "\""
  var idArg = idValue == "" ? "" : "id=\"" + idValue + "\""
  var openTag = "<" + tag + " " + classArg + " " + idArg + ">"
  var content = ""
  for(i = 0; i < contentArray.length; ++i) {
    content += contentArray[i]
  }
  var closeTag = "</" + tag + ">"
  return openTag + content + closeTag
}

function buildAwardItemView(award) {
  return wrapWithTag("div", "", "award-item row cells2",
    [
      wrapWithTag("div", "", "cell padding20",
        [wrapWithTag("div", "", "align-left", [toHumanReadableDate(award.when)])]),
      wrapWithTag("div", "", "cell padding20",
        [wrapWithTag("div", "", "align-right", [award.amount])])
    ])
}