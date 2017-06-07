using System;
using System.Data;
using System.Collections.Generic;
using Data.RestaurantOnline;
using Model.RestaurantOnline;

namespace Business.RestaurantOnline
{
    public class OrderBuss
    {
        OrderDao dao = new OrderDao();

        public bool CreateOrder(OrderInfo orderInfo, List<ShoppingCartItem> cartItems, out string orderId)
        {
            orderId = String.Empty;

            if (orderInfo == null)
            {
                return false;
            }

            if (cartItems == null || cartItems.Count == 0)
            {
                return false;
            }

            orderId = OrderIdHelper.NextBillNumber(1);
            orderInfo.OrderId = orderId;
            List<OrderDetail> orderDetails = new List<OrderDetail>();

            foreach (var item in cartItems)
            {
                OrderDetail detail = new OrderDetail();
                detail.OrderId = orderId;
                detail.DishId = item.ItemID;
                detail.DishEName = item.EName;
                detail.DishOName = item.OName;
                detail.UnitPrice = item.Price;
                detail.Count = item.Qty;

                orderDetails.Add(detail);
            }


            var monthStatus = String.Empty;
            char[] charsList = null;

            var tableDs = new TableDao().GetTableMonthStatusByTableNum(orderInfo.TableNum, orderInfo.MealTime.Month);
            if (tableDs != null && tableDs.Tables != null && tableDs.Tables.Count > 0)
            {
                monthStatus = tableDs.Tables[0].Rows[0]["smn"].ToString();
                charsList = monthStatus.ToCharArray();
                charsList[orderInfo.MealTime.Day - 1] = '0';
            }

            dao.CreateOrder(orderInfo, orderDetails, new string(charsList));

            return true;
        }

        public OrderInfoPaginationModel GetOrderList(int userId, int currentPage, int orderStatus)
        {
            var pageSize = int.Parse(System.Configuration.ConfigurationManager.AppSettings["pageSize"]);
            DataSet ds = null;

            switch (orderStatus)
            {
                case 0:
                    ds = dao.GetAllOrders(userId, currentPage, pageSize);
                    break;

                case 1:
                    ds = dao.GetReservedOrders(userId, currentPage, pageSize);
                    break;

                case 2:
                    ds = dao.GetCancledOrders(userId, currentPage, pageSize);
                    break;

                case 4:
                    ds = dao.GetFinishedOrders(userId, currentPage, pageSize);
                    break;

            }

            var orders = new OrderInfoPaginationModel();
            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0] != null && ds.Tables[0].Rows != null)
            {
                var totalRows = Convert.ToInt32(ds.Tables[0].Rows[0][0].ToString());
                if (totalRows <= 0)
                {
                    orders.TotalPage = 0;
                    orders.PageSize = pageSize;
                    orders.CurrentPage = 1;
                }
                else
                {
                    orders.TotalPage = totalRows % pageSize == 0 ? totalRows / pageSize : (totalRows / pageSize) + 1;
                    orders.PageSize = pageSize;
                    orders.CurrentPage = currentPage;
                }
            }

            if (orders.TotalPage == 0)
            {
                return orders;
            }

            if (ds.Tables[1] != null && ds.Tables[1].Rows != null)
            {
                orders.OrderList = new List<OrderInfo>();

                foreach (DataRow dr in ds.Tables[1].Rows)
                {
                    var orderInfo = new OrderInfo();
                    orderInfo.Id = int.Parse(dr[0].ToString());
                    orderInfo.OrderId = dr[1].ToString();
                    orderInfo.Gender = dr[2].ToString();
                    orderInfo.LastName = dr[3].ToString();
                    orderInfo.MealTime = DateTime.Parse(dr[4].ToString());
                    orderInfo.MealNumber = dr[5].ToString();
                    orderInfo.TotalPrice = decimal.Parse(dr[6].ToString());
                    orderInfo.ItemsCount = int.Parse(dr[7].ToString());
                    orderInfo.CreateTime = DateTime.Parse(dr[8].ToString());
                    orderInfo.Status = (OrderStatusEnum)int.Parse(dr[9].ToString());

                    orders.OrderList.Add(orderInfo);
                }
            }

            return orders;

        }

        public List<OrderDetail> GetOrderDetailInfo(int userId, int orderInfoId)
        {
            var orderDetails = new List<OrderDetail>();
            var ds = dao.GetOrderDetailInfo(userId, orderInfoId);

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0
                && ds.Tables[0] != null && ds.Tables[0].Rows != null)
            {
                foreach (DataRow dr in ds.Tables[0].Rows)
                {
                    var od = new OrderDetail();

                    od.DishId = int.Parse(dr[0].ToString());
                    od.DishEName = dr[1].ToString();
                    od.DishOName = dr[2].ToString();
                    od.UnitPrice = decimal.Parse(dr[3].ToString());
                    od.Count = int.Parse(dr[4].ToString());

                    orderDetails.Add(od);
                }
            }

            return orderDetails;
        }

        public bool CancelOrder(int userId, int orderInfoId)
        {
            var result = true;

            try
            {
                var ds = dao.GetOrderInfoByOrderInfoId(orderInfoId);
                var tableNum = 0;
                var monthNum = 0;
                var dayInMonth = 0;
                char[] charsList = null;
                var monthStatus = String.Empty;

                if (ds != null && ds.Tables != null && ds.Tables.Count > 0)
                {
                    tableNum = Convert.ToInt32(ds.Tables[0].Rows[0]["tableNumber"].ToString());
                    var mealTime = Convert.ToDateTime(ds.Tables[0].Rows[0]["mealTime"].ToString());
                    monthNum = mealTime.Month;
                    dayInMonth = mealTime.Day;
                }

                var tableDs = new TableDao().GetTableMonthStatusByTableNum(tableNum, monthNum);
                if (tableDs != null && tableDs.Tables != null && tableDs.Tables.Count > 0)
                {
                    monthStatus = tableDs.Tables[0].Rows[0]["smn"].ToString();
                    charsList = monthStatus.ToCharArray();
                    charsList[dayInMonth - 1] = '1';
                }

                dao.CancelOrder(userId, orderInfoId, tableNum, monthNum, new string(charsList));
            }
            catch
            {
                result = false;
            }

            return result;
        }

        public bool CreateOrderFromTablet(OrderInfo orderInfo, List<OrderDetail> cartItems)
        {
            if (orderInfo == null)
            {
                return false;
            }

            if (cartItems == null || cartItems.Count == 0)
            {
                return false;
            }

            dao.CreateOrderFromTablet(orderInfo, cartItems);

            return true;
        }

        public List<OrderInfo> GetAllAvailableOrderInfos()
        {
            DataSet ds = dao.GetAvailableOrders();
            List<OrderInfo> orderInfos = new List<OrderInfo>();

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0
                && ds.Tables[0] != null && ds.Tables[0].Rows != null)
            {
                foreach (DataRow dr in ds.Tables[0].Rows)
                {
                    var item = new OrderInfo();
                    item.Id = Convert.ToInt32(dr[0].ToString());
                    item.OrderId = dr[1].ToString();

                    if (dr[2] != null)
                    {
                        item.Gender = dr[2].ToString();
                    }

                    if (dr[3] != null)
                    {
                        item.LastName = dr[3].ToString();
                    }
                    item.MealTime = Convert.ToDateTime(dr[4].ToString());
                    item.MealNumber = dr[5].ToString();

                    if (dr[6] != null && String.IsNullOrWhiteSpace(dr[6].ToString()) == false)
                    {
                        item.WaiterId = Convert.ToInt32(dr[6].ToString());
                    }

                    if (dr[7] != null && String.IsNullOrWhiteSpace(dr[7].ToString()) == false)
                    {
                        item.TableNum = Convert.ToInt32(dr[7].ToString());
                    }

                    item.Status = (OrderStatusEnum)Convert.ToInt32(dr[8].ToString());

                    orderInfos.Add(item);
                }
            }

            return orderInfos;
        }

        public void UpdateOrderStatus(string orderId, OrderStatusEnum status)
        {
            dao.UpdateOrderStatus(orderId, status);
        }

        public List<OrderDetail> GetOrderDetailInfo(string orderId)
        {
            var ds = dao.GetOrderDetailInfo(orderId);
            List<OrderDetail> result = new List<OrderDetail>();

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                var item = new OrderDetail();
                item.Count = Convert.ToInt32(dr[4].ToString());
                item.DishEName = dr[1].ToString();
                item.DishOName = dr[2].ToString();
                item.UnitPrice = Convert.ToDecimal(dr[3].ToString());
                item.DishId = Convert.ToInt32(dr[0].ToString());
                item.OrderId = orderId;

                result.Add(item);
            }

            return result;
        }

        public void UpdateOrderInfoFromTablet(OrderInfo orderInfo, List<OrderDetail> cartItems)
        {
            dao.UpdateOrderInfoFromTablet(orderInfo, cartItems);
        }
    }
}
