using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.Mvc;
using Business.RestaurantOnline;

namespace Web.RestaurantOnline.Controllers
{
    public class DishController : ApiBaseController
    {
        public HttpResponseMessage Index()
        {
            return ToJson(new { Status = "Successed" });
        }

        public HttpResponseMessage GetDishesByCategoryId(int id = 1)
        {
            DishesBuss buss = new DishesBuss();
            var dishes = buss.GetDishes(id);

            foreach(var item in dishes){
                item.ImgUrl = @"http://10.0.2.2:8081/Imgs/products/" + item.Id + ".jpeg";
            }

            return ToJson(new { Dishes = dishes });
        }

        [HttpGet]
        public HttpResponseMessage GetLastModifyTime()
        {
            DataChangingLogBuss buss = new DataChangingLogBuss();
            var lastModifyTime = buss.GetDateChangingTimeByType("Dishes");

            return ToJson(new { LastModifyTime = lastModifyTime });
        }
    }
}
