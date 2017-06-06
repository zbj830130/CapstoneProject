$(function () {

    $('#login-form-link').click(function (e) {
        $("#login_form").delay(100).fadeIn(100);
        $("#register_form").fadeOut(100);
        $('#register-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });
    $('#register-form-link').click(function (e) {
        $("#register_form").delay(100).fadeIn(100);
        $("#login_form").fadeOut(100);
        $('#login-form-link').removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    });

    registerInit();

});

function registerInit() {
    $("#reg_username").focusin(function () {
        hidError("reg_username");
    }).focusout(function () {
        checkUserName("reg_username", true);
    });

    $("#reg_password").focusin(function () {
        hidError("reg_password");
    }).focusout(function () {
        checkPassword("reg_password");
    });

    $("#reg_confirm_password").focusin(function () {
        hidError("reg_confirm_password");
    }).focusout(function () {
        checkConfirmePwd("reg_password", "reg_confirm_password");
    });


    $("#register_submit").click(function () {
        if (checkUserName("reg_username", true) == false) {
            return false;
        }

        if (checkPassword("reg_password") == false) {
            return false;
        }

        if (checkConfirmePwd("reg_password", "reg_confirm_password") == false) {
            return false;
        }

        $.ajax({
            type: 'get',
            url: '/User/Registing',
            data: $("#register_form").serialize(),
            success: function (data) {
                if (data.result == true) {
                    pageJump();
                } else {
                    alert("Register failed");
                }
            }
        });
    });

    $("#log_username").focusin(function () {
        hidError("log_username");
    }).focusout(function () {
        checkUserName("log_username", false);
    });

    $("#log_password").focusin(function () {
        hidError("log_password");
    }).focusout(function () {
        checkPassword("log_password");
    });

    $("#login_submit").click(function () {
        if (checkUserName("log_username", false) == false) {
            return false;
        }

        if (checkPassword("log_password") == false) {
            return false;
        }

        $.ajax({
            type: 'get',
            url: '/User/Login',
            data: $("#login_form").serialize(),
            success: function (data) {
                if (data.result == true) {
                   pageJump();
                } else {
                    alert("Login failed");
                }
            }
        });
    });
}

function showError(elementId, error) {
    $("#" + elementId).next().text(error).css("color", "red").show();
}

function hidError(elementId) {
    $("#" + elementId).next().css("color", "red").hide();
}

function checkUserName(userNameId, isCheckExisted) {
    var userName = $("#" + userNameId).val();
    if (userName.length == 0) {
        showError(userNameId, "Username is required!");
        return false;
    }

    reg = /^[a-zA-Z]\w{5,15}$/;
    if (!reg.test(userName)) {
        showError(userNameId, "Username format is wrong!");
        return false;
    }

    if (isCheckExisted === true) {
        $.ajax({
            type: 'get',
            async: false,
            url: '/User/IsUsernameExisted?userName=' + userName,
            success: function (data) {
                if (data.result == true) {
                    showError(userNameId, "Username is exsited!");
                    return false;
                } else {
                    return true;
                }
            }
        });
    }
}

function checkPassword(pwdId) {
    var pwd = $("#" + pwdId).val();
    if (pwd.length == 0) {
        showError(pwdId, "Password is required!");
        return false;
    }

    reg = /^[a-zA-Z]\w{5,15}$/;
    if (!reg.test(pwd)) {
        showError(pwdId, "Password format is wrong!");
        return false;
    }
}

function checkConfirmePwd(pwdId, cfPwdId) {
    var pwd = $("#" + pwdId).val();
    var cfPwd = $("#" + cfPwdId).val();

    if (pwd !== cfPwd) {
        showError(cfPwdId, "Confirm password does not equal to password!");
        return false;
    }
}

function pageJump(){
    var pageUrl = $("#callbackUrl").val();
    if(pageUrl.length == 0){
        return;
    }
    window.location.href = pageUrl;
}
