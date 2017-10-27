using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public class Hardware
    {
        [JsonProperty("id")]
        public int id { get; set; }
        [JsonProperty("name")]
        public string name { get; set; }
        [JsonProperty("logo")]
        public string logo { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        [JsonProperty("desc")]
        public string desc { get; set; }
        public Hardware()
        {

        }
        public Hardware(int id, string name, string logopath, string type, string desc)
        {
            this.id = id;
            this.name = name;
            this.logo = logopath;
            this.type = type;
            this.desc = desc;
        }
        public Hardware(string name, string logo, string type, string desc)
        {
            this.name = name;
            this.logo = logo;
            this.type = type;
            this.desc = desc;
        }
        public Hardware(Hardware tocopy)
        {
            this.id = tocopy.id;
            this.name = tocopy.name;
            this.logo = tocopy.logo;
            this.type = tocopy.type;
        }

        public override string ToString()
        {
            return this.name;
        }
    }
}
