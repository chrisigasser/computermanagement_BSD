using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public class Room
    {
        [JsonProperty("name")]
        public string name { get; set; }
        [JsonProperty("id")]
        public string id { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        public Room()
        {

        }
        public Room(string id, string name, string type)
        {
            this.id = id;
            this.name = name;
            this.type = type;
        }
    }
}
