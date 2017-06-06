using System;
namespace Model.RestaurantOnline
{
    public class OrderInfo
    {
        public int Id { get; set; }
        public string OrderId { get; set; }
        public int CustomerId { get; set; }
        public string Gender { get; set; }
        public string LastName { get; set; }
        public DateTime MealTime { get; set; }
        public string MealNumber { get; set; }
        public decimal TotalPrice { get; set; }
        public int ItemsCount { get; set; }
        public int WaiterId { get; set; }
        public int TableNum { get; set; }
        public DateTime CreateTime { get; set; }
        public OrderStatusEnum Status { get; set; }
    }
}
