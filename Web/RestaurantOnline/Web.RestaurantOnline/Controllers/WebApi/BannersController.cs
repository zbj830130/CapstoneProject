using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.Mvc;
using Business.RestaurantOnline;
using Model.RestaurantOnline;

namespace Web.RestaurantOnline.Controllers
{
    public class BannersController : ApiBaseController
    {
        public HttpResponseMessage GetBannerPictures()
        {
            DataChangingLogBuss buss = new DataChangingLogBuss();
            var model = new BannersModel();

            model.LastModifyTime = buss.GetDateChangingTimeByType("Banner");
            model.ImgPaths = buss.GetBannerPaths();

            return ToJson(model);
        }

    }
}
