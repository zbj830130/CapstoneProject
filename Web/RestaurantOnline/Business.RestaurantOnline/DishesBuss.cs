using System;
using System.Data;
using System.Collections.Generic;
using Data.RestaurantOnline;
using Model.RestaurantOnline;

namespace Business.RestaurantOnline
{
    public class DishesBuss
    {
        DishesDao dao = new DishesDao();

        public List<DishesModel> GetDishes(int id)
        {
            var ds = dao.GetDishes(id);
            List<DishesModel> dishes = new List<DishesModel>();

            if(ds != null && ds.Tables != null && ds.Tables.Count >0 && ds.Tables[0].Rows.Count >0)
            {
                foreach (DataRow dr in ds.Tables[0].Rows)
                {
                    var dm = new DishesModel();
                    dm.Id = Int32.Parse(dr[0].ToString());
                    dm.CategoryId = Int32.Parse(dr[1].ToString());
                    dm.EName = dr[2].ToString();
                    dm.OtherName = dr[3].ToString();

                    dm.Price = Decimal.Parse(dr[4].ToString());
                    dm.EComment = dr[5].ToString();
                    dm.OtherComment = dr[6].ToString();

                    dm.ModifyTime = DateTime.Parse(dr[7].ToString())
                        .ToString("yyyy-MM-dd HH:mm:ss");
                    dm.CEName = dr[8].ToString();
                    dm.COName = dr[9].ToString();

                    dishes.Add(dm);
                }
            }

            return dishes;
        }

        public DishesModel GetSingleDishes(int dishId)
        {
            var ds = dao.GetSingleDishes(dishId);
            DishesModel dm = new DishesModel();

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                DataRow dr = ds.Tables[0].Rows[0];
                dm.Id = Int32.Parse(dr[0].ToString());
                dm.CategoryId = Int32.Parse(dr[1].ToString());
                dm.EName = dr[2].ToString();
                dm.OtherName = dr[3].ToString();

                dm.Price = Decimal.Parse(dr[4].ToString());
                dm.EComment = dr[5].ToString();
                dm.OtherComment = dr[6].ToString();

                dm.CEName = dr[7].ToString();
                dm.COName = dr[8].ToString();
            }

            return dm;
        }

        public List<DishesModel> GetDishesByIds(int[] ids)
        {
            var ds = dao.GetDishesByIds(ids);
            List<DishesModel> dishes = new List<DishesModel>();

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                foreach (DataRow dr in ds.Tables[0].Rows)
                {
                    var dm = new DishesModel();
                    dm.Id = Int32.Parse(dr[0].ToString());
                    dm.CategoryId = Int32.Parse(dr[1].ToString());
                    dm.EName = dr[2].ToString();
                    dm.OtherName = dr[3].ToString();

                    dm.Price = Decimal.Parse(dr[4].ToString());
                    dm.EComment = dr[5].ToString();
                    dm.OtherComment = dr[6].ToString();

                    dm.CEName = dr[7].ToString();
                    dm.COName = dr[8].ToString();

                    dishes.Add(dm);
                }
            }

            return dishes;
        }
    }
}
