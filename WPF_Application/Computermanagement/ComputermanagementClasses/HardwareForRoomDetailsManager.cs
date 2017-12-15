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
        public static void setName(HardwareForRoomDetails h)
        {
            System.Collections.Specialized.NameValueCollection temp = new NameValueCollection() {
                        { "hid", h.id + "" },
                        { "name", h.name },
                        { "rhdesc", h.desc }
            };
            RestCall.makePUTRestcall("/room/hardware", temp);
        }
    }
}
