using System;
using System.Data;
using System.Collections.Generic;
using Data.RestaurantOnline;
using Model.RestaurantOnline;

namespace Business.RestaurantOnline
{
    public class CategoriesBuss
    {
        CategoryDao dao = new CategoryDao();

        public List<CategoryModel> GetCategories()
        {
            var ds = dao.GetCategories();
            List<CategoryModel> categories = null;

            if (ds != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                categories = new List<CategoryModel>();
                foreach (DataRow dr in ds.Tables[0].Rows)
                {
                    var cm = new CategoryModel();
                    cm.Id = Int32.Parse(dr[0].ToString());
                    cm.EName = dr[1].ToString();
                    cm.OtherName = dr[2].ToString();

                    categories.Add(cm);
                }
            }

            return categories;
        }
    }
}
