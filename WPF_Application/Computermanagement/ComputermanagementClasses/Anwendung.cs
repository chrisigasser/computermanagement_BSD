using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public class Anwendung : IComparable<Anwendung>
    {
        public int id { get; set; }
        public string name { get; set; }
        public string details { get; set; }

        public Anwendung(int id, string name, string details)
        {
            this.id = id;
            this.name = name;
            this.details = details;
        }
        public Anwendung(string name, string details)
        {
            this.name = name;
            this.details = details;
        }
        public Anwendung(Anwendung tocopy)
        {
            this.id = tocopy.id;
            this.name = tocopy.name;
            this.details = tocopy.details;
        }

        public int CompareTo(Anwendung other)
        {
            return (other.id - this.id);
        }
    }
}
