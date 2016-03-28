package application;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class InternetConnectivity_java {
	public String getInternetStatus(){
		try{
			final String authUser = "201585103";
			final String authPassword = "msit123";
			Authenticator.setDefault(
			   new Authenticator() {
			      public PasswordAuthentication getPasswordAuthentication() {
			         return new PasswordAuthentication(
			               authUser, authPassword.toCharArray());
			      }
			   }
			);

			System.setProperty("http.proxyUser", authUser);
			System.setProperty("http.proxyPassword", authPassword);
			System.setProperty("http.proxyHost", "10.10.10.3");
            System.setProperty("http.proxyPort", "3128");
			URL url = new URL("http://www.google.co.in");
			System.out.println(url.getHost());
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.connect();
			if(con.getResponseCode() == 200){
				System.out.println("out online");
				return "Online";
			}


		}catch(Exception e){}
		return "Offline";
	}
}
