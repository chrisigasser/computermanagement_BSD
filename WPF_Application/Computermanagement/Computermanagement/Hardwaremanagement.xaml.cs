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
        public Hardwaremanagement()
        {
            InitializeComponent();
            listOfHardware = new List<Hardware>();
            Init();
        }

        private void Init()
        {
            Hardwaremanager.addHardware(new Hardware(1, "Test01", "Test01.jpg", "Test01 pic"));
            Hardwaremanager.addHardware(new Hardware(2, "Test02", "Test02.jpg", "Test02 pic"));
            Hardwaremanager.addHardware(new Hardware(3, "Test03", "Test03.jpg", "Test03 pic"));
            refreshGUI();
        }

        private void refreshGUI()
        {
            this.listOfHardware = Hardwaremanager.getallHardware();
            this.lb_ListOfHardware.ItemsSource = this.listOfHardware;
        }

        private void hardwareSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            
        }
    }
}
