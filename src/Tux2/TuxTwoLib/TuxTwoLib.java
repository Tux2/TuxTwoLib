package Tux2.TuxTwoLib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * TuxTwoLib for Bukkit
 *
 * @author Tux2
 */
public class TuxTwoLib extends JavaPlugin {
	
	String ttlbuild = "2";
	String bukkitdlurl = "http://dev.bukkit.org/media/files/";
	public boolean hasupdate = false;
	public String newversion = "";
	public boolean updatefailed = false;
	boolean checkforupdates = true;
	boolean autodownloadupdates = true;
	boolean autodownloadupdateonnewmcversion = true;
	public boolean updatesuccessful = false;
	
	boolean incompatiblemcversion = false;
	
	WarningsThread warnings = null;

    public TuxTwoLib() {
        super();
    }

   

    public void onEnable() {
    	
    	FileConfiguration config = getConfig();
    	File configfile = new File(getDataFolder().toString() + "/config.yml");
    	if(!configfile.exists()) {
    		config.set("AutoDownloadUpdates", true);
    		config.set("CheckForUpdates", true);
    		config.set("AutoUpdateOnMinecraftVersionChange", true);
    		saveConfig();
    	}
    	autodownloadupdates = config.getBoolean("AutoDownloadUpdates", true);
    	autodownloadupdateonnewmcversion = config.getBoolean("AutoUpdateOnMinecraftVersionChange", true);
    	checkforupdates = config.getBoolean("CheckForUpdates", true);
    	
    	Pattern bukkitversion = Pattern.compile("(\\d\\.\\d\\.\\d)-R(\\d\\.\\d)");
    	String ver = getServer().getBukkitVersion();
    	Matcher bukkitmatch = bukkitversion.matcher(ver);
    	//----------------1.4.5-R0.3 code--------------------
    	if(bukkitmatch.find()) {
    		String mcversion = bukkitmatch.group(1);
    		String craftbukkitrevision = bukkitmatch.group(2);
    		if(!mcversion.equals("1.4.5")) {
    			if(autodownloadupdateonnewmcversion) {
					getLogger().warning("Current version incompatible with this version of Craftbukkit! Checking for and downloading a compatible version.");
    				boolean result = updatePlugin(mcversion, craftbukkitrevision, false);
    				if(result && !updatefailed) {
    					getLogger().warning("New version downloaded successfully. Make sure to restart your server to restore full functionality!");
    				}else {
        				incompatiblemcversion = true;
    					getLogger().severe("New version download was unsuccessful. Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    				}
    			}else {
    				if(checkforupdates) {
    					String versioncheck = updateAvailable(mcversion, craftbukkitrevision, true);
    	    			if(!versioncheck.equals("0") && !versioncheck.equals("-1")) {
    	    				newversion = versioncheck;
                			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    	    			}else {
    	        			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    	    			}
    				}else {
            			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    				}
    			}
    			incompatiblemcversion = true;
    			//register events for server admins.
    			getServer().getPluginManager().registerEvents(new TuxTwoLibWarningsListener(this), this);
    		}else {
    			//Version string matches, now to get the build number
    			//and make sure we are running the right version.
    			String[] split = craftbukkitrevision.split("\\.");
    			int majorrev = Integer.parseInt(split[0]);
    			int minorrev = Integer.parseInt(split[1]);
    			//See if the version is less than 0.3
    			if(majorrev < 1 && minorrev < 3) {
    				if(autodownloadupdateonnewmcversion) {
    					getLogger().warning("Current version incompatible with this version of Craftbukkit! Checking for and downloading a compatible version.");
        				boolean result = updatePlugin(mcversion, craftbukkitrevision, false);
        				if(result && !updatefailed) {
        					getLogger().warning("New version downloaded successfully. Make sure to restart your server to restore full functionality!");
        				}else {
        					getLogger().severe("New version download was unsuccessful. Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        				}
        			}else {
        				if(checkforupdates) {
        					String versioncheck = updateAvailable(mcversion, craftbukkitrevision, true);
        	    			if(!versioncheck.equals("0") && !versioncheck.equals("-1")) {
        	    				newversion = versioncheck;
                    			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        	    			}else {
        	        			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        	    			}
        				}else {
                			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        				}
        			}
    				incompatiblemcversion = true;
        			//register events for server admins.
        			getServer().getPluginManager().registerEvents(new TuxTwoLibWarningsListener(this), this);
    			}else {
    				//This version of minecraft is compatible. Let's do the optional update check.
    				if(checkforupdates) {
    	    			String versioncheck = updateAvailable(mcversion, craftbukkitrevision, false);
    	    			if(!versioncheck.equals("0") && !versioncheck.equals("-1")) {
    	    				//We have an update! Set the newversion string to the name of the new version.
    	    				newversion = versioncheck;
    	    				//We can update the plugin in the background.
    	    				if(autodownloadupdates) {
    	    					getLogger().warning("Update available! Downloading in the background.");
        	    				if(!updatePlugin(mcversion, craftbukkitrevision, true)) {
        	    					getLogger().info("Update failed! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/ manually.");
        	    				}
    	    				}else {
    	    					getLogger().info("A new version for your version of Craftbukkit is available! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    	    				}
    	    			}
    	    			//register events for server admins.
    	    			getServer().getPluginManager().registerEvents(new TuxTwoLibWarningsListener(this), this);
    	    		}
    			}
    		}
    	}
    	
    	//----------------1.4.5-R0.2 and below code--------------------
    	/*
    	if(bukkitmatch.find()) {
    		String mcversion = bukkitmatch.group(1);
    		String craftbukkitrevision = bukkitmatch.group(2);
			String[] mcver = mcversion.split("\\.");
			int minor = Integer.parseInt(mcver[1]);
			int build = Integer.parseInt(mcver[2]);
    		if(!(mcver[0].equals("1") && minor <=4 && build < 5)) {
    			if(autodownloadupdateonnewmcversion) {
					getLogger().warning("Current version incompatible with this version of Craftbukkit! Checking for and downloading a compatible version.");
    				boolean result = updatePlugin(mcversion, craftbukkitrevision, false);
    				if(result && !updatefailed) {
    					getLogger().warning("New version downloaded successfully. Make sure to restart your server to restore full functionality!");
    				}else {
        				incompatiblemcversion = true;
    					getLogger().severe("New version download was unsuccessful. Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    				}
    			}else {
    				if(checkforupdates) {
    					String versioncheck = updateAvailable(mcversion, craftbukkitrevision, true);
    	    			if(!versioncheck.equals("0") && !versioncheck.equals("-1")) {
    	    				newversion = versioncheck;
                			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    	    			}else {
    	        			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    	    			}
    				}else {
            			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    				}
    			}
    			incompatiblemcversion = true;
    			//register events for server admins.
    			getServer().getPluginManager().registerEvents(new TuxTwoLibWarningsListener(this), this);
    		}else {
    			//Version string matches, now to get the build number
    			//and make sure we are running the right version.
    			double rev = Double.parseDouble(craftbukkitrevision);
    			//See if the version is less than 0.3
    			if(mcversion.equals("1.4.5") && rev > 0.2) {
    				if(autodownloadupdateonnewmcversion) {
    					getLogger().warning("Current version incompatible with this version of Craftbukkit! Checking for and downloading a compatible version.");
        				boolean result = updatePlugin(mcversion, craftbukkitrevision, false);
        				if(result && !updatefailed) {
        					getLogger().warning("New version downloaded successfully. Make sure to restart your server to restore full functionality!");
        				}else {
        					getLogger().severe("New version download was unsuccessful. Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        				}
        			}else {
        				if(checkforupdates) {
        					String versioncheck = updateAvailable(mcversion, craftbukkitrevision, true);
        	    			if(!versioncheck.equals("0") && !versioncheck.equals("-1")) {
        	    				newversion = versioncheck;
                    			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        	    			}else {
        	        			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        	    			}
        				}else {
                			getLogger().severe("Craftbukkit revision is incompatible with this build! Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
        				}
        			}
    				incompatiblemcversion = true;
        			//register events for server admins.
        			getServer().getPluginManager().registerEvents(new TuxTwoLibWarningsListener(this), this);
    			}else {
    				//This version of minecraft is compatible. Let's do the optional update check.
    				if(checkforupdates) {
    	    			String versioncheck = updateAvailable(mcversion, craftbukkitrevision, false);
    	    			if(!versioncheck.equals("0") && !versioncheck.equals("-1")) {
    	    				//We have an update! Set the newversion string to the name of the new version.
    	    				newversion = versioncheck;
    	    				//We can update the plugin in the background.
    	    				if(autodownloadupdates) {
    	    					getLogger().warning("Update available! Downloading in the background.");
        	    				if(!updatePlugin(mcversion, craftbukkitrevision, true)) {
        	    					getLogger().info("Update failed! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/ manually.");
        	    				}
    	    				}else {
    	    					getLogger().info("A new version for your version of Craftbukkit is available! Please download " + newversion + " version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    	    				}
    	    			}
    	    			//register events for server admins.
    	    			getServer().getPluginManager().registerEvents(new TuxTwoLibWarningsListener(this), this);
    	    		}
    			}
    		}
    	}*/
    }
    
    public void onDisable() {
    	
    }
    
    public String updateAvailable(String version, String cbrev, boolean returnversion) {
    	try {
			URL updateurl = new URL("http://update.yu8.me/tuxtwolib.php");
			HttpURLConnection con;
			// Mineshafter creates a socks proxy, so we can safely bypass it
	        // It does not reroute POST requests so we need to go around it
	        if (isMineshafterPresent()) {
	            con = (HttpURLConnection) updateurl.openConnection(Proxy.NO_PROXY);
	        } else {
	            con = (HttpURLConnection) updateurl.openConnection();
	        }
			con.setRequestMethod("POST");
			con.setReadTimeout(1000);
			con.setConnectTimeout(1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestProperty("User-Agent", "Mozilla/4.0");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			String longurl = "version=" + URLEncoder.encode(version, "UTF-8") + "&rev=" + URLEncoder.encode(cbrev, "UTF-8") + "&build=" + URLEncoder.encode(ttlbuild, "UTF-8") + "&checkupdate=true";
			if(returnversion) {
				longurl = longurl + "&getcurrent=true";
			}
			con.setRequestProperty("Content-Length", String.valueOf(longurl.length()));
			con.getOutputStream().write(longurl.getBytes());
			InputStream in = con.getInputStream();
			String url = readString(in);
			if(url.trim().equalsIgnoreCase("0")) {
				return "0";
			}else {
				return url;
			}
		} catch (MalformedURLException e) {
			return "-1";
		} catch (UnsupportedEncodingException e) {
			return "-1";
		} catch (IOException e) {
			return "-1";
		}
    }
    
    public boolean updatePlugin(String version, String cbrev, boolean threaded) {
    	try {
			URL updateurl = new URL("http://update.yu8.me/tuxtwolib.php");
			HttpURLConnection con;
			// Mineshafter creates a socks proxy, so we can safely bypass it
	        // It does not reroute POST requests so we need to go around it
	        if (isMineshafterPresent()) {
	            con = (HttpURLConnection) updateurl.openConnection(Proxy.NO_PROXY);
	        } else {
	            con = (HttpURLConnection) updateurl.openConnection();
	        }
			con.setRequestMethod("POST");
			con.setReadTimeout(1000);
			con.setConnectTimeout(1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestProperty("User-Agent", "Mozilla/4.0");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			String longurl = "version=" + URLEncoder.encode(version, "UTF-8") + "&rev=" + URLEncoder.encode(cbrev, "UTF-8") + "&build=" + URLEncoder.encode(ttlbuild, "UTF-8") + "&checkupdate=false";
			con.setRequestProperty("Content-Length", String.valueOf(longurl.length()));
			con.getOutputStream().write(longurl.getBytes());
			InputStream in = con.getInputStream();
			String url = readString(in);
			if(url.trim().equalsIgnoreCase("0")) {
				return false;
			}else {
				DownloadPluginThread dpt = new DownloadPluginThread(getDataFolder().getParent(), url, new File(getDataFolder().getParent() + File.separator + "TuxTwoLib.jar"), this);
				if(threaded) {
					Thread downloaderthread = new Thread(dpt);
					downloaderthread.start();
				}else {
					dpt.run();
				}
				return true;
			}
		} catch (MalformedURLException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
    }

	public static String readString(InputStream in) throws IOException {
		StringBuilder builder = new StringBuilder();
		for(int c = in.read(); ((char)c) != '\r' && c != -1 ; c = in.read()) {
			builder.append(((char)c));
		}
		return builder.toString();
	}



	public static boolean isMineshafterPresent() {
	    try {
	        Class.forName("mineshafter.MineServer");
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
}

