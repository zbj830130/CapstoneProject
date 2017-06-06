using System;
using System.Collections.Generic;

namespace Model.RestaurantOnline
{
    public class BannersModel
    {
        public string LastModifyTime
        {
            get;
            set;
        }

        public List<BannerName> ImgPaths
        {
            get;
            set;
        }
    }

    public class BannerName
    {
        public string ImgUrl;
    }
}
