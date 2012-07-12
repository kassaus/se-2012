using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Security;
using log4net;

namespace GesColecoesWeb
{
    public class Global : System.Web.HttpApplication
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(Global));

        void Application_Start(object sender, EventArgs e)
        {
            // Code that runs on application startup
            log4net.Config.XmlConfigurator.Configure();

            Log.Debug("Application start...");
        }

        void Application_End(object sender, EventArgs e)
        {
            //  Code that runs on application shutdown
            Log.Debug("Application end.");
        }

        void Application_Error(object sender, EventArgs e)
        {
            // Code that runs when an unhandled error occurs
            Log.Debug("Application error...");
        }

        void Session_Start(object sender, EventArgs e)
        {
            // Code that runs when a new session is started

        }

        void Session_End(object sender, EventArgs e)
        {
            // Code that runs when a session ends. 
            // Note: The Session_End event is raised only when the sessionstate mode
            // is set to InProc in the Web.config file. If session mode is set to StateServer 
            // or SQLServer, the event is not raised.

        }

    }
}
