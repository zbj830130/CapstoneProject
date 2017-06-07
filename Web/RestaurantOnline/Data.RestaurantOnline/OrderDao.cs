using System;
using System.Data;
using System.Text;
using MySql.Data.MySqlClient;
using Model.RestaurantOnline;
using System.Collections.Generic;

namespace Data.RestaurantOnline
{
    public class OrderDao
    {
        private String[] monthColumeNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dece" };

        public void CreateOrder(OrderInfo orderInfo, List<OrderDetail> orderDetail, string tableMonthStatus)
        {

            using (MySqlConnection con = new MySqlConnection(MySqlHelper.Conn))
            {
                con.Open();
                using (MySqlTransaction tran = con.BeginTransaction())
                {
                    try
                    {
                        var sql = @"INSERT INTO OrderInfo(orderId,customerId,gender,lastName,mealTime,mealNumber,totalPrice,dishesCount,createTime,status,tableNumber)
		                      VALUES(@orderId,@customerId,@gender,@lastName,@mealTime,@mealNumber,@totalPrice,@dishesCount,@createTime,@status,@tableNumber)";

                        MySqlParameter[] paras = {
                            new MySqlParameter("@orderId",MySqlDbType.String),
                            new MySqlParameter("@customerId",MySqlDbType.Int32),
                            new MySqlParameter("@gender",MySqlDbType.String),

                            new MySqlParameter("@lastName",MySqlDbType.String),
                            new MySqlParameter("@mealTime",MySqlDbType.DateTime),

                            new MySqlParameter("@mealNumber",MySqlDbType.String),
                            new MySqlParameter("@totalPrice",MySqlDbType.Decimal),

                            new MySqlParameter("@dishesCount",MySqlDbType.Int32),
                            new MySqlParameter("@createTime",MySqlDbType.DateTime),
                            new MySqlParameter("@status",MySqlDbType.Int32),
                            new MySqlParameter("@tableNumber",MySqlDbType.Int32)
                        };
                        paras[0].Value = orderInfo.OrderId;
                        paras[1].Value = orderInfo.CustomerId;
                        paras[2].Value = orderInfo.Gender;
                        paras[3].Value = orderInfo.LastName;
                        paras[4].Value = orderInfo.MealTime;
                        paras[5].Value = orderInfo.MealNumber;
                        paras[6].Value = orderInfo.TotalPrice;
                        paras[7].Value = orderInfo.ItemsCount;
                        paras[8].Value = orderInfo.CreateTime;
                        paras[9].Value = orderInfo.Status;
                        paras[10].Value = orderInfo.TableNum;

                        MySqlHelper.ExecuteNonQuery(tran, CommandType.Text, sql, paras);

                        StringBuilder detailSql = new StringBuilder(1024);
                        detailSql.Append("INSERT INTO OrderDetail(orderId,dishId,dishEName,dishOName,dishPrice,dishCount) VALUES");

                        int i = 0;
                        List<MySqlParameter> detailParas = new List<MySqlParameter>();

                        foreach (var detail in orderDetail)
                        {
                            detailSql.Append("(");
                            detailSql.Append("@orderId" + i);
                            detailSql.Append(",@dishId" + i);
                            detailSql.Append(",@dishEName" + i);

                            detailSql.Append(",@dishOName" + i);
                            detailSql.Append(",@dishPrice" + i);
                            detailSql.Append(",@dishCount" + i);
                            detailSql.Append("),");

                            var orderIdPara = new MySqlParameter("@orderId" + i, MySqlDbType.String);
                            orderIdPara.Value = detail.OrderId;
                            detailParas.Add(orderIdPara);

                            var dishIdPara = new MySqlParameter("@dishId" + i, MySqlDbType.Int32);
                            dishIdPara.Value = detail.DishId;
                            detailParas.Add(dishIdPara);

                            var dishENamePara = new MySqlParameter("@dishEName" + i, MySqlDbType.String);
                            dishENamePara.Value = detail.DishEName;
                            detailParas.Add(dishENamePara);

                            var dishONamePara = new MySqlParameter("@dishOName" + i, MySqlDbType.String);
                            dishONamePara.Value = detail.DishOName;
                            detailParas.Add(dishONamePara);

                            var dishPricePara = new MySqlParameter("@dishPrice" + i, MySqlDbType.String);
                            dishPricePara.Value = detail.UnitPrice;
                            detailParas.Add(dishPricePara);

                            var dishCountPara = new MySqlParameter("@dishCount" + i, MySqlDbType.String);
                            dishCountPara.Value = detail.Count;
                            detailParas.Add(dishCountPara);

                            i++;
                        }

                        MySqlHelper.ExecuteNonQuery(tran, CommandType.Text, detailSql.Remove(detailSql.Length - 1, 1).ToString(), detailParas.ToArray());

                        var selectMonthName = monthColumeNames[orderInfo.MealTime.Month - 1];
                        sql = "UPDATE TableInfo SET " + selectMonthName + " = @monthStatus WHERE tableNum=@tableNum";

                        MySqlParameter[] parasTable = { new MySqlParameter("@monthStatus", MySqlDbType.String)
                                        ,new MySqlParameter("@tableNum", MySqlDbType.Int32) };
                        parasTable[0].Value = tableMonthStatus;
                        parasTable[1].Value = orderInfo.TableNum;

                        MySqlHelper.ExecuteNonQuery(MySqlHelper.Conn, CommandType.Text, sql, parasTable);

                        tran.Commit();
                        con.Close();
                    }
                    catch
                    {
                        tran.Rollback();
                        con.Close();
                    }
                }
            }
        }


        public DataSet GetAllOrders(int userId, int currentPage, int pageSize)
        {
            if (currentPage <= 1)
            {
                currentPage = 1;
            }

            if (pageSize <= 0)
            {
                pageSize = 10;
            }

            var sql = @"SELECT COUNT(1) FROM Orderinfo WHERE customerId=@userId; 
                        SELECT id,orderid,gender,lastName,mealTime,mealNumber
                        ,totalPrice,dishesCount,createTime,status 
                        FROM Orderinfo WHERE customerId=@userId1 ORDER BY id DESC limit "
                        + (currentPage - 1) * pageSize + "," + pageSize;


            MySqlParameter[] paras = { new MySqlParameter("@userId", MySqlDbType.Int32)
                                        ,new MySqlParameter("@userId1", MySqlDbType.Int32) };
            paras[0].Value = userId;
            paras[1].Value = userId;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetReservedOrders(int userId, int currentPage, int pageSize)
        {
            if (currentPage <= 1)
            {
                currentPage = 1;
            }

            if (pageSize <= 0)
            {
                pageSize = 10;
            }

            var sql = @"SELECT COUNT(1) FROM Orderinfo WHERE customerId=@userId and Status=1; 
                        SELECT id,orderid,gender,lastName,mealTime,mealNumber
                        ,totalPrice,dishesCount,createTime,status
                        FROM Orderinfo WHERE customerId=@userId1 and Status=1 ORDER BY id DESC limit "
                        + (currentPage - 1) * pageSize + "," + pageSize;


            MySqlParameter[] paras = { new MySqlParameter("@userId", MySqlDbType.Int32)
                                        ,new MySqlParameter("@userId1", MySqlDbType.Int32) };
            paras[0].Value = userId;
            paras[1].Value = userId;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetCancledOrders(int userId, int currentPage, int pageSize)
        {
            if (currentPage <= 1)
            {
                currentPage = 1;
            }

            if (pageSize <= 0)
            {
                pageSize = 10;
            }

            var sql = @"SELECT COUNT(1) FROM Orderinfo WHERE customerId=@userId and Status=2; 
                        SELECT id,orderid,gender,lastName,mealTime,mealNumber
                        ,totalPrice,dishesCount,createTime,status
                        FROM Orderinfo WHERE customerId=@userId1 and Status=2 ORDER BY id DESC limit "
                        + (currentPage - 1) * pageSize + "," + pageSize;


            MySqlParameter[] paras = { new MySqlParameter("@userId", MySqlDbType.Int32)
                                        ,new MySqlParameter("@userId1", MySqlDbType.Int32) };
            paras[0].Value = userId;
            paras[1].Value = userId;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetFinishedOrders(int userId, int currentPage, int pageSize)
        {
            if (currentPage <= 1)
            {
                currentPage = 1;
            }

            if (pageSize <= 0)
            {
                pageSize = 10;
            }

            var sql = @"SELECT COUNT(1) FROM Orderinfo WHERE customerId=@userId and Status=4; 
                        SELECT id,orderid,gender,lastName,mealTime,mealNumber
                        ,totalPrice,dishesCount,createTime,status
                        FROM Orderinfo WHERE customerId=@userId1 and Status=4 ORDER BY id DESC limit "
                        + (currentPage - 1) * pageSize + "," + pageSize;


            MySqlParameter[] paras = { new MySqlParameter("@userId", MySqlDbType.Int32)
                                        ,new MySqlParameter("@userId1", MySqlDbType.Int32) };
            paras[0].Value = userId;
            paras[1].Value = userId;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetOrderDetailInfo(int userId, int orderInfoId)
        {
            var sql = @"SELECT dishId,dishEName,dishOName,dishPrice,dishCount 
                        FROM OrderDetail AS od
						INNER JOIN OrderInfo AS oi
						ON od.orderId  = oi.orderId
						WHERE oi.customerId = @userId AND oi.id = @orderInfoId";

            MySqlParameter[] paras = { new MySqlParameter("@userId", MySqlDbType.Int32)
                                        ,new MySqlParameter("@orderInfoId", MySqlDbType.Int32) };
            paras[0].Value = userId;
            paras[1].Value = orderInfoId;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetOrderDetailInfo(String orderId)
        {
            var sql = @"SELECT dishId,dishEName,dishOName,dishPrice,dishCount 
                        FROM OrderDetail AS od
                        INNER JOIN OrderInfo AS oi
                        ON od.orderId  = oi.orderId
                        WHERE oi.orderId = @orderId";

            MySqlParameter[] paras = { new MySqlParameter("@orderId", MySqlDbType.String) };
            paras[0].Value = orderId;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public void CancelOrder(int userId, int orderInfoId, int tableNum, int monthNum, string monthStatus)
        {
            using (MySqlConnection con = new MySqlConnection(MySqlHelper.Conn))
            {
                con.Open();
                using (MySqlTransaction tran = con.BeginTransaction())
                {
                    try
                    {
                        var sql = @"UPDATE orderinfo set status= 4 
                        WHERE customerId = @userId AND id = @orderInfoId";

                        MySqlParameter[] paras = { new MySqlParameter("@userId", MySqlDbType.Int32)
                                        ,new MySqlParameter("@orderInfoId", MySqlDbType.Int32) };
                        paras[0].Value = userId;
                        paras[1].Value = orderInfoId;

                        MySqlHelper.ExecuteNonQuery(MySqlHelper.Conn, CommandType.Text, sql, paras);

                        var selectMonthName = monthColumeNames[monthNum - 1];

                        sql = "UPDATE TableInfo SET " + selectMonthName + " = @monthStatus WHERE tableNum=@tableNum";

                        MySqlParameter[] parasTable = { new MySqlParameter("@monthStatus", MySqlDbType.String)
                                        ,new MySqlParameter("@tableNum", MySqlDbType.Int32) };
                        parasTable[0].Value = monthStatus;
                        parasTable[1].Value = tableNum;

                        MySqlHelper.ExecuteNonQuery(MySqlHelper.Conn, CommandType.Text, sql, parasTable);

                        tran.Commit();
                        con.Close();
                    }
                    catch
                    {
                        tran.Rollback();
                        con.Close();
                    }
                }
            }
        }

        public void CreateOrderFromTablet(OrderInfo orderInfo, List<OrderDetail> orderDetail)
        {
            using (MySqlConnection con = new MySqlConnection(MySqlHelper.Conn))
            {
                con.Open();
                using (MySqlTransaction tran = con.BeginTransaction())
                {
                    try
                    {
                        var sql = @"INSERT INTO OrderInfo(orderId,mealTime,mealNumber,totalPrice,dishesCount,waiterId,tableNumber,createTime,status)
                              VALUES(@orderId,@mealTime,@mealNumber,@totalPrice,@dishesCount,@waiterId,@tableNumber,@createTime,@status)";

                        MySqlParameter[] paras = {
                            new MySqlParameter("@orderId",MySqlDbType.String),
                            new MySqlParameter("@mealTime",MySqlDbType.DateTime),

                            new MySqlParameter("@mealNumber",MySqlDbType.String),
                            new MySqlParameter("@totalPrice",MySqlDbType.Decimal),

                            new MySqlParameter("@dishesCount",MySqlDbType.Int32),
                            new MySqlParameter("@waiterId",MySqlDbType.Int32),
                            new MySqlParameter("@tableNumber",MySqlDbType.Int32),

                            new MySqlParameter("@createTime",MySqlDbType.DateTime),
                            new MySqlParameter("@status",MySqlDbType.Int32),
                        };
                        paras[0].Value = orderInfo.OrderId;
                        paras[1].Value = orderInfo.MealTime;
                        paras[2].Value = orderInfo.MealNumber;
                        paras[3].Value = orderInfo.TotalPrice;
                        paras[4].Value = orderInfo.ItemsCount;
                        paras[5].Value = orderInfo.WaiterId;
                        paras[6].Value = orderInfo.TableNum;
                        paras[7].Value = orderInfo.CreateTime;
                        paras[8].Value = orderInfo.Status;

                        MySqlHelper.ExecuteNonQuery(tran, CommandType.Text, sql, paras);

                        StringBuilder detailSql = new StringBuilder(1024);
                        detailSql.Append("INSERT INTO OrderDetail(orderId,dishId,dishEName,dishOName,dishPrice,dishCount) VALUES");

                        int i = 0;
                        List<MySqlParameter> detailParas = new List<MySqlParameter>();

                        foreach (var detail in orderDetail)
                        {
                            detailSql.Append("(");
                            detailSql.Append("@orderId" + i);
                            detailSql.Append(",@dishId" + i);
                            detailSql.Append(",@dishEName" + i);

                            detailSql.Append(",@dishOName" + i);
                            detailSql.Append(",@dishPrice" + i);
                            detailSql.Append(",@dishCount" + i);
                            detailSql.Append("),");

                            var orderIdPara = new MySqlParameter("@orderId" + i, MySqlDbType.String);
                            orderIdPara.Value = detail.OrderId;
                            detailParas.Add(orderIdPara);

                            var dishIdPara = new MySqlParameter("@dishId" + i, MySqlDbType.Int32);
                            dishIdPara.Value = detail.DishId;
                            detailParas.Add(dishIdPara);

                            var dishENamePara = new MySqlParameter("@dishEName" + i, MySqlDbType.String);
                            dishENamePara.Value = detail.DishEName;
                            detailParas.Add(dishENamePara);

                            var dishONamePara = new MySqlParameter("@dishOName" + i, MySqlDbType.String);
                            dishONamePara.Value = detail.DishOName;
                            detailParas.Add(dishONamePara);

                            var dishPricePara = new MySqlParameter("@dishPrice" + i, MySqlDbType.String);
                            dishPricePara.Value = detail.UnitPrice;
                            detailParas.Add(dishPricePara);

                            var dishCountPara = new MySqlParameter("@dishCount" + i, MySqlDbType.String);
                            dishCountPara.Value = detail.Count;
                            detailParas.Add(dishCountPara);

                            i++;
                        }

                        MySqlHelper.ExecuteNonQuery(tran, CommandType.Text, detailSql.Remove(detailSql.Length - 1, 1).ToString(), detailParas.ToArray());

                        tran.Commit();
                        con.Close();
                    }
                    catch (Exception e)
                    {
                        tran.Rollback();
                        con.Close();
                    }
                }
            }
        }

        public DataSet GetAvailableOrders()
        {
            var sql = @"SELECT id,orderid,gender,lastName,mealTime,mealNumber,waiterId,tableNumber,status
                        FROM Orderinfo WHERE status IN(1,3) ";

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, null);

            return ds;
        }

        public DataSet GetOrderInfoByOrderInfoId(int orderInfoId)
        {
            var sql = "SELECT * FROM OrderInfo WHERE Id=@id";
            MySqlParameter[] paras = { new MySqlParameter("@id", MySqlDbType.Int32) };

            paras[0].Value = orderInfoId;

            return MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);
        }

        public void UpdateOrderStatus(string orderId, OrderStatusEnum status)
        {
            var sql = @"UPDATE Orderinfo SET status = @status WHERE orderId=@orderId";

            MySqlParameter[] paras = { new MySqlParameter("@status", MySqlDbType.Int32)
                ,new MySqlParameter("@orderId", MySqlDbType.String) };

            paras[0].Value = status;
            paras[1].Value = orderId;

            MySqlHelper.ExecuteNonQuery(MySqlHelper.Conn, CommandType.Text, sql, paras);
        }

        public void UpdateOrderInfoFromTablet(OrderInfo orderInfo, List<OrderDetail> orderDetails)
        {
            using (MySqlConnection con = new MySqlConnection(MySqlHelper.Conn))
            {
                con.Open();
                using (MySqlTransaction tran = con.BeginTransaction())
                {
                    try
                    {
                        var sql = @"UPDATE Orderinfo SET mealNumber = @mealNumber,totalPrice = @totalPrice
                                    ,dishesCount = @dishesCount,waiterId = @waiterId,tableNumber=@tableNumber 
                                    WHERE orderId=@orderId";

                        MySqlParameter[] paras = {
                            new MySqlParameter("@mealNumber",MySqlDbType.String),
                            new MySqlParameter("@totalPrice",MySqlDbType.Decimal),

                            new MySqlParameter("@dishesCount",MySqlDbType.Int32),
                            new MySqlParameter("@waiterId",MySqlDbType.Int32),

                            new MySqlParameter("@tableNumber",MySqlDbType.Int32),
                            new MySqlParameter("@orderId",MySqlDbType.String),
                        };

                        paras[0].Value = orderInfo.MealNumber;
                        paras[1].Value = orderInfo.TotalPrice;
                        paras[2].Value = orderInfo.ItemsCount;
                        paras[3].Value = orderInfo.WaiterId;
                        paras[4].Value = orderInfo.TableNum;
                        paras[5].Value = orderInfo.OrderId;

                        MySqlHelper.ExecuteNonQuery(tran, CommandType.Text, sql, paras);

                        {
                            var deleSql = @"DELETE FROM OrderDetail WHERE orderId=@orderId";

                            List<MySqlParameter> deleParas = new List<MySqlParameter>();

                            var orderIdPara = new MySqlParameter("@orderId", MySqlDbType.String);
                            orderIdPara.Value = orderInfo.OrderId;
                            deleParas.Add(orderIdPara);

                            MySqlHelper.ExecuteNonQuery(tran, CommandType.Text, deleSql, deleParas.ToArray());

                        }

                        #region new item

                        if (orderDetails != null && orderDetails.Count > 0)
                        {

                            StringBuilder detailSql = new StringBuilder(1024);
                            detailSql.Append("INSERT INTO OrderDetail(orderId,dishId,dishEName,dishOName,dishPrice,dishCount) VALUES");

                            int i = 0;
                            List<MySqlParameter> detailParas = new List<MySqlParameter>();

                            foreach (var detail in orderDetails)
                            {
                                detailSql.Append("(");
                                detailSql.Append("@orderId" + i);
                                detailSql.Append(",@dishId" + i);
                                detailSql.Append(",@dishEName" + i);

                                detailSql.Append(",@dishOName" + i);
                                detailSql.Append(",@dishPrice" + i);
                                detailSql.Append(",@dishCount" + i);
                                detailSql.Append("),");

                                var orderIdPara = new MySqlParameter("@orderId" + i, MySqlDbType.String);
                                orderIdPara.Value = detail.OrderId;
                                detailParas.Add(orderIdPara);

                                var dishIdPara = new MySqlParameter("@dishId" + i, MySqlDbType.Int32);
                                dishIdPara.Value = detail.DishId;
                                detailParas.Add(dishIdPara);

                                var dishENamePara = new MySqlParameter("@dishEName" + i, MySqlDbType.String);
                                dishENamePara.Value = detail.DishEName;
                                detailParas.Add(dishENamePara);

                                var dishONamePara = new MySqlParameter("@dishOName" + i, MySqlDbType.String);
                                dishONamePara.Value = detail.DishOName;
                                detailParas.Add(dishONamePara);

                                var dishPricePara = new MySqlParameter("@dishPrice" + i, MySqlDbType.String);
                                dishPricePara.Value = detail.UnitPrice;
                                detailParas.Add(dishPricePara);

                                var dishCountPara = new MySqlParameter("@dishCount" + i, MySqlDbType.String);
                                dishCountPara.Value = detail.Count;
                                detailParas.Add(dishCountPara);

                                i++;
                            }

                            MySqlHelper.ExecuteNonQuery(tran, CommandType.Text, detailSql.Remove(detailSql.Length - 1, 1).ToString(), detailParas.ToArray());
                        }

                        #endregion

                        tran.Commit();
                        con.Close();
                    }
                    catch (Exception e)
                    {
                        tran.Rollback();
                        con.Close();
                    }
                }
            }
        }
    }
}
