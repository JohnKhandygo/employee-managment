var address = "http://localhost:8080";
var session = "bea8e1f8-88ea-4894-bbcd-7c5dc5e21601"
//var session = "3ea3e127-7455-4272-bbbf-5f47be377e56"

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

    $(contentDiv).append(wrapWithTag("div", "", "row cells2",
      [
        wrapWithTag("div", "team", "cell padding20", [wrapWithTag("h3", "", "", "TEAM")]),
        wrapWithTag("div", "patronaged", "cell padding20",
          [wrapWithTag("h3", "", "", "PATRONAGED")])
      ]))

    $.ajax({
      url: address + "/api/employees/manager",
      method: "GET",
      contentType: "application/json",
      headers: {
        session_id: session
      },
      success: function(d) {
        if (d.type == ".employee") {
          //$("div#team").append(wrapWithTag("h4", "", "", "Manager"))
          $("div#team").append(buildManagerView(d))
        }
      }
    })

    $.ajax({
      url: address + "/api/employees/paymaster",
      method: "GET",
      contentType: "application/json",
      headers: {
        session_id: session
      },
      success: function(d) {
        if (d.type == ".employee") {
          //$("div#team").append(wrapWithTag("h4", "", "", "Manager"))
          $("div#team").append(buildPaymasterView(d))
        }
      }
    })

    $.ajax({
      url: address + "/api/employees/temmates",
      method: "GET",
      contentType: "application/json",
      headers: {
        session_id: session
      },
      success: function(d) {
        for(var i = 0; i < d.length; ++i) {
          //$("div#team").append(wrapWithTag("h4", "", "", "Manager"))
          $("div#team").append(buildTeammateView(d[i]))
        }
      }
    })

    $.ajax({
      url: address + "/api/employees/patronaged",
      method: "GET",
      contentType: "application/json",
      headers: {
        session_id: session
      },
      success: function(d) {
        for(var i = 0; i < d.length; ++i) {
          $("div#patronaged").append(buildPatronagedEmployeeView(d[i]))
          var buttons = $("div#patronaged").find("button.award-button")
          var lastAddedButton = buttons[buttons.length - 1];
          $(lastAddedButton).click(function(event) {
            var parentView = $(this).closest("div.patronaged-item")[0]
            var id = parseInt($(parentView).attr("id"))
            var awardForm = $(parentView).find("div.award-form")[0];
            if ($(awardForm).css("display") == "none") {
              $(awardForm).css("display", "block")
            } else {
              var awardAmountField = $(awardForm).find("input[name=\"award-amount\"]")[0];
              var awardAmountText = $(awardAmountField).val();
              var awardAmount = parseInt(awardAmountText);
              var awardDateField = $(awardForm).find("input[name=\"award-date\"]")[0];
              var awardDateText = $(awardDateField).val();
              var awardDate = toMilliseconds(awardDateText)
              $.post({
                url: address + "/api/awards/propose",
                headers: {
                  session_id: session
                },
                data: {
                  employee_id : id,
                  when : awardDate,
                  amount : awardAmount
                },
                success: function(d) {
                  alert(JSON.stringify(d))
                }
              })
              $(awardForm).css("display", "none")
            }
          })
        }
      }
    })
  })

  $(proposalsSection).click(function() {
    $(contentDiv).empty()

    $(homeSection).removeClass("active")
    $(stuffSection).removeClass("active")
    $(proposalsSection).addClass("active")
  })
})