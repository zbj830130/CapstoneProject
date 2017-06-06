using System;
using System.Web;
using System.Web.Mvc;
namespace Web.RestaurantOnline.Controllers
{
    [AttributeUsage(AttributeTargets.Method | AttributeTargets.Class, Inherited = true, AllowMultiple = true)]
    public class LoginAttribute : FilterAttribute, IAuthorizationFilter
    {
        private string authUrl = string.Empty;
       
        public LoginAttribute(string callbackUrl)
        {
            authUrl = "/User/Index?callbackUrl=" + callbackUrl; 
        }


        public void OnAuthorization(AuthorizationContext filterContext)
        {
            if (filterContext.HttpContext.Session["userId"] == null 
                || filterContext.HttpContext.Session["userName"] == null)
            {
                filterContext.HttpContext.Session["userId"] = null;
                filterContext.HttpContext.Session["userName"] = null;
                filterContext.Result = new RedirectResult(authUrl);
            }
        }
    }
}
