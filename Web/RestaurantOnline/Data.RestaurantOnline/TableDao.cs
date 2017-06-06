using System;
using System.Collections.Generic;
using System.Data;
using MySql.Data.MySqlClient;

namespace Data.RestaurantOnline
{
    public class TableDao
    {
        public void InitTableInfo()
        {
            var sql = @"INSERT INTO tableinfo(tableNum,Jan,Feb,Mar,Apr,May,Jun,July,Aug,Sep,Oct,Nov,Dece) VALUES(@tableNum,@limit1,@limit2
            ,@limit3,@limit4,@limit5,@limit6,@limit7,@limit8,@limit9,@limit10,@limit11,@limit12)";

            var limitaions = new List<String>();
            limitaions.Add("1111111111111111111111111111111");
            limitaions.Add("11111111111111111111111111111");
            limitaions.Add("1111111111111111111111111111111");
            limitaions.Add("111111111111111111111111111111");
            limitaions.Add("1111111111111111111111111111111");//5

            limitaions.Add("111111111111111111111111111111");
            limitaions.Add("1111111111111111111111111111111");
            limitaions.Add("1111111111111111111111111111111");
            limitaions.Add("111111111111111111111111111111");
            limitaions.Add("1111111111111111111111111111111");//10

            limitaions.Add("111111111111111111111111111111");
            limitaions.Add("1111111111111111111111111111111");

            for (int i = 1; i < 51;i++){
                MySqlParameter[] paras = new MySqlParameter[13];
                paras[0] = new MySqlParameter("@tableNum", MySqlDbType.Int16);
                paras[0].Value = i;

                for (int j = 1; j <= 12;j++){
                    paras[j] = new MySqlParameter("@limit" + j, MySqlDbType.String);
                    paras[j].Value = limitaions[j - 1];
                }

                MySqlHelper.ExecuteNonQuery(MySqlHelper.Conn,CommandType.Text,sql,paras);
            }
        }

        public DataSet GetTableNumList()
        {
            var sql = @"SELECT tableNum FROM TableInfo ORDER BY id";

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, null);

            return ds;
        }
    }
}
