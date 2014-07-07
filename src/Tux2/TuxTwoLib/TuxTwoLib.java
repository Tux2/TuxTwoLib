package Tux2.TuxTwoLib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * TuxTwoLib for Bukkit
 *
 * @author Tux2
 */
public class TuxTwoLib extends JavaPlugin {
	
	String ttlbuild = "2";
	public boolean hasupdate = false;
	public String newversion = "";
	public boolean updatefailed = false;
	boolean checkforupdates = true;
	boolean autodownloadupdates = true;
	boolean autodownloadupdateonnewmcversion = true;
	public boolean updatesuccessful = false;
	
	String currentMCversion = "1.7.9";

    String versionName = null;
    private String versionLink = null;
    String mcversion = currentMCversion;

    private static final String TITLE_VALUE = "name"; // Gets remote file's title
    private static final String LINK_VALUE = "downloadUrl"; // Gets remote file's download link
	
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
    	
    	Pattern bukkitversion = Pattern.compile("(\\d\\.\\d\\.?\\d?)-R(\\d\\.\\d)");
    	String ver = getServer().getBukkitVersion();
    	Matcher bukkitmatch = bukkitversion.matcher(ver);
    	//----------------1.5 code--------------------
    	if(bukkitmatch.find()) {
    		mcversion = bukkitmatch.group(1);
    		if(!mcversion.equals(currentMCversion)) {
    			if(autodownloadupdateonnewmcversion) {
					getLogger().warning("Current version incompatible with this version of Craftbukkit! Checking for and downloading a compatible version.");
    				boolean result = updatePlugin(mcversion, false);
    				if(result && !updatefailed) {
    					getLogger().warning("New version downloaded successfully. Make sure to restart your server to restore full functionality!");
    				}else {
        				incompatiblemcversion = true;
    					getLogger().severe("New version download was unsuccessful. Please download the correct version of the library from http://dev.bukkit.org/server-mods/tuxtwolib/");
    				}
    			}else {
    				if(checkforupdates) {
    					String versioncheck = updateAvailable(mcversion, true);
    	    			if(!versioncheck.equals("0")) {
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
	    			String versioncheck = updateAvailable(mcversion, true);
	    			if(!versioncheck.equals("0")) {
	    				//We have an update! Set the newversion string to the name of the new version.
	    				newversion = versioncheck;
	    				//We can update the plugin in the background.
	    				if(autodownloadupdates) {
	    					getLogger().warning("Update available! Downloading in the background.");
    	    				if(!updatePlugin(mcversion, true)) {
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
    	}else {
    		getLogger().warning("Unable to verify minecraft version! MC version reported: " + ver);
    	}
    }
    
    public void onDisable() {
    	
    }
    
    public String updateAvailable(String version, boolean returnversion) {
    	boolean result = read();
    	if(result) {
    		if(returnversion) {
        		return versionName;
    		}else {
    			return "1";
    		}
    	}else {
    		return "0";
    	}
    }
    
    public boolean updatePlugin(String version, boolean threaded) {
    	if(updateAvailable(version,false).equals("0")) {
    		return false;
    	}
		DownloadPluginThread dpt = new DownloadPluginThread(getDataFolder().getParent(), versionLink, new File(getServer().getUpdateFolder() + File.separator + this.getFile()), this);
		if(threaded) {
			Thread downloaderthread = new Thread(dpt);
			downloaderthread.start();
		}else {
			dpt.run();
		}
		return true;
    }

	private boolean read() {
        try {
            URL url = new URL("https://api.curseforge.com/servermods/files?projectIds=48210");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            
            conn.addRequestProperty("User-Agent", "Updater (by Gravity)");

            conn.setDoOutput(true);

            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String response = reader.readLine();

            final JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() == 0) {
                getLogger().warning("The updater could not find any files for the TuxTwoLib project!");
                return false;
            }
            boolean foundupdate = false;
            for(int i = array.size() - 1; i < -1 && !foundupdate; i--) {
                versionName = (String) ((JSONObject) array.get(i)).get(TITLE_VALUE);
                versionLink = (String) ((JSONObject) array.get(i)).get(LINK_VALUE);
                String[] versionsplit = versionName.split("-");
                if(versionsplit.length > 1) {
                	//Let's see if it is for the correct mc version.
                	if(versionsplit[0].equalsIgnoreCase("v" + mcversion)) {
                		//If the current MC version is the same as the version this
                		//plugin was built for, then we need to check build numbers
                		if(mcversion.equalsIgnoreCase(currentMCversion)) {
                			String buildnumber = versionsplit[1].substring(1);
                			try {
                    			int build = Integer.parseInt(buildnumber);
                    			int currentbuild = Integer.parseInt(ttlbuild);
                    			//Since the files go backwards, then if it's bigger
                    			//than the current build it must be the newest
                    			if(currentbuild < build) {
                    				foundupdate = true;
                    			}
                			}catch(NumberFormatException e) {
                				
                			}
                		}
                	}
                }
            }
            return foundupdate;
        } catch (final IOException e) {
            if (e.getMessage().contains("HTTP response code: 403")) {
                getLogger().warning("dev.bukkit.org rejected the API key provided in plugins/Updater/config.yml");
                getLogger().warning("Please double-check your configuration to ensure it is correct.");
            } else {
                getLogger().warning("The updater could not contact dev.bukkit.org for updating.");
                getLogger().warning("If you have not recently modified your configuration and this is the first time you are seeing this message, the site may be experiencing temporary downtime.");
            }
            e.printStackTrace();
            return false;
        }
    }
}

