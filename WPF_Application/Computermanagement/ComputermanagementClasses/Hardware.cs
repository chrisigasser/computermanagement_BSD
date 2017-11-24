using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public class Hardware
    {
        public int id { get; set; }
        public string name { get; set; }
        public string logopath { get; set; }
        public string description { get; set; }
        public Hardware(int id, string name, string logopath, string description)
        {
            this.id = id;
            this.name = name;
            this.logopath = logopath;
            this.description = description;
        }
        public Hardware(string name, string logopath, string description)
        {
            this.name = name;
            this.logopath = logopath;
            this.description = description;

        }
        public Hardware(Hardware tocopy)
        {
            this.id = tocopy.id;
            this.name = tocopy.name;
            this.logopath = tocopy.logopath;
            this.description = tocopy.description;
        }

        public override string ToString()
        {
            return this.name;
        }
    }
}
