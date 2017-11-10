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
    /// Interaction logic for Overview.xaml
    /// </summary>
    public partial class Overview : Window
    {
        Housing selectedHousing = null;
        List<Housing> listOfHousings;
        List<Room> listOfRoomsForHousing;
        public Overview()
        {
            InitializeComponent();
            listOfHousings = new List<Housing>();
            listOfRoomsForHousing = new List<Room>();
            Init();
        }

        private void Init()
        {
            loadHousingsRestCall();
            refreshGUI();
            //setStatusOfInputs(false);
        }

        private void setStatusOfInputs(bool v)
        {
            throw new NotImplementedException();
        }

        private void refreshGUI()
        {
            this.comboBox_housing.Items.Clear();
            Console.WriteLine();
            foreach (Housing h in listOfHousings)
            {
                this.comboBox_housing.Items.Add(h.name);
            }
            Console.WriteLine();
        }

        private void housingSelected(object sender, SelectionChangedEventArgs e)
        {
            //Console.WriteLine(comboBox_housing.SelectedIndex);
            if (this.comboBox_housing.SelectedIndex != -1)
            {
                this.selectedHousing = this.listOfHousings[this.comboBox_housing.SelectedIndex];
                this.loadRoomsForHousingRestCall();
                this.addRoomsToGUI();
                //refreshGUI();
            }
            else
            {
                this.selectedHousing = null;
                this.comboBox_Room.Items.Clear();
            }
        }

        private void addRoomsToGUI()
        {
            this.comboBox_Room.Items.Clear();
            if (selectedHousing != null && selectedHousing.rooms != null)
            {
                foreach (Room r in selectedHousing.rooms)
                {
                    this.comboBox_Room.Items.Add(r.name);
                }
            }
        }

        private void loadHousingsRestCall()
        {
            this.listOfHousings = new List<Housing>();
            this.listOfHousings = OverviewManager.getAllHousings();
        }

        private void loadRoomsForHousingRestCall()
        {
            this.selectedHousing = OverviewManager.getAllRoomsForHousing(this.selectedHousing);
        }

        
    }
}
