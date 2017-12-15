using ComputermanagementClasses;
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
        List<Anwendung> listOfAnwendung = null;
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
                this.listOfAnwendung = hfrdetails.applications;
                this.listbox_anwendungen.ItemsSource = this.listOfAnwendung;
                this.texbox_desc.Text = this.hfrdetails.desc;
                this.texbox_name.Text = this.hfrdetails.name;
                this.texbox_type.Text = this.hfrdetails.type;
                this.texbox_working.Text = (this.hfrdetails.working) ? "ja" : "nein";
                //this.texbox_working.Text = this.hfrdetails.working + "";
                if (this.hfrdetails.networkInfo != null)
                {
                    this.texbox_furtherinformation.Text = this.hfrdetails.networkInfo.furtherInfo;
                    this.texbox_isdhcp.Text = (this.hfrdetails.networkInfo.isDHCP == "1") ? "ja" : "nein";
                }
                else
                {
                    this.texbox_furtherinformation.Text = "not defined";
                    this.texbox_isdhcp.Text = "not defined";
                }
                
            }
        }

        private void nameLostFocus(object sender, RoutedEventArgs e)
        {
            this.hfrdetails.name = this.texbox_name.Text;
            HardwareForRoomDetailsManager.setName(this.hfrdetails);
        }
    }
}
