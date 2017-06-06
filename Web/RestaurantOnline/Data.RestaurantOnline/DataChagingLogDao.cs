using System;
using System.Data;
using MySql.Data.MySqlClient;

namespace Data.RestaurantOnline
{
    public class DataChagingLogDao
    {
        public DataSet GetDateChangingTimeByType(string type)
        {
            var sql = @"SELECT lastModifyTime FROM DataChangingLog 
                        WHERE Type=@type";

            MySqlParameter[] paras = { new MySqlParameter("@type", MySqlDbType.String) };
            paras[0].Value = type;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public DataSet GetBannerPaths()
        {
            var sql = "SELECT imgName FROM BannerNames WHERE status = 1";

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, null);

            return ds;
        }
    }
}
