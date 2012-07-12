using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class ViewAutoresView : Table<ViewAutoresRow>
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="DB"></param>
        public ViewAutoresView(DataBase Parent)
            : base(Parent, "ViewAutores")
        {
        }

        /// <summary>
        /// Populate Row
        /// </summary>
        /// <param name="DR"></param>
        /// <returns></returns>
        protected override ViewAutoresRow PopulateRow(SqlDataReader DR)
        {
            return new ViewAutoresRow()
            {
                idItem = DR.GetInt32(DR.GetOrdinal("idItem")),
                prioridade = DR.GetInt32(DR.GetOrdinal("prioridade")),
                nomeAutor = DR.GetString(DR.GetOrdinal("nomeAutor"))
            };
        }

        public override bool List(out ViewAutoresRow[] Rows)
        {
            throw new NotImplementedException();
        }

        public override bool Get(int idUser, out ViewAutoresRow Row)
        {
            throw new NotImplementedException();
        }

        public bool Get_ByidItem(long idItem, out ViewAutoresRow[] Rows)
        {
            return ExecuteReaderQueryList("SELECT [idItem],[prioridade],[nomeAutor] FROM [" + TableName + "] WHERE [idItem] = @idItem ORDER BY [prioridade]", new SqlParameter[] {
                    new SqlParameter("idItem", idItem)
                }, out Rows);
        }

        public override bool Insert(ref ViewAutoresRow Value)
        {
            throw new NotImplementedException();
        }

        public override bool Update(ref ViewAutoresRow Value)
        {
            throw new NotImplementedException();
        }

        public override bool Delete(int ID)
        {
            throw new NotImplementedException();
        }

        public bool Delete_ByidItem(int idUser)
        {
            throw new NotImplementedException();
        }
    }
}
