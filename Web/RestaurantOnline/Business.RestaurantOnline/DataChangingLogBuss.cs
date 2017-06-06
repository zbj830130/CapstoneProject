using System;
using System.Collections.Generic;
using System.Data;
using Data.RestaurantOnline;
using Model.RestaurantOnline;

namespace Business.RestaurantOnline
{
    public class DataChangingLogBuss
    {
        private DataChagingLogDao dao = new DataChagingLogDao();

        public string GetDateChangingTimeByType(string type)
        {
            var ds = dao.GetDateChangingTimeByType(type);
            var lastModfiyTime = DateTime.Now;

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                lastModfiyTime = DateTime.Parse(ds.Tables[0].Rows[0][0].ToString());
            }

            return lastModfiyTime.ToString("yyyy-MM-dd HH:mm:ss");
        }

        public List<BannerName> GetBannerPaths()
        {
            var ds = dao.GetBannerPaths();
            var imgPaths = new List<BannerName>();

            if (ds != null && ds.Tables != null && ds.Tables.Count > 0 && ds.Tables[0].Rows.Count > 0)
            {
                foreach (DataRow dr in ds.Tables[0].Rows)
                {
                    var bannerName = new BannerName();
                    var imgPath = dr[0].ToString();
                    bannerName.ImgUrl = System.Web.Configuration.WebConfigurationManager.AppSettings["bannerPaths"] + imgPath;
                    imgPaths.Add(bannerName);
                }
            }

            return imgPaths;
        }
    }
}
