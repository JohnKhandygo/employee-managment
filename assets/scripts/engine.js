var address = "http://localhost:8080";
var session = "c134b3d7-f7c5-49b0-8ba8-d355489c79e3"

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
        wrapWithTag("div", "awards", "cell padding20", [wrapWithTag("h3", "", "", "PAYMENTS")]),
        wrapWithTag("div", "schedule", "cell padding20", [wrapWithTag("h3", "", "", "SCHEDULE")])
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
            $("div#awards").append(buildAwardView(d[i]))
          }
        }
      })

    $.ajax({
        url: address + "/api/out_of_offices/approved",
        method: "GET",
        contentType: "application/json",
        headers: {
          session_id: session
        },
        success: function(d) {
          for (var i = 0; i < d.length; ++i) {
            $("div#schedule").append(buildOutOfOfficeView(d[i]))
            var buttons = $("div#schedule").find("button.button")
            var lastAddedButton = buttons[buttons.length - 1];
            $(lastAddedButton).click(function(event) {
              var parentView = $(this).closest("div.out-of-office-item")
              var id = $(parentView).attr("id")
              $.ajax({
                url: address + "/api/out_of_offices/" + id + "/cancel",
                method: "DELETE",
                contentType: "application/json",
                headers: {
                  session_id: session
                },
                success: function(r) {
                  alert(JSON.stringify(r));
                  $(parentView).remove();
                }
              })
            })
          }
        }
      })

    $.ajax({
        url: address + "/api/vocations/approved",
        method: "GET",
        contentType: "application/json",
        headers: {
          session_id: session
        },
        success: function(d) {
          for (var i = 0; i < d.length; ++i) {
            $("div#schedule").append(buildVocationView(d[i]))
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