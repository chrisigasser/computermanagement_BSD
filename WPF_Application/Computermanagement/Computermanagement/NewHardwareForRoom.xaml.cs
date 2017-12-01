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
    /// Interaktionslogik für NewHardwareForRoom.xaml
    /// </summary>
    public partial class NewHardwareForRoom : Window
    {
        List<Hardware> listOfHardware = null;
        Overview calledBy = null;
        public NewHardwareForRoom(Overview caller)
        {
            InitializeComponent();
            Init();
            this.calledBy = caller;
        }

        private void Init()
        {
            this.listOfHardware = Hardwaremanager.getallHardware();
            this.combobox_hardware.Items.Clear();
            foreach (Hardware h in listOfHardware)
            {
                this.combobox_hardware.Items.Add(h.name);
            }
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (this.combobox_hardware.SelectedIndex != -1)
            {
                this.calledBy.addNewHardwareForRoom("" + this.listOfHardware[combobox_hardware.SelectedIndex].id, this.textbox_name.Text, this.textbox_desc.Text);
                this.Close();
            }
        }
    }
}
