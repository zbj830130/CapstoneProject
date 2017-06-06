using System;
using System.Runtime.Serialization;

namespace Model.RestaurantOnline
{
    [DataContract]
    public class ShoppingCartItem
    {
        [DataMember(Order = 0)]
        public int ItemID { get; set; }

        [DataMember(Order = 1)]
        public int Qty { get; set; }

        [DataMember(Order = 2)]
        public decimal Price { get; set; }

        [DataMember(Order = 3)]
        public string EName { get; set; }

        [DataMember(Order = 3)]
        public string OName { get; set; }
    }
}
