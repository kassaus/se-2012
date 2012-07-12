using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GesColecoesWeb.Data
{
    public class UserRow : Row
    {
        public long idUser { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
    }
}
