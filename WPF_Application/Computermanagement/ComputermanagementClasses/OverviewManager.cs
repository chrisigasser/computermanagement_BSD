using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{

    public static class OverviewManager
    {
        private static List<Housing> allHousings;
        public static List<Housing> getAllHousings()
        {
            string response = RestCall.makeRestCall("/housing", "");
            Housing[] result = JsonConvert.DeserializeObject<Housing[]>(response);
            allHousings = new List<Housing>(result);
            return allHousings.ToList();
        }

        public static List<Room> getAllRoomsForHousing(Housing housing)
        {
            string response = RestCall.makeRestCall("/housing/" + housing.id, "");
            Housing result = JsonConvert.DeserializeObject<Housing>(response);
            for (int i = 0; i < allHousings.Count; i++)
            {
                if(result.id == allHousings[i].id)
                {
                    allHousings[i] = result;
                    break;
                }
            }
            return result.rooms;
        }
    }
}
