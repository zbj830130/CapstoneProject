using System;
using System.Data;
using System.Text;
using MySql.Data.MySqlClient;
using Model.RestaurantOnline;
using System.Collections.Generic;

namespace Data.RestaurantOnline
{
    public class UserDao
    {
        public DataSet GetUserName(string userName)
        {
            var sql = @"SELECT userName 
                        FROM Users 
                        WHERE userName=@userName
                        ORDER BY id desc limit 1";

            MySqlParameter[] paras = { new MySqlParameter("@userName", MySqlDbType.String) };
            paras[0].Value = userName;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;
        }

        public void CreateUser(string userName,string pwd)
        {
            var now = DateTime.Now;

            var sql = @"INSERT INTO Users (userName,password,createDay,createTime)
                        VALUES(@userName,@password,@createDay,@createTime)";
            
            MySqlParameter[] paras = { new MySqlParameter("@userName", MySqlDbType.String)
                                        ,new MySqlParameter("@password", MySqlDbType.String)
                                        ,new MySqlParameter("@createDay", MySqlDbType.Date)
                                        ,new MySqlParameter("@createTime", MySqlDbType.DateTime)};
          
            paras[0].Value = userName;
            paras[1].Value = pwd;
            paras[2].Value = now;
            paras[3].Value = now;

            MySqlHelper.ExecuteNonQuery(MySqlHelper.Conn, CommandType.Text, sql, paras); 
        }

        public DataSet GetUserInfoByNameAndPwd(string userName,string pwd)
        {
            var sql = @"SELECT id
                        FROM Users 
                        WHERE userName=@userName AND password = @password
                        ORDER BY id desc limit 1";

            MySqlParameter[] paras = { new MySqlParameter("@userName", MySqlDbType.String)
                                        ,new MySqlParameter("@password", MySqlDbType.String)};
            paras[0].Value = userName;
            paras[1].Value = pwd;

            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, sql, paras);

            return ds;

        }
    }
}
