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

		 var val=$('input:radio[name="mealNumber"]:checked').val();
		 if(val==null){
		    $("#mealNumError").parent().show();
		    $("#mealNumError").text("").text("Please Choose Meal Number");
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

    $("#mealTime").focus(function(){
        $("#mealTimeError").parent().hide();
        $("#shoppingCartError").parent().hide();
    });

    $('input:radio[name="mealNumber"]').click(function(){
        $("#mealNumError").parent().hide();
        $("#shoppingCartError").parent().hide();
    });
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
        url: "/Home/OperatShoppingCart?id=" + id + "&qty=" + qty + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            $("#confirmShoppingCartTotalCount").text(data.itemCount);
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

