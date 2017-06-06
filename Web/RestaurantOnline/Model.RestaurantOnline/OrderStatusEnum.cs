using System;
namespace Model.RestaurantOnline
{
    public enum OrderStatusEnum
    {
        [System.ComponentModel.Description("Reserved")]
        Order = 1,
        [System.ComponentModel.Description("Cancled")]
        Cancle = 2,
        [System.ComponentModel.Description("Dining")]
        Dining = 3,
        [System.ComponentModel.Description("Finished")]
        Finish = 4
    }
}
