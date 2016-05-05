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

function buildAwardView(award) {
  /*return wrapWithTag("div", "", "grid condensed",
    [*/
  return    wrapWithTag("div", "", "award-item row cells2",
        [
          wrapWithTag("div", "", "cell padding20",
            [wrapWithTag("div", "", "item-info align-left", [toHumanReadableDate(award.when)])]),
          wrapWithTag("div", "", "cell padding20",
            [wrapWithTag("div", "", "item-info align-right", [award.amount])]),
          wrapWithTag("div", "", "",
            [wrapWithTag("div", "", "item-type align-right", ["award"])])
        ])
}

function buildOutOfOfficeView(outOfOffice) {
  return wrapWithTag("div", outOfOffice.id, "out-of-office-item row cells5",
      [
        /*wrapWithTag("div", "", "align-right",
          [wrapWithTag("span", "", "",
            [wrapWithTag("i", "", "fa fa-times fa-2x", [""])])]),*/
        wrapWithTag("div", "", "cell colspan2 padding20",
          [wrapWithTag("div", "", "item-info align-left",
            [
              toHumanReadableTime(outOfOffice.when),
              " <br/> ",
              toHumanReadableTime(outOfOffice.when + outOfOffice.duration)
            ])]),
        wrapWithTag("div", "", "cell colspan3 padding20",
          [wrapWithTag("div", "", "item-info align-left", [outOfOffice.reason])]),
        wrapWithTag("div", "", "",
          [wrapWithTag("div", "", "item-type align-right",
            [
              wrapWithTag("span", "", "item-type v-align-bottom", ["out of office"]),
              wrapWithTag("button", "", "button danger", ["Cancel"])
            ])])
      ])
}

function buildVocationView(vocation) {
  return wrapWithTag("div", vocation.id, "vocation-item row cells5",
      [
        wrapWithTag("div", "", "cell colspan2 padding20",
          [wrapWithTag("div", "", "item-info align-left",
            [
              "since",
              " <br/> ",
              toHumanReadableDate(vocation.when)
            ])]),
        wrapWithTag("div", "", "cell colspan3 padding20",
          [wrapWithTag("div", "", "item-info align-right",
          [
            "till",
            " <br/> ",
            toHumanReadableDate(vocation.when + vocation.duration)
          ])]),
        wrapWithTag("div", "", "",
          [wrapWithTag("div", "", "item-type align-right", ["vocation"])])
      ])
}