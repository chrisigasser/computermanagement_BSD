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
    /// Interaction logic for Anwendungen.xaml
    /// </summary>
    public partial class Anwendungen : Window
    {
        List<Anwendung> listOfAnwendung;
        Anwendung selectedAnwendung = null;
        public Anwendungen()
        {
            InitializeComponent();
            Init();
        }
        private void Init()
        {
            loadAnwendungenRestCall();
            refreshGUI();
            setStatusOfInputs(false);
        }

        private void setStatusOfInputs(bool v)
        {
            this.textbox_details.IsEnabled = v;
            this.textbox_name.IsEnabled = v;
        }

        private void refreshGUI()
        {

            this.listbox_anwendungen.ItemsSource = this.listOfAnwendung;

            if (this.selectedAnwendung != null)
            {
                setStatusOfInputs(true);
                this.textbox_name.Text = this.selectedAnwendung.name;
                this.textbox_details.Text = this.selectedAnwendung.desc;
            }
            else
            {
                setStatusOfInputs(false);
                this.textbox_name.Text = "";
                this.textbox_details.Text = "";
            }
        }

        private void loadAnwendungenRestCall()
        {
            this.listOfAnwendung = Anwendungsmanager.getallAnwendung();
            this.listbox_anwendungen.SelectedIndex = -1;
        }

        private void anwendungSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (this.listbox_anwendungen.SelectedIndex != -1)
                this.selectedAnwendung = this.listOfAnwendung[this.listbox_anwendungen.SelectedIndex];
            else
                this.selectedAnwendung = null;

            refreshGUI();
        }

        private void btn_add_Click(object sender, RoutedEventArgs e)
        {
            Anwendung newAnwendung = new Anwendung(this.textbox_name.Text, this.textbox_details.Text);
            Anwendungsmanager.addAnwendung(newAnwendung);
            loadAnwendungenRestCall();
        }

        private void btn_save_Click(object sender, RoutedEventArgs e)
        {
            if (this.selectedAnwendung != null)
            {
                this.selectedAnwendung.desc = this.textbox_details.Text;
                this.selectedAnwendung.name = this.textbox_name.Text;
                Anwendungsmanager.updateAnwendung(this.selectedAnwendung);
                loadAnwendungenRestCall();
            }
        }

        private void btn_delete_Click(object sender, RoutedEventArgs e)
        {
            if (this.selectedAnwendung != null)
            {
                Anwendungsmanager.removeAnwendungByObject(this.selectedAnwendung);
                loadAnwendungenRestCall();
            }
        }
    }
}
