using System;
namespace Model.RestaurantOnline
{
    public class OrderDetail
    {
        public int Id{get;set;}
        public string OrderId{get;set;}
        public int DishId{get;set;}
        public string DishEName{get;set;}
        public string DishOName{get;set;}
        public decimal UnitPrice{get;set;}
        public int Count{get;set;}
    }
}
