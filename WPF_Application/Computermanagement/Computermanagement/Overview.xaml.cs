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
        public Overview()
        {
            InitializeComponent();
            listOfHousings = new List<Housing>();
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
            this.comboBox_hardware.Items.Clear();
            this.comboBox_Room.Items.Clear();
            foreach (Housing h in listOfHousings)
            {
                this.comboBox_hardware.Items.Add(h.name);
            }
            if (selectedHousing!=null&&selectedHousing.rooms!=null)
            {
                foreach (Room r in selectedHousing.rooms)
                {
                    this.comboBox_Room.Items.Add(r.name);
                }
            }

        }

        private void housingSelected(object sender, SelectionChangedEventArgs e)
        {
            if (this.comboBox_hardware.SelectedIndex != -1)
            {
                this.selectedHousing = this.listOfHousings[this.comboBox_hardware.SelectedIndex];
                this.loadRoomsForHousingRestCall();
            }
            else
            {
                this.selectedHousing = null;
            }

            refreshGUI();
        }
        private void loadHousingsRestCall()
        {
            this.listOfHousings.Clear();
            this.listOfHousings = OverviewManager.getAllHousings();
        }

        private void loadRoomsForHousingRestCall()
        {
            OverviewManager.getAllRoomsForHousing(this.selectedHousing);
        }
    }
}
