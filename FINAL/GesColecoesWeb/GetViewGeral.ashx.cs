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
    public class GetViewGeral : IHttpHandler
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(GetViewGeral));

        public void ProcessRequest(HttpContext context)
        {
            Log.Debug("Processing...");

            context.Response.ContentType = "application/json";

            JavaScriptSerializer Serializer = new JavaScriptSerializer();

            /*Search by qrcode*/
            if (!string.IsNullOrEmpty(context.Request.Params["qrcode"]))
            {
                string qrcode = context.Request.Params["qrcode"];
                Log.Debug("qrcode=" + qrcode);


                ViewGeralViewRow Row;
                if (DataBase.Default.ViewGeral.Get_Byqrcode(qrcode, out Row))
                {
                    Log.Info("Row found!");
                    ViewAutoresRow[] Rows;
                    DataBase.Default.ViewAutores.Get_ByidItem(Row.idItem, out Rows);
                    String strAutores="";
                    
                    foreach (ViewAutoresRow ra in Rows){
                        if (strAutores.CompareTo("")==0)
                            strAutores = ra.nomeAutor;
                        else
                            strAutores = strAutores + ", " + ra.nomeAutor;
                    }

                    Row.autores = strAutores;
                    context.Response.Write(Serializer.Serialize(Row));//Serialize!
                }
                else
                {
                    //DB error or row not found...
                    Log.Error("Row not found...");
                    context.Response.Write("Error: Row not found...");
                }
            }
            else
                /*Search by barcode*/
                if (!string.IsNullOrEmpty(context.Request.Params["barcode"]))
                {
                    string barcode = context.Request.Params["barcode"];
                    Log.Debug("barcode=" + barcode);


                    ViewGeralViewRow Row;
                    if (DataBase.Default.ViewGeral.Get_Bybarcode(barcode, out Row))
                    {
                        Log.Info("Row found!");

                        ViewAutoresRow[] Rows;
                        DataBase.Default.ViewAutores.Get_ByidItem(Row.idItem, out Rows);
                        String strAutores = "";

                        foreach (ViewAutoresRow ra in Rows)
                        {
                            if (strAutores.CompareTo("") == 0)
                                strAutores = ra.nomeAutor;
                            else
                                strAutores = strAutores + ", " + ra.nomeAutor;
                        }

                        Row.autores = strAutores;

                        context.Response.Write(Serializer.Serialize(Row));//Serialize!
                    }
                    else
                    {
                        //DB error or row not found...
                        Log.Error("Row not found...");
                        context.Response.Write("Error: Row not found...");
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