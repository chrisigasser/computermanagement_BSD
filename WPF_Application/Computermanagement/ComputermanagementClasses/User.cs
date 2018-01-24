using Newtonsoft.Json;

namespace ComputermanagementClasses
{
    public class User
    {
        [JsonProperty("id")]
        public int id { get; set; }
        [JsonProperty("uname")]
        public int uname { get; set; }
        [JsonProperty("type")]
        public int type { get; set; }
        [JsonProperty("desc")]
        public int desc { get; set; }
        [JsonProperty("managedByAD")]
        public bool managedByAD { get; set; }
        public User()
        {

        }
    }
}