﻿$(function(){
    formElementsBindEvent();

	loadShoppingCartInfo();

    $("#leftNavbar").children().removeClass("active");

    var oTop = $(".userInfo").offset().top;
    var sTop = 0;
    $(window).scroll(function(){
        sTop = $(this).scrollTop();
        if(sTop >= oTop){
            var originalWidth = Number($(".userInfo").css('width').replace("px",""));
            var windowWidth = Number($(window).width()); 
            var widthPercent = (originalWidth/windowWidth)*100 + "%";

            $(".userInfo").addClass("userInfoFied").css({left:$(".confirmOrderContainer").offset().left,width:widthPercent});
        }else{
            $(".userInfo").removeClass("userInfoFied").css({left:0,width:'50%'});
        }
    });

   $("#userInfo").submit(function(){
		 var val=$('input:radio[name="gender"]:checked').val();
		 if(val==null){
		    $("#genderError").parent().show();
		    $("#genderError").text("").text("Please Choose Gender");
		    return false;
		 }

		 var lastName = $("#lastName").val();
		 if($.trim(lastName).length ==0){
		    $("#lastNameError").parent().show();
		    $("#lastNameError").text("").text("Please input Last Name");
		    return false;
		 }

         var lastName = $("#lastName").val();
         if($.trim(lastName).length >200){
            $("#lastNameError").parent().show();
            $("#lastNameError").text("").text("The character length can not be greater than 200");
            return false;
         }

         var reg = /^[A-Za-z]{1,200}$/;
	     if (!reg.test(lastName)) {
            $("#lastNameError").parent().show();
            $("#lastNameError").text("").text("Please input English charaters");
	        return false;
         }

         var flag = /[a-zA-Z]+/.test(lastName);

		 var mealTime = $("#mealTime").val();
		 if($.trim(mealTime).length ==0){
		    $("#mealTimeError").parent().show();
		    $("#mealTimeError").text("").text("Please Choose Meal Time");
		    return false;
		 }

		 var now = new Date();

		 if(new Date(Date.parse(mealTime.replace(/-/g,  "/"))) < new Date(now.getFullYear(),now.getMonth(),now.getDate(),0,0,0)){
		    $("#mealTimeError").parent().show();
		    $("#mealTimeError").text("").text("Meal Time Must Be No Earlier Than Today");
		    return false;
		 }

         var mealNum = $("#mealNumDw").val();

         if(mealNum==0){
            $("#mealNumError").parent().show();
            $("#mealNumError").text("").text("Please Choose Meal Number");
            return false;
         }

         var tableNum = $("#tableNum").val();
         if(tableNum == 0){
            $("#tableNumError").parent().show();
            $("#tableNumError").text("").text("Please Choose Table");
            return false;
         }

         var itemCount = 0;

		$.ajax({ 
		    url: "/Home/GetShoppingCartItemCount",
		    type: 'get',
		    async: false,
		    success: function(data){
		        itemCount = data.itemCount;
		      }
		});

        if(itemCount ==0){
            $("#shoppingCartError").parent().show();
            $("#shoppingCartError").text("").text("Shopping Cart Is Empty");

            return false;
        }
   });

   $(".goBack").click(function(){
        window.location.href="Index";
   });
});

function formElementsBindEvent(){

    $('#mealTime').datetimepicker({  
        format: "yyyy-mm-dd",
        autoclose: false,
        todayBtn: true,
        startDate: new Date(),
        startView: 'month',
        minView:'year',
        maxView:'month'
    });

    $('input:radio[name="gender"]').click(function(){
        $("#genderError").parent().hide();
        $("#shoppingCartError").parent().hide();
    });

    $("#lastName").focus(function(){
        $("#lastNameError").parent().hide();
        $("#shoppingCartError").parent().hide();
    });

    $("#mealTime").change(function(){
        var mealNum = $("#mealNumDw").val();
        var mealTime = $("#mealTime").val();

        if(mealNum >0 && $.trim(mealTime).length > 0){
            getAvailableTableNums(mealNum,mealTime);
        }

    }).focus(function(){
        $("#mealTimeError").parent().hide();
        $("#shoppingCartError").parent().hide();
    });
    for(var i=2;i<=20;i++){
        if(i%2==0){
            $("#mealNumDw").append("<option value='"+i+"'>" + i + " Person</option>");
        }
    }

    $("#mealNumDw").click(function(){
        $("#mealNumError").parent().hide();
        $("#shoppingCartError").parent().hide();
    }).change(function(){
        var mealNum = $("#mealNumDw").val();
        var mealTime = $("#mealTime").val();

        if(mealNum >0 && $.trim(mealTime).length > 0){
            getAvailableTableNums(mealNum,mealTime);
        }

    });

    $("#tableNum").click(function(){
        $("#tableNumError").parent().hide();
        $("#shoppingCartError").parent().hide();
    })


}

function submitOrder() {
    var userInfo = $("#userInfo").serialize();

    $.ajax({ 
        url: "/Home/CreateOrder",
        type: 'POST',
        data: userInfo,
        success: function(data){
            if(data.result == false){
                alert("false");
            }
            else{
                alert(data.orderId);
            }
          }
    });
}

function loadShoppingCartInfo(){
    $.ajax({ 
        url: "/Home/OrderDetails?&r=" + Math.random(),
        type: 'get',
        success: function(data){
            $(".confirmShoppingCarItemList").html("").append(data);
            $("#confirmShoppingCartTotalCount").text($("#itemCount").val());
            $("#confirmShoppingCartTotalPrice").text($("#totalPrice").val());
            $("#submitPrice").text($("#totalPrice").val());


            $("#itemCount").remove();
            $("#totalPrice").remove();
          }
    });  
}

function confirmMinuQty(id){
    var qty = Number($("#confirmItemQty_" + id).text());
    if(qty==1){
        qty=0;
        $("#confirmListItem_" + id).remove();
        confirmGetShoppingCartTotalInfo(id,0);
        return;
    }

    qty = qty - 1;
    $("#confirmItemQty_" + id).text(qty);

    confirmGetShoppingCartTotalInfo(id,qty);
}

function confirmAddQty(id){
    var qty = Number($("#confirmItemQty_" + id).text());
    var qty = qty + 1;
    $("#confirmItemQty_" + id).text(qty);

    confirmGetShoppingCartTotalInfo(id,qty);
}

function confirmGetShoppingCartTotalInfo(id,qty){
    singleItemTotalPrice(id,qty);
    
    $.ajax({ 
        url: "/Home/OperatShoppingCartForOrderConfirm?id=" + id + "&qty=" + qty + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            $("#confirmShoppingCartTotalCount").text(data.totalItems);
            $("#confirmShoppingCartTotalPrice").text(data.totalPrice);
            $("#submitPrice").text(data.totalPrice);
          }
    });
}

function singleItemTotalPrice(id,qty){
    var price = Number($("#confirmItemPrice_" + id).val());
    var totalPrice = qty * price;
    $("#confirmItemTotalPrice_" + id).text(totalPrice);
}

function getAvailableTableNums(mealNum,mealDate){
    $("#tableNumError").parent().hide();
    $("#tableNumError").text("");

    $("#tableNum").empty();
    $("#tableNum").append('<option value="0">Please Choose</option>');
    $.ajax({ 
        url: "/Home/GetAvailableTableNum?mealNum=" + mealNum + "&mealDate=" + mealDate + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            var tableNums = eval(data.tableNums);
            if(tableNums.length==0){
                $("#tableNumError").parent().show();
                $("#tableNumError").text("").text("Sorry, No available tables");
            }
            else{
                for (var i = 0; i < tableNums.length; i++){
                    $("#tableNum").append("<option value='"+ tableNums[i] +"'> No. " + tableNums[i] + "</option>");
                }

            }
        }
    });
}


