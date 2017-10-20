using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public static class Hardwaremanager
    {

        private static HashSet<Hardware> allHardware = new HashSet<Hardware>(); //nur bis rest eingebaut wird
        public static void addHardware(Hardware newHardware)
        {
            if (!allHardware.Contains(newHardware))
            {
                allHardware.Add(new Hardware(newHardware));
            }
            else
            {
                throw new Exception("This ID is already taken");
            }
        }
        public static void removeHardwareByObject(Hardware toremove)
        {
            if (allHardware.Contains(toremove))
                allHardware.RemoveWhere(e => (e.id == toremove.id));
            else
                throw new Exception("The element with id " + toremove.id + " cant be removed because it does not exist!");
        }
        public static void removeHardwareByID(int id)
        {
            int removed = allHardware.RemoveWhere(e => (e.id == id));

            if(removed==0)
                throw new Exception("The element with id " + id + " cant be removed because it does not exist!");
        }
        public static List<Hardware> getallHardware()
        {
            RestCall.makeRestCall("http://192.168.194.150:8080/RESTOracle/rest/UserService/hardware", "");
            return allHardware.ToList();
        }
    }
}
