using System;
using System.Collections.Generic;

namespace Model.RestaurantOnline
{
    [Serializable]
    public class OrderInfoFromAndroidModel
    {
        public String OrderId;
        public int WaiterId;
        public String TableHeadcount;
        public String TableNum;
        public List<OrderDishInfo> Dishes;

    }

    [Serializable]
    public class OrderDishInfo
    {
        public int DishId;
        public int Qty;
        public double UnitPrice;
        public String EName;
        public String OName;
    }

}
