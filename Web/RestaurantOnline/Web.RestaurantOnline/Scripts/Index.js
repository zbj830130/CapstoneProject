$(function(){
    $(".catgoryItem").click(function(){
        $(".catgoryItem").css('backgroundColor','white');
        $(".catgoryItem").find('.oName').css('color','gray');
        $(".catgoryItem").find('.eName').css('color','black');


        $(this).css('backgroundColor','#f4a40e');
        $(this).find('.oName').css('color','white');
        $(this).find('.eName').css('color','white');

        var cagetoryId = $(this).parent().attr('id');

        if($(".secondNav").hasClass("secondNavFied") == false){
            $("html,body").animate({scrollTop:$(".secondNav").offset().top},100);
        }

        $("html,body").animate({scrollTop:$("#category_"+cagetoryId).offset().top - $(".secondNav").height()},1000);

    });

    $('#confirmButton').click(function(){
        if($('#confirmButton').text() == "Confirm Order >"){
            window.location.href = "/Home/ConfirmOrder";
        }
    });

    $(".dishContainer").each(function(){
        var cagetoryId = $(this).attr("id").split('_')[1];

        $.ajax({ 
        url: "/Home/DishList?id=" + cagetoryId,
        type: 'get',
        success: function(data){
            $("#dishes_" + cagetoryId).html("").html(data);
          }
        });
    });

    $.ajax({ 
        url: "/Home/LoadMiniShoppingCart?&r=" + Math.random(),
        type: 'get',
        success: function(data){
            bindShoppingCartData(data);
          }
    });

    var oTop = $(".secondNav").offset().top;
    var sTop = 0;
    $(window).scroll(function(){
        sTop = $(this).scrollTop();
        if(sTop >= oTop){
            $(".secondNav").addClass("secondNavFied").css({left:$(".dishList").offset().left});
            $(".placeHolderDiv").css({minHeight:$(".secondNav").height()}).show();
        }else{
            $(".secondNav").removeClass("secondNavFied").css({left:0});
            $(".placeHolderDiv").css({minHeight:0}).hide();
        }

        var secondNavTop = $(".secondNav").offset().top + $(".secondNav").height();

        $(".dishContainer").each(function(){
            if($(this).offset().top + $(this).height() > secondNavTop){
                
                $(".catgoryItem").css('backgroundColor','white');
                $(".catgoryItem").find('.oName').css('color','gray');
                $(".catgoryItem").find('.eName').css('color','black');

                var cItem = $("#" + $(this).attr("id").split('_')[1]).children('.catgoryItem');

                $(cItem).css('backgroundColor','#f4a40e');
                $(cItem).find('.oName').css('color','white');
                $(cItem).find('.eName').css('color','white');
                return false;
            }
        });
    });

    var pushTop = $("#push").offset().top;
    $(".itemList").css("max-height", pushTop-oTop-30);

    $(".miniShoppingCartName").click(function(){
        if($(".itemList").is(":hidden")){
            $(".itemList").show();
        }
        else{
            $(".itemList").hide();
        }
    })
});

function bindShoppingCartData(data){
    $(".itemList").html("").append(data);
    $(".itemCount").text($("#itemCount").val());
    $(".totalPrice").text($("#totalPrice").val());

    if($(".itemCount").text() ==0){
        $('#confirmButton').css('backgroundColor','#c5c5c5').text('Shopping Cart is Empty');
    }
    else{
        $('#confirmButton').css('backgroundColor','#41d74a').text('Confirm Order >');
    }

    $("#itemCount").remove();
    $("#totalPrice").remove();

    $(".shoppingButton").show();
    $(".shoppingCount").hide();

    $(".listItem").each(function(){
        var itemId = $(this).attr("id").split('_')[1];
        $("#buy_" + itemId).hide();
        $("#shoppingCount_" + itemId).show();
        $("#shoppingQty_" + itemId).text($("#itemQty_"+ itemId).text());
    });
}

function buyButtonClick(id){
    $("#buy_" + id).hide();
    $("#shoppingCount_" + id).show();
    $("#shoppingQty_" + id).text(1);

    addShoppingFlyer(id);
    addItemToShoppingCart(id);

    getShoppingCartTotalInfo(id,1);
}

function buyMinuQty(id){
    var qty = Number($("#shoppingQty_" + id).text());
    if(qty==1){
        qty=0;
        $("#shoppingQty_" + id).text(qty);
        $("#buy_" + id).show();
        $("#shoppingCount_" + id).hide();
        removeItemFromMiniCart(id);

        getShoppingCartTotalInfo(id,0);
        return;
    }

    qty = qty - 1;
    $("#shoppingQty_" + id).text(qty);
    syncQtyFromItemToMiniCart(id,qty);

    getShoppingCartTotalInfo(id,qty);
}

function buyAddQty(id){
    var qty = Number($("#shoppingQty_" + id).text());
    var qty = qty + 1;
    $("#shoppingQty_" + id).text(qty);

    addShoppingFlyer(id);
    syncQtyFromItemToMiniCart(id,qty);

    getShoppingCartTotalInfo(id,qty);
}

function miniMinuQty(id){
    var qty = Number($("#itemQty_" + id).text());
    if(qty==1){
        qty=0;
        syncQtyFromMiniCartToItem(id,qty);
        $("#buy_" + id).show();
        $("#shoppingCount_" + id).hide();
        removeItemFromMiniCart(id);

        getShoppingCartTotalInfo(id,0);
        return;
    }

    qty = qty - 1;
    $("#itemQty_" + id).text(qty);
    syncQtyFromMiniCartToItem(id,qty);

    getShoppingCartTotalInfo(id,qty);
}

function miniAddQty(id){
    var qty = Number($("#itemQty_" + id).text());
    var qty = qty + 1;
    $("#itemQty_" + id).text(qty);

    syncQtyFromMiniCartToItem(id,qty);

    getShoppingCartTotalInfo(id,qty);
}

function syncQtyFromItemToMiniCart(id,qty){
    $("#itemQty_" + id).text(qty);
}

function syncQtyFromMiniCartToItem(id,qty){
    $("#shoppingQty_" + id).text(qty);
}

function singleItemTotalPrice(id,qty){
    var price = Number($("#dishPrice_" + id).text().substring(1));
    var totalPrice = qty * price;
    $("#itemTotalPrice_" + id).text(totalPrice);
}

function removeItemFromMiniCart(id){
    $("#listItem_" + id).remove();
}

function addItemToShoppingCart(id){
    $.ajax({ 
        url: "/Home/MiniShoppingCartSingleItem?id=" + id,
        type: 'get',
        success: function(data){
            $(".itemList").append(data);
          }
        });
}

function getShoppingCartTotalInfo(id,qty){
    singleItemTotalPrice(id,qty);
    
    $.ajax({ 
        url: "/Home/OperatShoppingCart?id=" + id + "&qty=" + qty + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            bindShoppingCartData(data);
        }
    });
}

function addShoppingFlyer(id){ 
     var qtyOffset = $("#shoppingQty_" + id).offset();
     var startY = qtyOffset.top - $(window).scrollTop();
     var startX = qtyOffset.left;


    var offset = $(".miniShoppingCart").offset();  
    var flyer = $('<span class="flyerPoint"></span>');
    flyer.fly({   
        start: {   
            left: startX,
            top: startY
        },   
        end: {   
            left: offset.left  + 10,
            top: offset.top - $(window).scrollTop() + 10,
        },   
        onEnd: function() {
            flyer.remove(); 
        }   
    });  
}