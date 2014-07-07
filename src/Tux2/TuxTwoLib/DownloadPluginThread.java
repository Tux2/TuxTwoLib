package Tux2.TuxTwoLib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadPluginThread implements Runnable {
	
	String downloadlocation = "";
	File destination;
	TuxTwoLib plugin;
	String dldetails;
	
	public DownloadPluginThread(String downloadlocation, String dldetails, File destination, TuxTwoLib plugin) {
		this.downloadlocation = downloadlocation;
		this.dldetails = dldetails;
		this.destination = destination;
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		File tempfile = new File(downloadlocation + File.separator + "TuxTwoLib.jar.part");
		try {
			URL website;
			website = new URL(dldetails);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(tempfile);
		    fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		    fos.close();
		    destination.delete();
		    if(tempfile.renameTo(destination)) {
		    	plugin.hasupdate = true;
		    	plugin.getLogger().warning("Updated to version " + plugin.versionName + ". Please restart your server.");
		    	return;
		    }else {
		    	plugin.updatefailed = true;
		    	plugin.getLogger().warning("Unable to update to new version. Please update manually!");
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
    	plugin.newversion = plugin.versionName;
	    plugin.hasupdate = false;
	}

}
