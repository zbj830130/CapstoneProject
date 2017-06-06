using System;
using System.Collections.Generic;
using Data.RestaurantOnline;
using Model.RestaurantOnline;
using System.Data;

namespace Business.RestaurantOnline
{
    public class UserBuss
    {
        UserDao dao = new UserDao();

        public bool IsUserNameExisted(string userName)
        {
            DataSet ds = dao.GetUserName(userName);

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                return true;
            }

            return false;
        }

        public bool CreateUser(string userName, string pwd)
        {
            bool result = true;

            try
            {
                dao.CreateUser(userName, pwd);
            }
            catch
            {
                result = false;
            }

            return result;
        }

        public int IsUserNameAndPwdCorrect(string userName, string pwd)
        {
            int userId = 0;

            try
            {
                DataSet ds = dao.GetUserInfoByNameAndPwd(userName, pwd);

                if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
                {
                    userId = Int32.Parse(ds.Tables[0].Rows[0][0].ToString());
                }
            }
            catch
            {
                userId = 0;
            }

            return userId;
        }
    }
}
