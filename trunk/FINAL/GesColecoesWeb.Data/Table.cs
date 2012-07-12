using System;
using System.Collections.Generic;
using System.Text;
using System.Data.SqlClient;
using log4net;

namespace GesColecoesWeb.Data
{
    public abstract class Table<T> where T : Row
    {
        public DataBase Parent { get; set; }
        public string TableName { get; set; }
        public ILog Log { get; set; }

        /// <summary>
        /// Generic base table
        /// </summary>
        /// <param name="DB"></param>
        /// <param name="TableName"></param>
        /// <param name="ImplementingTableType"></param>
        public Table(DataBase Parent, string TableName)
        {
            this.Log = LogManager.GetLogger(this.GetType());
            this.Parent = Parent;
            this.TableName = TableName;
        }

        /// <summary>
        /// Populates a row with the data reader values
        /// </summary>
        /// <param name="DR"></param>
        /// <returns></returns>
        protected abstract T PopulateRow(SqlDataReader DR);

        public abstract bool List(out T[] Rows);
        public abstract bool Get(int ID, out T Row);
        public abstract bool Insert(ref T Row);
        public abstract bool Update(ref T Row);
        public abstract bool Delete(int ID);

        /// <summary>
        /// Executes an sql query and gets the values as a list of the implemented type
        /// </summary>
        /// <param name="SqlQuery"></param>
        /// <param name="MySqlParameter"></param>
        /// <returns></returns>
        protected bool ExecuteReaderQueryList(string SqlQuery, SqlParameter[] SqlParameters, out T[] Rows)
        {
            SqlConnection Connection;
            SqlCommand Command;
            SqlDataReader DataReader;
            Rows = null;

            if (Parent.ExecuteReader(SqlQuery, SqlParameters, out Connection, out Command, out DataReader))
            {
                List<T> Temp = new List<T>();

                if (DataReader.HasRows)
                    while (DataReader.Read())
                        Temp.Add(PopulateRow(DataReader));

                DataReader.Close();
                Command.Dispose();
                Connection.Close();

                Rows = Temp.ToArray();
                return true;
            }
            else
                return false;
        }

        /// <summary>
        /// Returns the first result only
        /// </summary>
        /// <param name="SqlQuery"></param>
        /// <param name="MySqlParameters"></param>
        /// <returns></returns>
        protected bool ExecuteReaderQueryGet(string SqlQuery, SqlParameter[] SqlParameters, out T Row)
        {
            SqlConnection Connection;
            SqlCommand Command;
            SqlDataReader DataReader;
            Row = null;

            if (Parent.ExecuteReader(SqlQuery, SqlParameters, out Connection, out Command, out DataReader))
            {
                Row = null;

                if (DataReader.HasRows && DataReader.Read())
                    Row = PopulateRow(DataReader);

                DataReader.Close();
                Command.Dispose();
                Connection.Close();

                return true;
            }
            else
                return false;
        }

        /// <summary>
        /// Executes a non query command and sets the last inserted id on the referenciated value ID atribute
        /// </summary>
        /// <param name="SqlQuery"></param>
        /// <param name="MySqlParameters"></param>
        /// <param name="Value">Outputs the Last Inserted ID if any to the row ID field</param>
        /// <returns></returns>
        protected bool ExecuteNonQuery(string SqlQuery, SqlParameter[] SqlParameters, ref T Value)
        {
            long? LastInsertedId;
            int? Result = Parent.ExecuteNonQuery(SqlQuery, SqlParameters, out LastInsertedId);

            if (Result.HasValue && Result.Value > 0/* && LastInsertedId.HasValue*/)
            {
                //Value.ID = (int)LastInsertedId;
                return true;
            }
            else
                return false;
        }

        /// <summary>
        /// Executes a non query sql command
        /// </summary>
        /// <param name="SqlQuery"></param>
        /// <param name="MySqlParameters"></param>
        /// <returns></returns>
        protected bool ExecuteNonQuery(string SqlQuery, SqlParameter[] SqlParameters)
        {
            int? Result = Parent.ExecuteNonQuery(SqlQuery, SqlParameters);
            return Result.HasValue && Result.Value > 0;
        }
    }
}
