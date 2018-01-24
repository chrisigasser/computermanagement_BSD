using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ComputermanagementClasses
{
    public static class HardwareForRoomDetailsManager
    {
        public static void setNameAndDesc(HardwareForRoomDetails h)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", h.id + "" },
                        { "name", h.name },
                        { "rhdesc", h.desc }
            };
            RestCall.makePUTRestcall("/room/hardware", temp);
        }
        public static void setNetworkInfo(HardwareForRoomDetails h)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", h.id + "" },   
                        { "isDHCP", (h.networkInfo.isDHCP=="ja")?"1":"0" },
                        { "addInfo", h.networkInfo.furtherInfo}
            };
            RestCall.makePostRestcall("/room/hardware/networkInfo", temp);
        }
        public static void setWorking(HardwareForRoomDetails h)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", h.id + "" },
                        { "isWorking", (h.working)?"1":"0"}
            };
            RestCall.makePostRestcall("/room/hardware/works", temp);
        }
        public static void updateAnwendungen(HardwareForRoomDetails h, List<Anwendung> toAdd, List<Anwendung> toremove)
        {
            foreach (Anwendung atr in toremove)
            {
                System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", h.id + "" },
                        { "aID", atr.id+""}
                };
                RestCall.makeDELETERestcall("/room/hardware/application", temp);
            }
            foreach (Anwendung ata in toAdd)
            {
                System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", h.id + "" },
                        { "aID", ata.id+""}
                };
                RestCall.makePostRestcall("/room/hardware/application", temp);
            }

        }
    }
}
