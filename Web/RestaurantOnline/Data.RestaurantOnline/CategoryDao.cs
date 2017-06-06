using System;
using System.Data;
namespace Data.RestaurantOnline
{
    public class CategoryDao
    {
        public DataSet GetCategories()
        {
            DataSet ds = null;
            ds = MySqlHelper.GetDataSet(MySqlHelper.Conn, CommandType.Text, "Select * from Categories");

            return ds;
        }
    }
}
