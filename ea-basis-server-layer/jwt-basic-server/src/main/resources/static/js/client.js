/**
 * Created by stephan on 20.03.16.
 */

$(function () {
    // VARIABLES =============================================================
    var TOKEN_KEY = "jwtToken";
    var $notLoggedIn = $("#notLoggedIn");
    var $loggedIn = $("#loggedIn").hide();
    var $loggedInBody = $("#loggedInBody");
    var $response = $("#response");
    var $login = $("#login");
    var $userInfo = $("#userInfo").hide();

    // FUNCTIONS =============================================================
    function getJwtToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    function setJwtToken(token) {
        localStorage.setItem(TOKEN_KEY, token);
    }

    function removeJwtToken() {
        localStorage.removeItem(TOKEN_KEY);
    }

    function doLogin(loginData) {
        $.ajax({
            url: "/auth",
            type: "POST",
            data: JSON.stringify(loginData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (result, textStatus, jqXHR) {
                if (result.code === 200){
                    console.log(result);
                    setJwtToken(result.data.token);
                    $login.hide();
                    $notLoggedIn.hide();
                    showTokenInformation();
                    showUserInformation();
                }else if(result.code === 401 || result.code === 403){
                    $('#loginErrorModal')
                        .modal("show")
                        .find(".modal-body")
                        .empty()
                        .html("<p>Message from server:<br>" + jqXHR.responseText + "</p>");
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                throw new Error("an unexpected error occured: " + errorThrown);
            }
        });
    }

    function doLogout() {
        removeJwtToken();
        $login.show();
        $userInfo
            .hide()
            .find("#userInfoBody").empty();
        $loggedIn.hide();
        $loggedInBody.empty();
        $notLoggedIn.show();
    }

    function createAuthorizationTokenHeader() {
        var token = getJwtToken();
        if (token) {
            return {"Authorization": "Bearer " + token};
        } else {
            return {};
        }
    }

    function showUserInformation() {
        $.ajax({
            url: "/user",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (result, textStatus, jqXHR) {
                if (result.code === 200){
                    var $userInfoBody = $userInfo.find("#userInfoBody");

                    $userInfoBody.append($("<div>").text("Username: " + result.data.username));
                    $userInfoBody.append($("<div>").text("Email: " + result.data.email));

                    var $authorityList = $("<ul>");
                    result.data.authorities.forEach(function (authorityItem) {
                        $authorityList.append($("<li>").text(authorityItem.authority));
                    });
                    var $authorities = $("<div>").text("Authorities:");
                    $authorities.append($authorityList);

                    $userInfoBody.append($authorities);
                    $userInfo.show();
                }

            }
        });
    }

    function showTokenInformation() {
        var jwtToken = getJwtToken();
        var decodedToken = jwt_decode(jwtToken);

        $loggedInBody.append($("<h4>").text("Token"));
        $loggedInBody.append($("<div>").text(jwtToken).css("word-break", "break-all"));
        $loggedInBody.append($("<h4>").text("Token claims"));

        var $table = $("<table>").addClass("table table-striped");
        appendKeyValue($table, "sub", decodedToken.sub);
        appendKeyValue($table, "iat", decodedToken.iat);
        appendKeyValue($table, "exp", decodedToken.exp);

        $loggedInBody.append($table);
        $loggedIn.show();
    }

    function appendKeyValue($table, key, value) {
        var $row = $("<tr>")
            .append($("<td>").text(key))
            .append($("<td>").text(value));
        $table.append($row);
    }

    function showResponse(statusCode, message) {
        $response
            .empty()
            .text("status code: " + statusCode + "\n-------------------------\n" + message);
    }

    // REGISTER EVENT LISTENERS =============================================================
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        var $form = $(this);
        var formData = {
            username: $form.find('input[name="username"]').val(),
            password: $form.find('input[name="password"]').val()
        };

        doLogin(formData);
    });

    $("#logoutButton").click(doLogout);

    $("#exampleServiceBtn").click(function () {
        $.ajax({
            url: "/persons",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (result, textStatus, jqXHR) {
                console.log(jqXHR);
                showResponse(result.code, JSON.stringify(result));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                showResponse(jqXHR.status, errorThrown);
            }
        });
    });

    $("#adminServiceBtn").click(function () {
        $.ajax({
            url: "/protected",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            headers: createAuthorizationTokenHeader(),
            success: function (result, textStatus, jqXHR) {
                console.log(jqXHR);
                console.log(result);
                showResponse(result.code, result);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponse(jqXHR.status, errorThrown);
            }
        });
    });

    $loggedIn.click(function () {
        $loggedIn
            .toggleClass("text-hidden")
            .toggleClass("text-shown");
    });

    // INITIAL CALLS =============================================================
    if (getJwtToken()) {
        //$login.hide();
        //$notLoggedIn.hide();
        showTokenInformation();
        showUserInformation();
    }
});
