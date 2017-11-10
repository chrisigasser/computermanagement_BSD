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
            try
            {
                string response = RestCall.makeRestCall("/housing", "");
                Housing[] result = JsonConvert.DeserializeObject<Housing[]>(response);
                allHousings = new List<Housing>(result);
                return new List<Housing>(allHousings);
            }
            catch (Exception)
            {
                return null;
            }
            
        }

        public static Housing getAllRoomsForHousing(Housing housing)
        {
            try
            {
                string response = RestCall.makeRestCall("/housing/" + housing.id, "");

                Housing result = JsonConvert.DeserializeObject<Housing>(response);
                for (int i = 0; i < allHousings.Count; i++)
                {
                    if (result.id == allHousings[i].id)
                    {
                        allHousings[i] = result;
                        break;
                    }
                }
                return result;
            }
            catch (Exception)
            {
                return null;
            }
            
        }

        public static List<HardwareForRoom> getHardwareForRoom(Room room)
        {
            string response = RestCall.makeRestCall("/room/" + room.id, "");
            try
            {
                HardwareForRoom[] wholeHardware = JsonConvert.DeserializeObject<HardwareForRoom[]>(response);
                return new List<HardwareForRoom>(wholeHardware);
            }
            catch (Exception)
            {
                return null;
            }
        }
    }
}
