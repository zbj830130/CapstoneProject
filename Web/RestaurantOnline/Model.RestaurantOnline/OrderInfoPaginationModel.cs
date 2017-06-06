using System;
using System.Collections.Generic; 
namespace Model.RestaurantOnline
{
    public class OrderInfoPaginationModel
    {
        public int TotalPage {get;set;}
        public int PageSize {get;set;}
        public int CurrentPage {get;set;}

        public List<OrderInfo> OrderList{get;set;}
    }
}
