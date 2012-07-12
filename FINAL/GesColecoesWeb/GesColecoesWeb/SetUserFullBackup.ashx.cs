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
    public class SetUserFullBackup : IHttpHandler
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(SetUserFullBackup));

        public void ProcessRequest(HttpContext context)
        {
            Log.Debug("Processing...");

            context.Response.ContentType = "application/json";

            JavaScriptSerializer Serializer = new JavaScriptSerializer();

            /*Search by idUser*/
            int idUser;
            if (!string.IsNullOrEmpty(context.Request.Params["idUser"]) && !string.IsNullOrEmpty(context.Request.Params["data"]) && int.TryParse(context.Request.Params["idUser"], out idUser))
            {
                string data = context.Request.Params["data"];

                Log.Debug("idUser=" + idUser + ", data=" + data);

                //JSON deserialize!
                UserFullBackupRow[] Rows = (UserFullBackupRow[])Serializer.Deserialize(data, typeof(UserFullBackupRow[]));

                if (Rows != null)
                {
                    Log.Debug("Deserialization sucessfull!");

                    //Delete old data, if any
                    if (DataBase.Default.UserFullBackup.Delete_ByidUser(idUser))
                    {
                        //Ok
                        Log.Debug("Old user data deleted!");
                    }
                    else
                    {
                        //error
                        Log.Info("Can't delete user data or there is no data to delete.");
                        //context.Response.Write(Serializer.Serialize("Error: Can't delete user data..."));
                    }


                    //Now: Insert the new data!!!
                    int Count = 0;
                    for (int i = 0; i < Rows.Length; i++)
                    {
                        if (DataBase.Default.UserFullBackup.Insert(ref Rows[i]))
                            Count++;
                    }

                    //return the insert count!
                    context.Response.Write(Serializer.Serialize(Count));
                }
                else
                {
                    Log.Error("Deserialization error...");
                    context.Response.Write("Error: Can't deserialize...");
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