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
    public class GetViewGeral_ByID : IHttpHandler
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(GetViewGeral_ByID));

        public void ProcessRequest(HttpContext context)
        {
            Log.Debug("Processing...");

            context.Response.ContentType = "application/json";

            JavaScriptSerializer Serializer = new JavaScriptSerializer();

            string parameterID = context.Request.Params["ID"];
            Log.Debug("parameterID=" + parameterID);
            int ID;
            if (!string.IsNullOrEmpty(parameterID) && int.TryParse(parameterID, out ID))
            {
                Log.Debug("ID=" + ID);

                ViewGeralViewRow Row;
                if (DataBase.Default.ViewGeral.Get(ID, out Row))
                {
                    Log.Info("Row found!");
                    
                    //Serializer.
                    context.Response.Write(Serializer.Serialize(Row));
                }
                else
                {
                    Log.Error("Row not found...");

                    //DB error or row not found...
                    context.Response.Write("Error: Row not found...");
                }
            }
            else
            {
                Log.Error("ID field not supplied...");

                context.Response.Write("Error: ID field not supplied...");
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