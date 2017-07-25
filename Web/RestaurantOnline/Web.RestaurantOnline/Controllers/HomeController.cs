using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Configuration;
using Business.RestaurantOnline;
using Model.RestaurantOnline;
using System.Web.Script.Serialization;

namespace Web.RestaurantOnline.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            ViewBag.Title = "Bojin's Restaurant Booking Online";
            
            CategoriesBuss categoriesBuss = new CategoriesBuss();
            List<CategoryModel> categories = categoriesBuss.GetCategories();

            return View(categories);
        }

        public ActionResult DishList(int id)
        {
            DishesBuss buss = new DishesBuss();
            var dishes = buss.GetDishes(id);

            return View(dishes);
        }

        public ActionResult MiniShoppingCartSingleItem(int id)
        {
            DishesBuss buss = new DishesBuss();
            var dishInfo = buss.GetSingleDishes(id);

            return View(dishInfo);
        }


        public ActionResult OperatShoppingCart(int id, int qty)
        {
            var shoppingCartItems = OperateShoppingCart(id,qty);

            return RedirectToAction("LoadMiniShoppingCart");
        }

        public JsonResult OperatShoppingCartForOrderConfirm(int id, int qty)
        {
            var shoppingCartItems = OperateShoppingCart(id, qty);

            var totalPrice = decimal.Zero;
            var totalItems = 0;

            foreach (var item in shoppingCartItems)
            {
                totalPrice += (item.Price * item.Qty);
                totalItems += item.Qty;
            }

            var result = new { totalPrice = totalPrice, totalItems = totalItems };
            return Json(result, JsonRequestBehavior.AllowGet);

        }

        public ActionResult LoadMiniShoppingCart()
        {
            var shoppingCartItems = GetShoppingCartInfoFromCookie();

            return View(shoppingCartItems);
        }

        public JsonResult GetShoppingCartItemCount()
        {
            var shoppingCartItems = GetShoppingCartInfoFromCookie();

            int itemCount = 0;

            foreach (var item in shoppingCartItems)
            {
                itemCount += item.Qty;
            }

            var result = new { itemCount = itemCount };

            return Json(result, JsonRequestBehavior.AllowGet);
        }

        public JsonResult GetAvailableTableNum(int mealNum, DateTime mealDate)
        {
            TableBuss buss = new TableBuss();
            var availableTableNums = buss.GetTableNumListByTableHeadcount(mealNum, mealDate);

            var result = new { tableNums = availableTableNums };
            return Json(result, JsonRequestBehavior.AllowGet);
        }

        [LoginAttribute("/Home/ConfirmOrder")]
        public ActionResult ConfirmOrder()
        {
            ViewBag.Title = "Bojin's Restaurant Booking Online";
            return View();
        }

        public ActionResult OrderDetails()
        {
            List<ShoppingCartItem> shoppingCartItems = GetShoppingCartInfoFromCookie();

            return View(shoppingCartItems);
        }

        [LoginAttribute("/Home/ConfirmOrder")]
        [HttpPost]
        public ActionResult OrderCompleted(string gender, string lastName, string mealTime,
                                           string mealNumber, string tableNum)
        {
            ViewBag.Title = "Bojin's Restaurant Booking Online";
            var orderInfo = new OrderInfo();
            orderInfo.Gender = gender;
            orderInfo.LastName = lastName;
            orderInfo.MealTime = Convert.ToDateTime(mealTime);
            orderInfo.MealNumber = mealNumber;

            orderInfo.CreateTime = DateTime.Now;
            orderInfo.Status = OrderStatusEnum.Order;
            orderInfo.CustomerId = Int32.Parse(Session["userId"].ToString());
            orderInfo.TableNum = Convert.ToInt32(tableNum);

            var result = new OrderResultModel();
            var items = GetShoppingCartInfoFromCookie();

            if (items.Count == 0)
            {
                result.IsSuccess = false;
                return View(result);
            }

            decimal totalPrice = 0;
            int count = 0;

            foreach (var item in items)
            {
                count += item.Qty;
                totalPrice += (item.Qty * item.Price);
            }

            orderInfo.TotalPrice = totalPrice;
            orderInfo.ItemsCount = count;
            var orderId = String.Empty;

            var createResult = new OrderBuss().CreateOrder(orderInfo, items, out orderId);


            if (createResult == true)
            {
                HttpCookie aCookie = Request.Cookies["shoppingCart"];
                aCookie.Expires = DateTime.Now.AddDays(-1);
                Response.Cookies.Add(aCookie);

                result.IsSuccess = true;
                result.OrderCode = orderId;
                result.OrderAmount = orderInfo.TotalPrice;
            }
            else
            {
                result.IsSuccess = false;
            }

            return View(result);
        }

        #region private
        private List<ShoppingCartItem> GetShoppingCartInfoFromCookie()
        {
            List<ShoppingCartItem> shoppingCartItems = new List<ShoppingCartItem>();
            var jser = new JavaScriptSerializer();
            if (Request.Cookies["shoppingCart"] != null)
            {
                var cookieJson = Request.Cookies["shoppingCart"].Value;
                shoppingCartItems = jser.Deserialize<List<ShoppingCartItem>>(cookieJson);

                if (shoppingCartItems != null && shoppingCartItems.Count > 0)
                {

                    int[] ids = shoppingCartItems.Select(p => p.ItemID).ToArray();

                    DishesBuss buss = new DishesBuss();
                    var details = buss.GetDishesByIds(ids);

                    foreach (var item in shoppingCartItems)
                    {
                        foreach (var detailItem in details)
                        {
                            if (item.ItemID == detailItem.Id)
                            {
                                item.OName = detailItem.OtherName;
                                break;
                            }
                        }
                    }
                }
            }

            return shoppingCartItems;
        }

        private List<ShoppingCartItem> OperateShoppingCart(int id, int qty){
            DishesBuss buss = new DishesBuss();
            List<ShoppingCartItem> shoppingCartItems = null;

            var jser = new JavaScriptSerializer();
            if (Request.Cookies["shoppingCart"] != null)
            {
                var cookieJson = Request.Cookies["shoppingCart"].Value;
                shoppingCartItems = jser.Deserialize<List<ShoppingCartItem>>(cookieJson);
            }

            if (shoppingCartItems == null || shoppingCartItems.Count == 0)
            {
                shoppingCartItems = new List<ShoppingCartItem>();
            }

            var isContaint = false;
            var index = 0;
            foreach (var item in shoppingCartItems)
            {
                if (item.ItemID == id && qty > 0)
                {
                    item.Qty = qty;
                    isContaint = true;
                    break;
                }

                if (item.ItemID == id && qty == 0)
                {
                    isContaint = true;
                    break;
                }

                index++;
            }

            if (qty == 0)
            {
                shoppingCartItems.RemoveAt(index);
            }


            if (isContaint == false)
            {
                var dishInfo = buss.GetSingleDishes(id);

                ShoppingCartItem cartitem = new ShoppingCartItem();
                cartitem.ItemID = id;
                cartitem.Qty = qty;
                cartitem.EName = dishInfo.EName;
                //cartitem.OName = dishInfo.OtherName;
                cartitem.Price = dishInfo.Price;

                shoppingCartItems.Add(cartitem);
            }

            var json = jser.Serialize(shoppingCartItems);

            Response.Cookies["shoppingCart"].Value = json;
            Response.Cookies["shoppingCart"].Expires = DateTime.Now.AddHours(1);

            return shoppingCartItems;
        }
        #endregion
    }
}
