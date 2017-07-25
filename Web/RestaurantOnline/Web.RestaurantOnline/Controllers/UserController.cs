using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Business.RestaurantOnline;

namespace Web.RestaurantOnline.Controllers
{
    public class UserController : Controller
    {
        UserBuss buss = new UserBuss();
        OrderBuss oBuss = new OrderBuss();

        public ActionResult Index(string callbackUrl = null)
        {
            ViewBag.Title = "Bojin's Restaurant Booking Online";
            ViewData["callbackUrl"] = callbackUrl;
            return View();
        }

        public JsonResult IsUsernameExisted(string userName)
        {
            if (buss.IsUserNameExisted(userName) == true)
            {
                var result = new { result = true };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
            else
            {
                var result = new { result = false };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
        }

        public JsonResult Registing(string reg_username, string reg_password)
        {
            if (buss.CreateUser(reg_username, reg_password) == true)
            {
                if (Request.Cookies["shoppingCart"] != null)
                {
                    HttpCookie aCookie = Request.Cookies["shoppingCart"];
                    aCookie.Expires = DateTime.Now.AddDays(-1);
                    Response.Cookies.Add(aCookie);
                }

                var userId = buss.IsUserNameAndPwdCorrect(reg_username, reg_password);
                Session["userId"] = userId;
                Session["userName"] = reg_username;

                var result = new { result = true };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
            else
            {
                var result = new { result = false };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
        }

        public JsonResult Login(string log_username, string log_password)
        {
            var userId = buss.IsUserNameAndPwdCorrect(log_username, log_password);

            if (userId > 0)
            {
                Session["userId"] = userId;
                Session["userName"] = log_username;
                var result = new { result = true };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
            else
            {
                var result = new { result = false };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
        }

        public JsonResult GetUserName()
        {
            if (Session["userId"] != null && Session["userName"] != null)
            {
                var result = new { result = true, userName = Session["userName"].ToString() };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
            else
            {
                var result = new { result = false };
                return Json(result, JsonRequestBehavior.AllowGet);
            }
        }

        public JsonResult Logout()
        {
            if (Request.Cookies["shoppingCart"] != null)
	        {
	            HttpCookie aCookie = Request.Cookies["shoppingCart"];
	            aCookie.Expires = DateTime.Now.AddDays(-1);
	            Response.Cookies.Add(aCookie);
	        }

            Session["userId"] = null;
            Session["userName"] = null;

            var result = new { result = true };
            return Json(result, JsonRequestBehavior.AllowGet);
        }

        [LoginAttribute("/User/OrderList")]
        public ActionResult OrderList()
        {
            ViewBag.Title = "Bojin's Restaurant Booking Online";
            return View();
        }

        [LoginAttribute("/User/OrderList")]
        public ActionResult GetAllOrders(int currentPage)
        {
            var userId = int.Parse(Session["userId"].ToString());

            var result = oBuss.GetOrderList(userId, currentPage, 0);
            return View(result);
        }

        [LoginAttribute("/User/OrderList")]
        public ActionResult GetReservedOrders(int currentPage)
        {
            var userId = int.Parse(Session["userId"].ToString());

            var result = oBuss.GetOrderList(userId, currentPage, 1);
            return View(result);
        }

        [LoginAttribute("/User/OrderList")]
        public ActionResult GetCanceledOrders(int currentPage)
        {
            var userId = int.Parse(Session["userId"].ToString());

            var result = oBuss.GetOrderList(userId, currentPage, 2);
            return View(result);
        }

        [LoginAttribute("/User/OrderList")]
        public ActionResult GetFinishedOrders(int currentPage)
        {
            var userId = int.Parse(Session["userId"].ToString());

            var result = oBuss.GetOrderList(userId, currentPage, 4);
            return View(result);
        }

        [LoginAttribute("/User/OrderList")]
        public ActionResult GetOrderDetails(int orderInfoId)
        {
            var userId = int.Parse(Session["userId"].ToString());
            var result = oBuss.GetOrderDetailInfo(userId, orderInfoId);
            return View(result);
        }

        [LoginAttribute("/User/OrderList")]
        public void CancleOrder(int orderInfoId)
        {
            var userId = int.Parse(Session["userId"].ToString());
            oBuss.CancelOrder(userId, orderInfoId);

        }
    }
}
