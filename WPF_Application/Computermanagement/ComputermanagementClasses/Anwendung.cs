using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public class Anwendung : IComparable<Anwendung>
    {
        [JsonProperty("id")]
        public int id { get; set; }
        [JsonProperty("name")]
        public string name { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        [JsonProperty("desc")]
        public string desc { get; set; }
        
        public Anwendung(int id, string name, string desc)
        {
            this.id = id;
            this.name = name;
            this.desc = desc;
        }
        public Anwendung(string name, string desc)
        {
            this.name = name;
            this.desc = desc;
        }
        public Anwendung(Anwendung tocopy)
        {
            this.id = tocopy.id;
            this.name = tocopy.name;
            this.desc = tocopy.desc;
        }

        public Anwendung()
        {
        }

        public int CompareTo(Anwendung other)
        {
            return (other.id - this.id);
        }
    }
}
