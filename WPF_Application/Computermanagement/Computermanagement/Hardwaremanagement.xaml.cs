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
    /// Interaction logic for Hardwaremanagement.xaml
    /// </summary>
    public partial class Hardwaremanagement : Window
    {
        List<Hardware> listOfHardware;
         Hardware selectedHardware = null;
        public Hardwaremanagement()
        {
            InitializeComponent();
            listOfHardware = new List<Hardware>();
            
            Init();
        }

        private void Init()
        {
            loadHardwareRestCall();
            refreshGUI();
            setStatusOfInputs(false);
        }

        private void loadHardwareRestCall()
        {
            this.listOfHardware = Hardwaremanager.getallHardware();
            this.lb_ListOfHardware.SelectedIndex = -1;
        }

        private void setStatusOfInputs(bool status)
        {
            this.label_description.IsEnabled = status;
            this.label_name.IsEnabled = status;
            this.label_logopath.IsEnabled = status;

            this.textbox_description.IsEnabled = status;
            this.textbox_logopath.IsEnabled = status;
            this.textbox_name.IsEnabled = status;
        }

        private void refreshGUI()
        {
            this.lb_ListOfHardware.ItemsSource = this.listOfHardware;

            if(this.selectedHardware != null)
            {
                setStatusOfInputs(true);
                this.textbox_description.Text = this.selectedHardware.desc;
                this.textbox_name.Text = this.selectedHardware.name;
                this.textbox_logopath.Text = this.selectedHardware.logo;
            }
            else
            {
                setStatusOfInputs(false);
                this.textbox_description.Text = "";
                this.textbox_name.Text = "";
                this.textbox_logopath.Text = "";
            }
        }

        private void hardwareSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.lb_ListOfHardware.SelectedIndex != -1)
                this.selectedHardware = this.listOfHardware[this.lb_ListOfHardware.SelectedIndex];
            else
                this.selectedHardware = null;

            refreshGUI();
        }

        private void button_createNew_Click(object sender, RoutedEventArgs e)
        {
            Hardware newHardware = new Hardware(this.textbox_name.Text, this.textbox_logopath.Text, "", this.textbox_description.Text);
            Hardwaremanager.addHardware(newHardware);
            loadHardwareRestCall();
        }

        private void button_delete_Click(object sender, RoutedEventArgs e)
        {
            if (this.selectedHardware != null)
            {
                Hardwaremanager.removeHardwareByObject(this.selectedHardware);
                loadHardwareRestCall();
            }
        }

        private void button_save_Click(object sender, RoutedEventArgs e)
        {
            if (this.selectedHardware != null)
            {
                this.selectedHardware.desc = this.textbox_description.Text;
                this.selectedHardware.logo = this.textbox_logopath.Text;
                this.selectedHardware.name = this.textbox_name.Text;
                Hardwaremanager.updateHardware(this.selectedHardware);
                loadHardwareRestCall();
            }
        }
    }
}
