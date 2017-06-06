using System;
using System.Collections.Generic;
namespace Model.RestaurantOnline
{
    public class OrderResultModel
    {
        public bool IsSuccess{get;set;}
        public string OrderCode{get;set;}
        public decimal OrderAmount{get;set;}
        public List<string> ErrorReasons{get;set;}
    }
}
