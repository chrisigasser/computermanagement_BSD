using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Computermanagement
{
    /// <summary>
    /// Interaktionslogik für HardwareForRoomDetails.xaml
    /// </summary>
    public partial class HardwareForRoomDetails : Window
    {
        ComputermanagementClasses.HardwareForRoomDetails hfrdetails = null;
        public HardwareForRoomDetails(ComputermanagementClasses.HardwareForRoomDetails h)
        {
            InitializeComponent();
            this.hfrdetails = h;
            displayInfo();
        }

        private void displayInfo()
        {
            if (this.hfrdetails != null)
            {

            }
        }
    }
}
