using System;
using System.Data;
using MySql.Data.MySqlClient;

namespace Data.RestaurantOnline
{
    public class WaiterDao
    {
        public DataSet GetUserIdByPwd(string pwd)
        {
            var sql = @"SELECT id FROM WaitersInfo WHERE pwd = @pwd";
            MySqlParameter[] paras = new MySqlParameter[1];
            paras[0] = new MySqlParameter("@pwd", MySqlDbType.String);
            paras[0].Value = pwd;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }
    }
}
