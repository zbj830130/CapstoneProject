using System;
using System.Data;
using System.Text;
using MySql.Data.MySqlClient;
using System.Collections.Generic;

namespace Data.RestaurantOnline
{
    public class DishesDao
    {
        public DataSet GetDishes(int id)
        {
            var sql = @"SELECT d.*,c.eName AS cEName,c.otherName AS cOName 
                        FROM Dishes AS d 
                        INNER JOIN Categories AS c 
                        ON d.categoryId = c.id 
                        WHERE d.categoryId=@categoryId
                        ORDER BY d.categoryId,d.id";

            MySqlParameter[] paras = { new MySqlParameter("@categoryId", MySqlDbType.Int32) };
            paras[0].Value = id;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetSingleDishes(int dishId)
        {
            var sql = @"SELECT d.*,c.eName AS cEName,c.otherName AS cOName 
                        FROM Dishes AS d 
                        INNER JOIN Categories AS c 
                        ON d.categoryId = c.id 
                        WHERE d.id=@dishId
                        ORDER BY d.categoryId,d.id";

            MySqlParameter[] paras = { new MySqlParameter("@dishId", MySqlDbType.Int32) };
            paras[0].Value = dishId;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetDishesByIds(int[] ids)
        {
            var sql = @"SELECT d.*,c.eName AS cEName,c.otherName AS cOName 
                        FROM Dishes AS d 
                        INNER JOIN Categories AS c 
                        ON d.categoryId = c.id 
                        WHERE d.id in({0})
                        ORDER BY d.categoryId,d.id";

            StringBuilder sb = new StringBuilder(1024);

            foreach (var id in ids)
            {
                sb.Append(id);
                sb.Append(",");
            }

            sql = String.Format(sql, sb.Remove(sb.Length - 1, 1).ToString());

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql);

            return ds;
        }
    }
}
