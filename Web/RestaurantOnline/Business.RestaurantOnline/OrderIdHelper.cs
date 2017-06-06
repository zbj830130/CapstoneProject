using System;
namespace Business.RestaurantOnline
{
    public class OrderIdHelper
    {
        private static object locker = new object();

        private static int sn = 0;

        public static string NextBillNumber(int source)
        {
            lock (locker)
            {
                if (sn == 9999)
                    sn = 0;
                else
                    sn++;
                return source.ToString() + DateTime.Now.ToString("yyyyMMddHHmmss") +sn.ToString().PadLeft(4, '0');
            }
        }

        //singlton design
        private OrderIdHelper() { }
    }

}
