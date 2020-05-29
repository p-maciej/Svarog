using System;
using System.Collections.Generic;
using System.Diagnostics;
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

namespace SvarogLauncher {
    public partial class MainWindow : Window
    {
        public MainWindow() {
            InitializeComponent();

            JVMparameters.Text = "-XX:+UseG1GC -Xmx2048m -Xms512m";
        }

        private void RunBtn_Click(object sender, RoutedEventArgs e) {
            string jvmParameters = JVMparameters.Text + " -jar svarog.jar";
            var startInfo = new ProcessStartInfo("java.exe", jvmParameters) {
                RedirectStandardError = true,
                RedirectStandardOutput = true,
                CreateNoWindow = true,
                UseShellExecute = false
            };

            var process = Process.Start(startInfo);

            process.WaitForExit();
        }
    }
}
