using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public class NetworkInfo
    {
        [JsonProperty("isDHCP")]
        public string isDHCP { get; set; }
        [JsonProperty("type")]
        public string type { get; set; }
        [JsonProperty("furtherInfo")]
        public string furtherInfo { get; set; }
        public NetworkInfo()
        {
        }

    }
}
