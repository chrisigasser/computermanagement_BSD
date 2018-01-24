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
        List<Anwendung> allPossibleAnwendungen = null;
        List<Anwendung> toAdd = null;
        List<Anwendung> toremove = null;
        ComputermanagementClasses.HardwareForRoomDetails hfrdetails = null;
        public HardwareForRoomDetails(ComputermanagementClasses.HardwareForRoomDetails h)
        {
            InitializeComponent();
            
            this.hfrdetails = h;
            this.toAdd = new List<Anwendung>();
            this.toremove = new List<Anwendung>();
            displayInfo();
        }

        private void updateAllAnwendungen()
        {
            this.allPossibleAnwendungen = Anwendungsmanager.getallAnwendung();
            this.allPossibleAnwendungen.RemoveAll(isAlreadyInstalled);
        }

        private bool isAlreadyInstalled(Anwendung obj)
        {
            foreach (Anwendung a in this.hfrdetails.applications)
            {
                if (a.name.Equals(obj.name))
                {
                    return true;
                }
            }
            return false;
        }

        private void displayInfo()
        {
            if (this.hfrdetails != null)
            {
                this.updateAllAnwendungen();
                this.listOfAnwendung = hfrdetails.applications;
                this.listbox_anwendungen.ItemsSource = this.listOfAnwendung;
                this.listbox_anwendungen.Items.Refresh();
                this.listbox_Allanwendungen.ItemsSource = this.allPossibleAnwendungen;
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

        private void Button_Click(object sender, RoutedEventArgs e)
        {  
            this.hfrdetails.name = this.texbox_name.Text;
            this.hfrdetails.desc = this.texbox_desc.Text;
            this.hfrdetails.working = (this.texbox_working.Text.ToLower() == "ja") ? true : false;
            this.hfrdetails.networkInfo = new NetworkInfo();
            this.hfrdetails.networkInfo.isDHCP = this.texbox_isdhcp.Text;
            this.hfrdetails.networkInfo.furtherInfo = this.texbox_furtherinformation.Text;
            HardwareForRoomDetailsManager.setNameAndDesc(this.hfrdetails);
            HardwareForRoomDetailsManager.setNetworkInfo(this.hfrdetails);
            HardwareForRoomDetailsManager.setWorking(this.hfrdetails);
            HardwareForRoomDetailsManager.updateAnwendungen(this.hfrdetails, this.toAdd, this.toremove);
            this.Close();
        }

        private void btn_add_Click(object sender, RoutedEventArgs e)
        {
            if (this.listbox_Allanwendungen.SelectedIndex != -1)
            {
                Anwendung a = (Anwendung)this.listbox_Allanwendungen.SelectedItem;
                this.hfrdetails.applications.Add(a);
                this.toremove.Remove(a);
                this.toAdd.Add(a);
                this.displayInfo();
            }
        }

        private void btn_remove_Click(object sender, RoutedEventArgs e)
        {
            if (this.listbox_anwendungen.SelectedIndex != -1)
            {
                Anwendung a = (Anwendung)this.listbox_anwendungen.SelectedItem;
                this.toAdd.Remove(a);
                this.toremove.Add(a);
                this.hfrdetails.applications.Remove(a);
                this.displayInfo();
            }
        }
    }
}
