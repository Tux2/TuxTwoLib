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
        File tempfile = new File(this.downloadlocation + File.separator + "TuxTwoLib.jar.part");
        try {
            URL website;
            website = new URL(this.dldetails);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(tempfile);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            fos.close();
            this.destination.delete();
            if (tempfile.renameTo(this.destination)) {
                this.plugin.hasupdate = true;
                this.plugin.getLogger().warning("Updated to version " + this.plugin.versionName + ". Please restart your server.");
                return;
            } else {
                this.plugin.updatefailed = true;
                this.plugin.getLogger().warning("Unable to update to new version. Please update manually!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.plugin.newversion = this.plugin.versionName;
        this.plugin.hasupdate = false;
    }

}
