using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.Mvc;
using Business.RestaurantOnline;

namespace Web.RestaurantOnline.Controllers
{
    public class TableController : ApiBaseController
    {
        public HttpResponseMessage GetInitTableInfo()
        {
            TableBuss buss = new TableBuss();
            buss.InitTableInfo();
            return ToJson(new { Status = "Successed" });
        }

        public HttpResponseMessage GetTableNumList()
        {
            TableBuss buss = new TableBuss();
            var nums = buss.GetTableNumList();
            return ToJson(new { TableNums = nums });
        }
    }
}
