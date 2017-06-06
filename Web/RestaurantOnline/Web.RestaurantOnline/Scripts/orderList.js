$(function(){
    $("#leftNavbar").children().removeClass("active");
    $("#navMyOrders").addClass("active");
    getAllOrders(1);
    tabEventsBinding();
});

function tabEventsBinding(){
    $("#tabAllOrders").click(function(){
        if($("#allOrders").children().length == 0){
            getAllOrders(1);
        }
    });

    $("#tabReOrders").click(function(){
        if($("#reserved").children().length == 0){
            getReservedOrders(1);
        }
    });

    $("#tabCaOrders").click(function(){
        if($("#canceled").children().length == 0){
            getCanceledOrders(1);
        }
    });

    $("#tabFiOrders").click(function(){
        if($("#finished").children().length == 0){
            getFinishedOrders(1);
        }
    });
}

function getOrderDetailsByOrderInfoId(parentEle){
    var orderInfoId = $(parentEle).attr("id").split('_')[1];

    $.ajax({ 
        url: "/User/GetOrderDetails?orderInfoId=" + orderInfoId + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            $(parentEle).html("").html(data);
          }
    });
}

function getAllOrders(currentPage){
    $.ajax({ 
	    url: "/User/GetAllOrders?currentPage=" + currentPage + "&r=" + Math.random(),
	    type: 'get',
	    success: function(data){
	        $("#allOrders").html("").html(data);

            $("#allOrders").find(".orderDetails").each(function(){
                getOrderDetailsByOrderInfoId(this);
            });

	      }
    });
}

function getReservedOrders(currentPage){
    $.ajax({ 
        url: "/User/GetReservedOrders?currentPage=" + currentPage + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
			$("#reserved").html("").html(data);

			$("#reserved").find(".orderDetails").each(function(){
			    getOrderDetailsByOrderInfoId(this);
			});
        }
    });
}

function getCanceledOrders(currentPage){
    $.ajax({ 
        url: "/User/GetCanceledOrders?currentPage=" + currentPage + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            $("#canceled").html("").html(data);

            $("#canceled").find(".orderDetails").each(function(){
                getOrderDetailsByOrderInfoId(this);
            });
          }
    });
}

function getFinishedOrders(currentPage){
    $.ajax({ 
        url: "/User/GetFinishedOrders?currentPage=" + currentPage + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            $("#finished").html("").html(data);

            $("#finished").find(".orderDetails").each(function(){
                getOrderDetailsByOrderInfoId(this);
            });
          }
    });
}

function cancelOrder(orderInfoId){
    $.ajax({ 
        url: "/User/CancleOrder?orderInfoId=" + orderInfoId + "&r=" + Math.random(),
        type: 'get',
        success: function(data){
            $("#finished").html("").html(data);
            getAllOrders(1);
            getReservedOrders(1);
          }
    });
}
