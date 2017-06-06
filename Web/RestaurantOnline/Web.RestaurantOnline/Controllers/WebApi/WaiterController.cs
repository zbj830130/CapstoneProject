using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.Mvc;
using Business.RestaurantOnline;

namespace Web.RestaurantOnline.Controllers
{
    public class WaiterController : ApiBaseController
    {
        public HttpResponseMessage GetUWaiterByPwd(string pwd)
        {
            int userId = 0;
            WaiterBuss buss = new WaiterBuss();
            userId = buss.GetUserIdByPwd(pwd);

            return ToJson(new { userId = userId });
        }
    }
}