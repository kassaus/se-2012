using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class UserFullBackupRow : Row
    {
        public long id { get; set; }
        public int idUser { get; set; }
        public string barcode { get; set; }
        public string TipoItem { get; set; }
        public string titulo { get; set; }
        public string Editor { get; set; }
        public string Autores { get; set; }
        public string ano { get; set; }
        public string edicao { get; set; }
        public string qrcode { get; set; }
        public string ExtTipoItem { get; set; }
        public string Obs { get; set; }
    }
}
