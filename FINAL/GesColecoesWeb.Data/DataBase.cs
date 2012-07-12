using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Text;
using log4net;

namespace GesColecoesWeb.Data
{
    public class DataBase
    {
        static long InstanceCount = 0;
        static DataBase iDefault = new DataBase(Properties.Settings.Default.DefaultConnectionString);

        ILog Log;
        long InstanceID;

        private string ConnectionString;



        public ViewGeralView ViewGeral { get; set; }
        public UserFullBackupTable UserFullBackup { get; set; }
        public UserTable User { get; set; }
        public ViewAutoresView ViewAutores { get; set; } 



        public DataBase(String ConnectionString)
        {
            Log = LogManager.GetLogger(this.GetType());

            InstanceID = InstanceCount;
            Log.Info("DataBase[" + InstanceID + "]");

            this.ConnectionString = ConnectionString;

            this.ViewGeral = new ViewGeralView(this);
            this.UserFullBackup = new UserFullBackupTable(this);
            this.User = new UserTable(this);
            this.ViewAutores = new ViewAutoresView(this);  
            InstanceCount++;
        }

        /// <summary>
        /// Gets the default database object
        /// </summary>
        public static DataBase Default
        {
            get
            {
                return iDefault;
            }

        }

        private SqlConnection EstablishConnection()
        {
            try
            {
                return new SqlConnection(ConnectionString);
            }
            catch (Exception Ex)
            {
                Log.Error("Couldn't establish a connection in database instance " + InstanceID + "... Exception: " + Environment.NewLine + Ex);
                return null;
            }
        }

        /// <summary>
        /// Executes an sql reader command
        /// </summary>
        /// <param name="SqlCommand"></param>
        /// <returns></returns>
        public bool ExecuteReader(string SqlCommand, SqlParameter[] Parameters, out SqlConnection Connection, out SqlCommand Command, out SqlDataReader DataReader)
        {
            Connection = null;
            Command = null;
            DataReader = null;

            try
            {
                Connection = EstablishConnection();
                Command = new SqlCommand(SqlCommand, Connection);

                Connection.Open();
                Command.Prepare();

                if (Parameters != null)
                    Command.Parameters.AddRange(Parameters);

                DataReader = Command.ExecuteReader();

                return true;
            }
            catch (Exception Ex)
            {
                if (DataReader != null && !DataReader.IsClosed)
                    DataReader.Close();

                if (Command != null)
                    Command.Dispose();

                if (Connection != null)
                    Connection.Close();

                Log.Error(Ex.ToString());

                return false;
            }
            finally
            {
                //after closing the data reader the connection closes
            }
        }

        /// <summary>
        /// Executes a scalar command
        /// </summary>
        /// <param name="SqlCommand"></param>
        /// <returns></returns>
        public object ExecuteScalar(string SqlCommand, SqlParameter[] Parameters)
        {
            SqlConnection Connection = null;
            SqlCommand Command = null;

            try
            {
                Connection = EstablishConnection();
                Command = new SqlCommand(SqlCommand, Connection);

                Connection.Open();
                Command.Prepare();

                if (Parameters != null)
                    Command.Parameters.AddRange(Parameters);

                return Command.ExecuteScalar();
            }
            catch (Exception Ex)
            {
                Log.Error(Ex.ToString());

                return null;
            }
            finally
            {
                if (Command != null)
                    Command.Dispose();

                if (Connection != null)
                    Connection.Close();
            }
        }

        /// <summary>
        /// Executes a non query sql command
        /// </summary>
        /// <param name="SqlCommand"></param>
        /// <param name="Result"></param>
        /// <returns></returns>
        public int? ExecuteNonQuery(string SqlCommand, SqlParameter[] Parameters)
        {
            SqlConnection Connection = null;
            SqlCommand Command = null;

            try
            {
                Connection = EstablishConnection();
                Command = new SqlCommand(SqlCommand, Connection);

                Connection.Open();
                Command.Prepare();

                if (Parameters != null)
                    Command.Parameters.AddRange(Parameters);

                return Command.ExecuteNonQuery();
            }
            catch (Exception Ex)
            {
                Log.Error(Ex.ToString());

                return null;
            }
            finally
            {
                if (Command != null)
                    Command.Dispose();

                if (Connection != null)
                    Connection.Close();
            }
        }

        public int? ExecuteNonQuery(string SqlCommand, SqlParameter[] Parameters, out long? LastInsertedId)
        {
            SqlConnection Connection = null;
            SqlCommand Command = null;
            LastInsertedId = null;

            try
            {
                Connection = EstablishConnection();
                Command = new SqlCommand(SqlCommand, Connection);

                Connection.Open();
                Command.Prepare();

                if (Parameters != null)
                    Command.Parameters.AddRange(Parameters);

                int Result = Command.ExecuteNonQuery();
                //LastInsertedId = Command.LastInsertedId;

                return Result;
            }
            catch (Exception Ex)
            {
                Log.Error(Ex.ToString());

                LastInsertedId = null;
                return null;
            }
            finally
            {
                if (Command != null)
                    Command.Dispose();

                if (Connection != null)
                    Connection.Close();
            }
        }
    }
}
