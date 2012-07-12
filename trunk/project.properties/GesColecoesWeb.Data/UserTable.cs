using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class UserTable : Table<UserRow>
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="DB"></param>
        public UserTable(DataBase Parent)
            : base(Parent, "User")
        {
        }

        /// <summary>
        /// Populate Row
        /// </summary>
        /// <param name="DR"></param>
        /// <returns></returns>
        protected override UserRow PopulateRow(SqlDataReader DR)
        {
            return new UserRow()
            {
                idUser = DR.GetInt32(DR.GetOrdinal("idUser")),
                UserName = DR.GetString(DR.GetOrdinal("UserName")),
                Password = DR.GetString(DR.GetOrdinal("Password"))
            };
        }

        public override bool List(out UserRow[] Rows)
        {
            throw new NotImplementedException();
        }

        public override bool Get(int idUser, out UserRow Row)
        {
            return ExecuteReaderQueryGet("SELECT [idUser],[UserName],[Password] FROM [" + TableName + "] WHERE [idUser] = @idUser", new SqlParameter[] {
                    new SqlParameter("idUser", idUser)
                }, out Row);
        }

        public bool Get_ByUserName(string UserName, out UserRow Row)
        {
            return ExecuteReaderQueryGet("SELECT [idUser],[UserName],[Password] FROM [" + TableName + "] WHERE [UserName] = @UserName", new SqlParameter[] {
                    new SqlParameter("UserName", UserName)
                }, out Row);
        }

        public override bool Insert(ref UserRow Value)
        {
            throw new NotImplementedException();
        }

        public override bool Update(ref UserRow Value)
        {
            throw new NotImplementedException();
        }

        public override bool Delete(int ID)
        {
            throw new NotImplementedException();
        }

        public bool Delete_ByidUser(int idUser)
        {
            throw new NotImplementedException();
        }
    }
}
