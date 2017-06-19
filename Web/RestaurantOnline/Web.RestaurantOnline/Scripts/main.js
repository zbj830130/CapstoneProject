$(function(){
    $("#loginButton").hide();

    GetUserName();

    $("#userLogout").click(function(){
        logout();
    });

    $("#loginButton").click(function(){
        window.location.href = "/User/Index?callbackUrl=http://127.0.0.1:8081";
    });

    $("#sendMessageBtn").click(function(){
        sendMessage();
    });

});

function GetUserName(){
    $.ajax({
            type: 'get',
            url: '/User/GetUserName',
            success: function (data) {
                if (data.result == true) {
                    $("#loginButton").hide();
                    $("#userLogout").show();
                    $("#loginUserName").show();
                    $("#loginUserName").text(data.userName);
                } else {
                    $("#loginButton").show();
                    $("#userLogout").hide();
                    $("#loginUserName").hide();
                    $("#loginUserName").text("");
                }
            }
        });
}

function logout(){
    $.ajax({
            type: 'get',
            url: '/User/Logout',
            success: function (data) {
                if (data.result == true) {
                    $("#loginButton").show();
                    $("#userLogout").hide();
                    $("#loginUserName").hide();
                    $("#loginUserName").text("");
                } else {
                    $("#loginButton").hide();
                    $("#userLogout").show();
                    $("#loginUserName").show();
                    $("#loginUserName").text(data.userName);
                }
            }
        });
}

function sendMessage(){
    var subject = $("#inputSubject").val();
        var message = $("#inputMessage").val();
        var mailtoAttr = "mailto:zbj830130@gmail.com?subject=" + subject + "&body=" + message;

        $("#sendMessageBtn").attr("href", mailtoAttr);
}
