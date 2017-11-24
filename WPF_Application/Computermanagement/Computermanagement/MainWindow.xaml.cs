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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Computermanagement
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void btn_Hardwaremanagement_Click(object sender, RoutedEventArgs e)
        {
            Hardwaremanagement myHM = new Hardwaremanagement();
            myHM.Show();
        }

        private void btn_Overview_Click(object sender, RoutedEventArgs e)
        {
            Overview myOV = new Overview();
            myOV.Show();
        }

        private void btn_Anwendungen_Click(object sender, RoutedEventArgs e)
        {
            Anwendungen myAW = new Anwendungen();
            myAW.Show();
        }
    }
}
