using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public static class Anwendungsmanager
    {
        private static HashSet<Anwendung> allAnwendung = new HashSet<Anwendung>();
        public static void addAnwendung(Anwendung newAnwendung)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "aname", newAnwendung.name },
                        { "adesc", newAnwendung.desc }
            };
            RestCall.makePostRestcall("/application/", temp);
        }
        public static void removeAnwendungByObject(Anwendung toremove)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "aid", ""+toremove.id }
            };
            RestCall.makeDELETERestcall("/application/", temp);
        }
        public static void updateAnwendung(Anwendung toupdate)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "aid", ""+toupdate.id },
                        { "aname", toupdate.name },
                        { "adesc", toupdate.desc }
            };
            RestCall.makePUTRestcall("/application/", temp);
        }
        public static List<Anwendung> getallAnwendung()
        {
            string response = RestCall.makeRestCall("/application/", "");
            Anwendung[] result = JsonConvert.DeserializeObject<Anwendung[]>(response);
            allAnwendung = new HashSet<Anwendung>(result);
            return allAnwendung.ToList();
        }
    }
}
