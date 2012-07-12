using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class ViewGeralViewRow : Row
    {
        public int idItem { get; set; }
        public string desTipoItem { get; set; }
        public string nomeEditor { get; set; }
        public string desExpTipoItem { get; set; }
        public string titulo { get; set; }
        public short ano { get; set; }
        public short edicao { get; set; }
        public string qrcode { get; set; }
        public string barcode { get; set; }
        public string autores { get; set; }
    }
}
