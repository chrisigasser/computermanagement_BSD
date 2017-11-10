﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace ComputermanagementClasses
{
    static class RestCall
    {
        private static string ipOfRestServer = "http://192.168.193.98:8080/RESTOracle/rest/UserService";

        public static string makeRestCall(string URL, string urlParameters)
        {
            URL = ipOfRestServer + URL;
            string dataObject = null;
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri(URL);

            // Add an Accept header for JSON format.
            client.DefaultRequestHeaders.Accept.Add(
            new MediaTypeWithQualityHeaderValue("application/json"));

            // List data response.
            HttpResponseMessage response = client.GetAsync(urlParameters).Result;  // Blocking call!
            if (response.IsSuccessStatusCode)
            {
                // Parse the response body. Blocking!
                dataObject = response.Content.ReadAsStringAsync().Result.ToString();
                
                //foreach (var d in dataObjects)
                //{
                    //MessageBox.Show(dataObject);
                //}
            }
            else
            {
                MessageBox.Show((int)response.StatusCode +" "+ response.ReasonPhrase);
            }
            return dataObject;
        }
    }
}
