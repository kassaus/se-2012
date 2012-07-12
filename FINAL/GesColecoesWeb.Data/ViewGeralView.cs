using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class ViewGeralView : Table<ViewGeralViewRow>
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="DB"></param>
        public ViewGeralView(DataBase Parent)
            : base(Parent, "ViewGeral")
        {
        }

        /// <summary>
        /// Populate Row
        /// </summary>
        /// <param name="DR"></param>
        /// <returns></returns>
        protected override ViewGeralViewRow PopulateRow(SqlDataReader DR)
        {
            return new ViewGeralViewRow()
            {
                idItem = DR.GetInt32(DR.GetOrdinal("idItem")),
                desTipoItem = DR.GetString(DR.GetOrdinal("desTipoItem")),
                nomeEditor = DR.GetString(DR.GetOrdinal("nomeEditor")),
                desExpTipoItem = DR.GetString(DR.GetOrdinal("desExpTipoItem")),
                titulo = DR.GetString(DR.GetOrdinal("titulo")),
                ano = DR.GetInt16(DR.GetOrdinal("ano")),
                edicao = DR.GetInt16(DR.GetOrdinal("edicao")),
                qrcode = DR.GetString(DR.GetOrdinal("qrcode")),
                barcode = DR.GetString(DR.GetOrdinal("barcode"))
            };
        }

        public override bool List(out ViewGeralViewRow[] Rows)
        {
            return ExecuteReaderQueryList(
                "SELECT [idItem],[desTipoItem],[nomeEditor],[desExpTipoItem],[titulo],[ano],[edicao],[qrcode],[barcode] FROM [" + TableName + "]", null, out Rows);
        }

        public override bool Get(int idItem, out ViewGeralViewRow Row)
        {
            return ExecuteReaderQueryGet("SELECT [idItem],[desTipoItem],[nomeEditor],[desExpTipoItem],[titulo],[ano],[edicao],[qrcode],[barcode] FROM [" + TableName + "] WHERE [idItem] = @idItem;", new SqlParameter[] {
                    new SqlParameter("idItem", idItem)
                }, out Row);
        }

        public bool Get_Byqrcode(string qrcode, out ViewGeralViewRow Row)
        {
            return ExecuteReaderQueryGet("SELECT [idItem],[desTipoItem],[nomeEditor],[desExpTipoItem],[titulo],[ano],[edicao],[qrcode],[barcode],'' as [autores] FROM [" + TableName + "] WHERE [qrcode] = @qrcode;", new SqlParameter[] {
                    new SqlParameter("qrcode", qrcode)
                }, out Row);
        }

        public bool Get_Bybarcode(string barcode, out ViewGeralViewRow Row)
        {
            return ExecuteReaderQueryGet("SELECT [idItem],[desTipoItem],[nomeEditor],[desExpTipoItem],[titulo],[ano],[edicao],[qrcode],[barcode] FROM [" + TableName + "] WHERE [barcode] = @barcode;", new SqlParameter[] {
                    new SqlParameter("barcode", barcode)
                }, out Row);
        }

        public override bool Insert(ref ViewGeralViewRow Value)
        {
            throw new NotImplementedException();
        }

        public override bool Update(ref ViewGeralViewRow Value)
        {
            throw new NotImplementedException();
        }

        public override bool Delete(int ID)
        {
            throw new NotImplementedException();
        }
    }
}
