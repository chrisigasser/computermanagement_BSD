using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    class HardwareForRoom
    {
        [JsonProperty("id")]
        public int id { get; set; }
        [JsonProperty("name")]
        public string name { get; set; }
        [JsonProperty("hdesc")]
        public string hdesc { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        [JsonProperty("room")]
        public int room { get; set; }
        [JsonProperty("hname")]
        public string hname { get; set; }
        [JsonProperty("hardware")]
        public int hardware { get; set; }
        [JsonProperty("hlogo")]
        public string hlogo { get; set; }

        public HardwareForRoom(int id, string name, string hdesc, string type, int room, string hname, int hardware, string hlogo)
        {
            this.id = id;
            this.name = name;
            this.hdesc = hdesc;
            this.type = type;
            this.room = room;
            this.hname = hname;
            this.hardware = hardware;
            this.hlogo = hlogo;
        }

    }
}
