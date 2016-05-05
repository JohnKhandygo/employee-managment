var address = "http://localhost:8080";
var session = "ee7684b2-c97d-4a49-8dfc-70dc13dffd0b"

$(document).ready(function() {
  var contentDiv = $("div#content")

  var homeSection = $("li#home")
  var stuffSection = $("li#stuff")
  var proposalsSection = $("li#proposals")

  $(homeSection).click(function() {
    $(contentDiv).empty()

    $(stuffSection).removeClass("active")
    $(proposalsSection).removeClass("active")
    $(homeSection).addClass("active")

    $(contentDiv).append(wrapWithTag("div", "", "row cells2",
      [
        wrapWithTag("div", "awards", "cell padding20", [""]),
        wrapWithTag("div", "schedule", "cell padding20", [""])
      ]))



    $.ajax({
        url: address + "/api/awards/approved",
        method: "GET",
        contentType: "application/json",
        headers: {
          session_id: session
        },
        success: function(d) {
          for (var i = 0; i < d.length; ++i) {
            //$(contentDiv).append(buildAwardItemView(d[i]))
            $("div#awards").append(buildAwardItemView(d[i]))
          }
        }
      })
  })

  $(stuffSection).click(function() {
    $(contentDiv).empty()

    $(homeSection).removeClass("active")
    $(proposalsSection).removeClass("active")
    $(stuffSection).addClass("active")
  })

  $(proposalsSection).click(function() {
    $(contentDiv).empty()

    $(homeSection).removeClass("active")
    $(stuffSection).removeClass("active")
    $(proposalsSection).addClass("active")
  })
})