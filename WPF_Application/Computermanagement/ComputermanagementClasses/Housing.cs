using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
   public class Housing
    {
        [JsonProperty("id")]
        public string id { get; set; }
        [JsonProperty("name")]
        public string name { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        [JsonProperty("rooms")]
        public List<Room> rooms { get; set; }
        public Housing()
        {

        }
        public Housing(string id, string name, string type, List<Room> rooms)
        {
            this.id = id;
            this.name = name;
            this.type = type;
            this.rooms = rooms;
        }
    }
}
