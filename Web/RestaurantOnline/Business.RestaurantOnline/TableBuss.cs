using System;
using System.Collections.Generic;
using System.Data;
using Data.RestaurantOnline;
using Model.RestaurantOnline;

namespace Business.RestaurantOnline
{
    public class TableBuss
    {
        private TableDao dao = new TableDao();

        public void InitTableInfo()
        {
            dao.InitTableInfo();
        }

        public List<TableInfoModel> GetTableNumList()
        {
            var ds = dao.GetTableNumList();
            List<TableInfoModel> tableNums = new List<TableInfoModel>();

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                foreach (DataRow dr in ds.Tables[0].Rows)
                {
                    var item = new TableInfoModel();
                    item.TableNum = dr[0].ToString();

                    tableNums.Add(item);
                }
            }

            return tableNums;
        }
    }
}
