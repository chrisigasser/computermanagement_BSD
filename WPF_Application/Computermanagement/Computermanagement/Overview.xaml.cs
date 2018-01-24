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
        Room selectedRoom = null;
        List<Housing> listOfHousings;
        List<Room> listOfRoomsForHousing;
        List<HardwareForRoom> listOfHardwareForRoom;
        HardwareForRoom selectedHardwareForroom;
        public Overview()
        {
            InitializeComponent();
            listOfHousings = new List<Housing>();
            listOfRoomsForHousing = new List<Room>();
            listOfHardwareForRoom = new List<HardwareForRoom>();
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
            selectedRoom = null;

            if (this.comboBox_housing.SelectedIndex != -1)
            {
                this.selectedHousing = this.listOfHousings[this.comboBox_housing.SelectedIndex];
                this.loadRoomsForHousingRestCall();

                if(this.selectedHousing!=null)
                    this.addRoomsToGUI();
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
            this.listOfRoomsForHousing = this.selectedHousing.rooms;
        }

        private void roomSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.comboBox_Room.SelectedIndex != -1)
            {
                this.selectedRoom = this.listOfRoomsForHousing[this.comboBox_Room.SelectedIndex];
                this.loadHardwareForRoomRestCall();

                if(this.selectedRoom!=null)
                    this.addHardwareToGUI();
                
            }
            else
            {
                this.selectedRoom = null;
                this.listBox_listofhardware.Items.Clear();
            }
        }

        private void addHardwareToGUI()
        {
            this.listBox_listofhardware.Items.Clear();
            if (selectedRoom != null && selectedHousing != null && this.listOfHardwareForRoom!=null)
            {
                foreach (HardwareForRoom h in listOfHardwareForRoom)
                {
                    this.listBox_listofhardware.Items.Add(h.name);
                }
            }
        }

        private void loadHardwareForRoomRestCall()
        {
            this.listOfHardwareForRoom = OverviewManager.getHardwareForRoom(this.selectedRoom);
            //this.listBox_listofhardware.Items.Refresh();
        }

        private void hardwareSelected(object sender, SelectionChangedEventArgs e)
        {
            if (listBox_listofhardware.SelectedIndex != -1)
            {
                HardwareForRoom temp = listOfHardwareForRoom[listBox_listofhardware.SelectedIndex];
                this.label_name.Content = temp.name;
                this.label_type.Content = temp.hname;
                this.label_desc.Content = temp.hdesc;
                
                this.selectedHardwareForroom = temp;
            }
            else
            {
                this.clearHardwareDetails();
            }
        }

        private void clearHardwareDetails()
        {
            this.label_name.Content = "";
            this.label_type.Content = "";
            this.label_desc.Content = "";
        }

        private void button_addhardware_Click(object sender, RoutedEventArgs e)
        {
            NewHardwareForRoom newHardware = new NewHardwareForRoom(this);
            newHardware.Show();
        }
        public void addNewHardwareForRoom(string hid, string name, string rhdesc)
        {
            Room selected = this.selectedRoom;
            OverviewManager.addHardwareForRoom(this.selectedRoom.id,hid,name,rhdesc);
            //loadHardwareForRoomRestCall();
            addRoomsToGUI();
            foreach (String s in comboBox_Room.Items)
            {
                if (s == selected.name)
                {
                    this.comboBox_Room.SelectedItem = s;
                }
            }
        }

        private void button_deletehardware_Click(object sender, RoutedEventArgs e)
        {
            if (this.selectedHardwareForroom != null)
            {
                Room selected = this.selectedRoom;
                OverviewManager.removeHardwareByObject(this.selectedHardwareForroom.id+"");
                //loadHardwareForRoomRestCall();
                addRoomsToGUI();
                foreach (String s in comboBox_Room.Items)
                {
                    if (s == selected.name)
                    {
                        this.comboBox_Room.SelectedItem = s;
                    }
                }
            }
        }

        private void showDetails(object sender, RoutedEventArgs e)
        {
            if (this.selectedHardwareForroom != null)
            {
                ComputermanagementClasses.HardwareForRoomDetails h = OverviewManager.getDetailsForHardwareInRoom(selectedHardwareForroom);
                Computermanagement.HardwareForRoomDetails hwfrdetails = new Computermanagement.HardwareForRoomDetails(h);
                hwfrdetails.Show();
            }
        }
    }
}
