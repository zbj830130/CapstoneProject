using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Script.Serialization;
using Business.RestaurantOnline;
using Model.RestaurantOnline;

namespace Web.RestaurantOnline.Controllers
{
    public class OrderController : ApiBaseController
    {
        [System.Web.Http.HttpPost]
        public HttpResponseMessage CreateOrder([FromBody]OrderInfoFromAndroidModel orderFromAndroid)
        {
            List<OrderDetail> details = new List<OrderDetail>();
            var orderId = OrderIdHelper.NextBillNumber(2);
            var itemCount = 0;
            var totalPrice = Decimal.Zero;

            foreach (var item in orderFromAndroid.Dishes)
            {
                var orderDetail = new OrderDetail();
                decimal unitPrice = Convert.ToDecimal(item.UnitPrice);
                orderDetail.DishId = item.DishId;
                orderDetail.Count = item.Qty;
                orderDetail.DishEName = item.EName;
                orderDetail.DishOName = item.OName;
                orderDetail.OrderId = orderId;
                orderDetail.UnitPrice = unitPrice;
                itemCount += item.Qty;
                totalPrice += (item.Qty * unitPrice);
                details.Add(orderDetail);
            }

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.CreateTime = DateTime.Now;
            orderInfo.ItemsCount = itemCount;
            orderInfo.MealNumber = orderFromAndroid.TableHeadcount;
            orderInfo.MealTime = DateTime.Now;
            orderInfo.OrderId = orderId;
            orderInfo.Status = OrderStatusEnum.Dining;
            orderInfo.TableNum = Convert.ToInt32(orderFromAndroid.TableNum);
            orderInfo.TotalPrice = totalPrice;
            orderInfo.WaiterId = orderFromAndroid.WaiterId;

            OrderBuss buss = new OrderBuss();
            var result = buss.CreateOrderFromTablet(orderInfo, details);

            return ToJson(new { Result = result, OrderId = orderId });
        }

        public HttpResponseMessage GetAllAvailableOrderInfos()
        {
            OrderBuss buss = new OrderBuss();
            var orderInfos = buss.GetAllAvailableOrderInfos();

            return ToJson(new { Orders = orderInfos });
        }

        public HttpResponseMessage GetOrderDetails(string orderId)
        {
            OrderBuss buss = new OrderBuss();
            var orderInfos = buss.GetOrderDetailInfo(orderId);

            return ToJson(new { Details = orderInfos });
        }

        [System.Web.Http.HttpPost]
        public HttpResponseMessage UpdateOrderStatus([FromBody]OrderStatusUpdateModel model)
        {
            OrderBuss buss = new OrderBuss();
            buss.UpdateOrderStatus(model.OrderId, (OrderStatusEnum)model.Status);

            return ToJson(new { Result = true });
        }

        [System.Web.Http.HttpPost]
        public HttpResponseMessage ModifyOrder([FromBody]OrderInfoFromAndroidModel orderFromAndroid)
        {
            List<OrderDetail> details = new List<OrderDetail>();
            var orderId = orderFromAndroid.OrderId;
            var itemCount = 0;
            var totalPrice = Decimal.Zero;

            foreach (var item in orderFromAndroid.Dishes)
            {
                var orderDetail = new OrderDetail();
                decimal unitPrice = Convert.ToDecimal(item.UnitPrice);
                orderDetail.DishId = item.DishId;
                orderDetail.Count = item.Qty;
                orderDetail.DishEName = item.EName;
                orderDetail.DishOName = item.OName;
                orderDetail.OrderId = orderId;
                orderDetail.UnitPrice = unitPrice;
                itemCount += item.Qty;
                totalPrice += (item.Qty * unitPrice);
                details.Add(orderDetail);
            }

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.CreateTime = DateTime.Now;
            orderInfo.ItemsCount = itemCount;
            orderInfo.MealNumber = orderFromAndroid.TableHeadcount;
            orderInfo.MealTime = DateTime.Now;
            orderInfo.OrderId = orderId;
            orderInfo.Status = OrderStatusEnum.Dining;
            orderInfo.TableNum = Convert.ToInt32(orderFromAndroid.TableNum);
            orderInfo.TotalPrice = totalPrice;
            orderInfo.WaiterId = orderFromAndroid.WaiterId;

            OrderBuss buss = new OrderBuss();
            buss.UpdateOrderInfoFromTablet(orderInfo, details);

            return ToJson(new { Result = true });
        }
    }
}
