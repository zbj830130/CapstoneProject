using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Net.Http;
using Business.RestaurantOnline;
using Model.RestaurantOnline;
using System.Web.Http;
using System.Web.Script.Serialization;
using System.Text;

namespace Web.RestaurantOnline.Controllers
{
    public class CategoryController : ApiBaseController
    {
        public HttpResponseMessage GetAllCategories()
        {
            CategoriesBuss buss = new CategoriesBuss();
            var categories = buss.GetCategories();

            IEnumerable<CategoryModel> result = new List<CategoryModel>();

            if (categories != null && categories.Count > 0)
            {
                result = categories;
            }

            return ToJson(new {Categories = result });
        }

    }
}
