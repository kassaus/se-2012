using System;
using System.Collections.Generic;
using System.Web;
using System.Web.Script.Serialization;
using GesColecoesWeb.Data;
using log4net;

namespace GesColecoesWeb
{
    /// <summary>
    /// Summary description for Handler
    /// </summary>
    public class Login : IHttpHandler
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(Login));

        public void ProcessRequest(HttpContext context)
        {
            Log.Debug("Processing...");

            context.Response.ContentType = "application/json";

            if (!string.IsNullOrEmpty(context.Request.Params["UserName"]) && !string.IsNullOrEmpty(context.Request.Params["Password"]))
            {
                string UserName = context.Request.Params["UserName"];
                string Password = context.Request.Params["Password"];

                Log.Debug("UserName=" + UserName + ", Password=" + Password);

                UserRow Row;
                if (DataBase.Default.User.Get_ByUserName(UserName, out Row) && Row != null)
                {
                    Log.Info("User Row found!");

                    if (Row.Password == Password)
                        context.Response.Write(Row.idUser);
                    else
                        context.Response.Write("Error: Invalid password.");
                }
                else
                {
                    Log.Error("User Row not found...");

                    //DB error or row not found...
                    context.Response.Write("Error: User Row not found...");
                }
            }
            else
            {
                Log.Error("UserName/Password field(s) not supplied...");
                context.Response.Write("Error: UserName/Password field(s) not supplied...");
            }

            Log.Debug("Finished.");
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}