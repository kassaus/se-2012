using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class UserFullBackupTable : Table<UserFullBackupRow>
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="DB"></param>
        public UserFullBackupTable(DataBase Parent)
            : base(Parent, "UserFullBackup")
        {
        }

        /// <summary>
        /// Populate Row
        /// </summary>
        /// <param name="DR"></param>
        /// <returns></returns>
        protected override UserFullBackupRow PopulateRow(SqlDataReader DR)
        {
            return new UserFullBackupRow()
            {
                id = DR.GetInt64(DR.GetOrdinal("id")),
                idUser = DR.GetInt32(DR.GetOrdinal("idUser")),
                barcode = DR.GetString(DR.GetOrdinal("barcode")),
                TipoItem = DR.GetString(DR.GetOrdinal("TipoItem")),
                titulo = DR.GetString(DR.GetOrdinal("titulo")),
                Editor = DR.GetString(DR.GetOrdinal("Editor")),
                Autores = DR.GetString(DR.GetOrdinal("Autores")),
                ano = DR.GetString(DR.GetOrdinal("ano")),
                edicao = DR.GetString(DR.GetOrdinal("edicao")),
                qrcode = DR.GetString(DR.GetOrdinal("qrcode")),
                ExtTipoItem = DR.GetString(DR.GetOrdinal("ExtTipoItem")),
                Obs = DR.GetString(DR.GetOrdinal("Obs"))
            };
        }

        public override bool List(out UserFullBackupRow[] Rows)
        {
            return ExecuteReaderQueryList(
                "SELECT [id],[idUser],[barcode],[TipoItem],[titulo],[Editor],[Autores],[ano],[edicao],[qrcode],[ExtTipoItem],[Obs] FROM [" + TableName + "]", null, out Rows);
        }

        public bool List_ByidUser(Int32 idUser, out UserFullBackupRow[] Rows)
        {
            return ExecuteReaderQueryList(
                "SELECT [id],[idUser],[barcode],[TipoItem],[titulo],[Editor],[Autores],[ano],[edicao],[qrcode],[ExtTipoItem],[Obs] FROM [" + TableName + "] WHERE [idUser] = @idUser", new SqlParameter[]{
                    new SqlParameter("idUser", idUser)                
                }, out Rows);
        }

        public override bool Get(int id, out UserFullBackupRow Row)
        {
            return ExecuteReaderQueryGet("SELECT [id],[idUser],[barcode],[TipoItem],[titulo],[Editor],[Autores],[ano],[edicao],[qrcode],[ExtTipoItem],[Obs] FROM [" + TableName + "] WHERE [id] = @id", new SqlParameter[] {
                    new SqlParameter("id", id)
                }, out Row);
        }

        public override bool Insert(ref UserFullBackupRow Value)
        {
            return ExecuteNonQuery("INSERT INTO [" + TableName + "] ([idUser],[barcode],[TipoItem],[titulo],[Editor],[Autores],[ano],[edicao],[qrcode],[ExtTipoItem],[Obs]) VALUES (@idUser,@barcode,@TipoItem,@titulo,@Editor,@Autores,@ano,@edicao,@qrcode,@ExtTipoItem,@Obs)", new SqlParameter[] {
                new SqlParameter("idUser", Value.idUser),
                new SqlParameter("barcode", Value.barcode),
                new SqlParameter("TipoItem", Value.TipoItem),
                new SqlParameter("titulo", Value.titulo),
                new SqlParameter("Editor", Value.Editor),
                new SqlParameter("Autores", Value.Autores),
                new SqlParameter("ano", Value.ano),
                new SqlParameter("edicao", Value.edicao),
                new SqlParameter("qrcode", Value.qrcode),
                new SqlParameter("ExtTipoItem", Value.ExtTipoItem),
                new SqlParameter("Obs", Value.Obs)
            }, ref Value);
        }

        public override bool Update(ref UserFullBackupRow Value)
        {
            throw new NotImplementedException();
        }

        public override bool Delete(int ID)
        {
            throw new NotImplementedException();
        }

        public bool Delete_ByidUser(int idUser)
        {
            return ExecuteNonQuery("DELETE FROM [" + TableName + "] WHERE [idUser] = @idUser", new SqlParameter[] {
                new SqlParameter("idUser", idUser)
            });
        }
    }
}
