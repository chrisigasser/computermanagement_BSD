using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace ComputermanagementClasses
{
    public static class Hardwaremanager
    {

        private static HashSet<Hardware> allHardware = new HashSet<Hardware>(); 
        public static void addHardware(Hardware newHardware)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hname", newHardware.name },
                        { "hdesc", newHardware.desc },
                        { "hlogo", newHardware.logo }
            };
            RestCall.makePostRestcall("/hardware/", temp);
        }
        public static void removeHardwareByObject(Hardware toremove)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", ""+toremove.id }
            };
            RestCall.makeDELETERestcall("/hardware/", temp);
        }
        public static void updateHardware(Hardware toupdate)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", ""+toupdate.id },
                        { "hname", toupdate.name },
                        { "hdesc", toupdate.desc },
                        { "hlogo", toupdate.logo }
            };
            RestCall.makePUTRestcall("/hardware/", temp);
        }
        public static void removeHardwareByID(int id)
        {
            int removed = allHardware.RemoveWhere(e => (e.id == id));

            if(removed==0)
                throw new Exception("The element with id " + id + " cant be removed because it does not exist!");
        }
        public static List<Hardware> getallHardware()
        {
            string response = RestCall.makeRestCall("/hardware", "");
            Hardware[] result = JsonConvert.DeserializeObject<Hardware[]>(response);
            allHardware = new HashSet<Hardware>(result);
            return allHardware.ToList();
        }
    }
}
