using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class ViewAutoresRow : Row
    {
        public int idItem { get; set; }
        public int prioridade { get; set; }
        public string nomeAutor { get; set; }
    }
}
