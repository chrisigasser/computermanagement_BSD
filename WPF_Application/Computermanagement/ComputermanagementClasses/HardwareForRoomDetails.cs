using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    //keine Vererbung wegen Problemen mit dem JSon Parser bei der Vererbung
    public class HardwareForRoomDetails
    {
        [JsonProperty("id")]
        public int id { get; set; }
        [JsonProperty("name")]
        public string name { get; set; }
        [JsonProperty("desc")]
        public string hdesc { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        [JsonProperty("room")]
        public int room { get; set; }
        [JsonProperty("hardware")]
        public int hardware { get; set; }
        [JsonProperty("applications")]
        public List<Anwendung> applications { get; set; }
        [JsonProperty("networkInfo")]
        public NetworkInfo networkInfo { get; set; }
        public HardwareForRoomDetails()
        {

        }
    }
}
