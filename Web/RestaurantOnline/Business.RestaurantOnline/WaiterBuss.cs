using System;
using System.Data;
using Data.RestaurantOnline;

namespace Business.RestaurantOnline
{
    public class WaiterBuss
    {
        public int GetUserIdByPwd(string pwd)
        {
            WaiterDao dao = new WaiterDao();
            DataSet ds = dao.GetUserIdByPwd(pwd);
            int waiterId = 0;

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                waiterId = int.Parse(ds.Tables[0].Rows[0][0].ToString());
            }

            return waiterId;
        }
    }
}
