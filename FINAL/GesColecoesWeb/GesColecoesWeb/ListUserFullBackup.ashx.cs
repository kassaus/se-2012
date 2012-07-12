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
    public class ListUserFullBackup : IHttpHandler
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(ListUserFullBackup));

        public void ProcessRequest(HttpContext context)
        {
            Log.Debug("Processing...");

            context.Response.ContentType = "application/json";

            JavaScriptSerializer Serializer = new JavaScriptSerializer();

            /*Search by idUser*/
            int idUser;
            if (!string.IsNullOrEmpty(context.Request.Params["idUser"]) && int.TryParse(context.Request.Params["idUser"], out idUser))
            {
                Log.Debug("idUser=" + idUser);

                UserFullBackupRow[] Rows;
                if (DataBase.Default.UserFullBackup.List_ByidUser(idUser, out Rows))
                {
                    Log.Info("Row found!");
                    context.Response.Write(Serializer.Serialize(Rows));//Serialize!
                }
                else
                {
                    //DB error or row not found...
                    Log.Error("Row not found...");
                    context.Response.Write(Serializer.Serialize("Error: Row not found..."));
                }
            }
            /*Invalid parameters supplied*/
            else
            {
                Log.Error("Invalid parameters supplied...");
                context.Response.Write("Error: Invalid parameters supplied...");
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