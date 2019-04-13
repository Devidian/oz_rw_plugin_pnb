package de.omegazirkel.risingworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;

import de.omegazirkel.risingworld.tools.Colors;
import de.omegazirkel.risingworld.tools.FileChangeListener;
import de.omegazirkel.risingworld.tools.I18n;
import de.omegazirkel.risingworld.tools.PluginChangeWatcher;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerDisconnectEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Inventory;
import net.risingworld.api.objects.Item;
import net.risingworld.api.objects.Player;

public class PNB extends Plugin implements Listener, FileChangeListener {

	static final String pluginVersion = "0.6.0";
	static final String pluginName = "Planks 'n Beams";
	static final String pluginCMD = "pnb";

	static final de.omegazirkel.risingworld.tools.Logger log = new de.omegazirkel.risingworld.tools.Logger("[OZ.PNB]");
	static final Colors c = Colors.getInstance();
	public static I18n t = null;

	// PNB CONSTANTS
	public static final int NUM_OF_QUICKSLOTS = 5;
	public static final int NUM_OF_INVSLOTS = 32;
	public static final short ORE_ID = 309;
	public static final short LUMBER_ID = 265;
	public static final short PLANK_ID = 760;
	public static final short BEAM_ID = 761;
	public static final short WINDOW1_ID = 771; // frame
	public static final short WINDOW2_ID = 772; // frame + vert.
	public static final short WINDOW3_ID = 773; // frame + vert. + high bar
	public static final short WINDOW4_ID = 774; // frame + vert. + middle bar
	public static final short PLANKTRI_ID = 763;
	public static final short LOG_ID = 762;
	public static final short IRONINGOT_ID = 310;
	public static final short COPPERINGOT_ID = 311;
	public static final short DIRT_VAR = 1;
	public static final short STONE_VAR = 3;
	public static final short GRAVEL_VAR = 4;
	public static final short SNOW_VAR = 8;
	public static final short SAND_VAR = 9;
	public static final short DRYEARTH_VAR = 10;
	public static final short SANDSTONE_VAR = 11;
	public static final short COAL_VAR = 15;
	public static final short HELLSTONE_VAR = 16;

	// not a real constant, cached for performance, as it is going to be used a lot!
	protected static final Inventory.SlotType[] slotTypeValues = Inventory.SlotType.values();

	// MATERIAL & RESOURCES
	public static final short[] resourceId = // the type ID of each used resource
			{ ORE_ID, ORE_ID, ORE_ID, LUMBER_ID, COPPERINGOT_ID, IRONINGOT_ID, ORE_ID, ORE_ID, ORE_ID, ORE_ID, ORE_ID };
	public static final short[] resourceVar = // the type variation of each used resource
			{ STONE_VAR, SANDSTONE_VAR, DIRT_VAR, 0, 0, 0, GRAVEL_VAR, SAND_VAR, SNOW_VAR, COAL_VAR, HELLSTONE_VAR };
	// the range of known textures
	public static final short firstVariation = 21;
	public static final short lastVariation = 221;
	// the index (into resourceId and resourceVar) of the needed resource for each
	// texture
	public static final short[] resourcePerVariation = {
			// 0: stone 5: iron ingot 10: hellstone
			// 1: sandstone 6: gravel
			// 2: dirt 7: sand
			// 3: lumber 8: snow
			// 4: copper ingot 9: coal
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 21-32 stone - stone bricks: stone
			1, 1, 1, 1, // 33-36 sandstone: sandstone
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 37-48 cobblestone: stone
			2, 2, 2, 2, // 49-52 loam: dirt
			0, 0, 0, 0, 0, 0, 0, 0, // 53-60 marble: stone
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 61-71 wood block: lumber
			3, 3, 3, 3, 3, 3, 3, 3, 3, // 72-80 wood plank: lumber
			0, 0, 0, 0, // 81-84 stone tiles: stone
			3, 3, 3, 3, 3, 3, 3, 3, 3, // 85-93 addit. wood block: lumber
			3, 3, 3, 3, 3, 3, 3, // 94-100 addit. wood plank: lumber
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 101-112 addit. cobble stone: stone
			0, 0, 0, 0, 0, 0, 0, 0, 0, // 113-121 addit. stone-stone bricks: stone
			0, 0, 0, // 122-124 asphalt: stone
			0, 0, 0, 0, 0, 0, // 125-130 concrete: stone
			0, 0, 0, 0, 0, 0, // 131-136 concrete plates: stone
			0, 0, 0, 0, // 137-140 reinforced concrete: stone
			0, 0, 0, 0, 0, 0, 0, 0, // 141-148 plaster: stone
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 149-158 tiles: stone
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 159-170 marble tiles: stone
			4, 4, 4, 4, // 171-174 copper: copper ingot
			5, 5, 5, 5, 5, 5, 5, 5, 5, 5, // 175-184 metal: iron ingot
			5, 5, 5, 5, 5, 5, 5, 5, 5, // 185-193 metal plates: iron ingot
			5, 5, 5, 5, 5, // 194-198 recycled metal: iron ingot
			0, 0, 0, 0, 0, 0, // 199-204 addit. plaster: stone
			0, 0, 0, 0, 0, 0, 0, 0, // 205-212 ornamental: stone
			2, 2, 0, 6, 2, 7, 8, 9, 10 // 213-221 natural: various
	};

	// RETURN CODES
	public static final int ERR_SUCCESS = 0;
	public static final int ERR_INVALID_PARAM = -1;
	public static final int ERR_NO_RESOURCES = -2;
	public static final int ERR_GENERIC = -3;

	// FIELDS
	//
	protected static PNB plugin;
	protected static String pluginPath;

	// Settings
	static int logLevel = 0;
	static boolean restartOnUpdate = true;
	static boolean sendPluginWelcome = false;

	// PNB Stuff
	public static int costPerItem = 1;
	public static int windowResMult = 5;
	public static boolean freeForAdmin = false;
	public static boolean freeForCreative = false;

	// END Settings

	static boolean flagRestart = false;

	@Override
	public void onEnable() {
		t = t != null ? t : new I18n(this);
		registerEventListener(this);
		this.initSettings();

		pluginPath = this.getPath();
		plugin = this;

		// For plugin updates
		try {
			PluginChangeWatcher WU = new PluginChangeWatcher(this);
			File f = new File(getPath());
			WU.watchDir(f);
			WU.startListening();
		} catch (IOException ex) {
			log.out(ex.getMessage(), 999);
		}

		log.out(pluginName + " Plugin is enabled", 10);
	}

	@Override
	public void onDisable() {
		log.out(pluginName + " Plugin is disabled", 10);
	}

	@EventMethod
	public void onPlayerSpawn(PlayerSpawnEvent event) {
		if (sendPluginWelcome) {
			Player player = event.getPlayer();
			String lang = player.getSystemLanguage();
			player.sendTextMessage(t.get("MSG_PLUGIN_WELCOME", lang));
		}
	}

	@EventMethod
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		Server server = getServer();

		if (flagRestart) {
			int playersLeft = server.getPlayerCount() - 1;
			if (playersLeft == 0) {
				log.out("Last player left the server, shutdown now due to flagRestart is set", 100); // INFO LEVEL
				server.shutdown();
			} else if (playersLeft > 1) {
				this.broadcastMessage("BC_PLAYER_REMAIN", playersLeft);
			}
		}
	}

	@EventMethod
	public void onPlayerCommand(PlayerCommandEvent event) {
		Player player = event.getPlayer();
		String command = event.getCommand();
		String lang = event.getPlayer().getSystemLanguage();
		String[] cmd = command.split(" ");

		if (cmd[0].equals("/" + pluginCMD)) {
			// Invalid number of arguments (0)
			if (cmd.length < 2) {
				mainGui(event.getPlayer());
				return;
			}
			String option = cmd[1];
			switch (option) {

			case "info":
				String infoMessage = t.get("CMD_INFO", lang);
				player.sendTextMessage(c.okay + pluginName + ":> " + infoMessage);
				break;
			case "help":
				String helpMessage = t.get("CMD_HELP", lang)
						.replace("PH_CMD_HELP", c.command + "/" + pluginCMD + " help" + c.text)
						.replace("PH_CMD_INFO", c.command + "/" + pluginCMD + " info" + c.text)
						.replace("PH_CMD_STATUS", c.command + "/" + pluginCMD + " status" + c.text)
						.replace("PH_CMD_GUI", c.command + "/" + pluginCMD + c.text);
				player.sendTextMessage(c.okay + pluginName + ":> " + helpMessage);
				break;
			case "status":
				String statusMessage = t.get("CMD_STATUS", lang).replace("PH_VERSION", c.okay + pluginVersion + c.text)
						.replace("PH_LANGUAGE",
								c.comment + player.getLanguage() + " / " + player.getSystemLanguage() + c.text)
						.replace("PH_USEDLANG", c.info + t.getLanguageUsed(lang) + c.text)
						.replace("PH_LANG_AVAILABLE", c.okay + t.getLanguageAvailable() + c.text);
				player.sendTextMessage(c.okay + pluginName + ":> " + statusMessage);
				break;
			default:
				player.sendTextMessage(c.error + pluginName + ":> " + c.text
						+ t.get("MSG_CMD_ERR_UNKNOWN_OPTION", lang).replace("PH_OPTION", option));
				break;
			}
		}

	}

	/** */
	private void initSettings() {
		Properties settings = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(getPath() + "/settings.properties");
			settings.load(new InputStreamReader(in, "UTF8"));
			in.close();

			// fill global values
			logLevel = Integer.parseInt(settings.getProperty("logLevel", "0"));
			sendPluginWelcome = settings.getProperty("sendPluginWelcome", "false").contentEquals("true");

			// PNB stuff
			costPerItem = Integer.parseInt(settings.getProperty("costPerItem", "1"));
			freeForAdmin = settings.getProperty("freeForAdmin", "false").contentEquals("true");
			freeForCreative = settings.getProperty("freeForCreative", "false").contentEquals("true");

			// restart settings
			restartOnUpdate = settings.getProperty("restartOnUpdate").contentEquals("true");
			log.out(pluginName + " Plugin settings loaded", 10);
		} catch (Exception ex) {
			log.out("Exception on initSettings: " + ex.getMessage(), 100);
		}
	}

	// PNB Stuff

	public void mainGui(Player player) {
		PNBGui gui = new PNBGui(this, player, player.getSystemLanguage());
		gui.show(player);
	}

	/**
	 * Gives the player the required items in exchange for resources. Checks the
	 * player has enough resources in the inventory.
	 * 
	 * @param player    the target player
	 * @param type      the type of the items (PLANK_ID, BEAM_ID or PLANKTRI_ID)
	 * @param variation the item variation (texture: 21 - 212)
	 * @param quantity  the quantity of the items (1 - 64)
	 * @return one of the ERR_ return values
	 */
	public int buy(Player player, int type, int variation, int quantity) {
		if (variation < firstVariation || variation > lastVariation)
			return ERR_INVALID_PARAM;

		// retrieve the needed resource
		int itemId = resourceId[resourcePerVariation[variation - firstVariation]];
		int itemVar = resourceVar[resourcePerVariation[variation - firstVariation]];
		Boolean freeCreative = (Boolean) player.getPermissionValue("creative_freecrafting");
		int cost = player.isAdmin() && PNB.freeForAdmin
				|| player.isCreativeModeEnabled() && freeCreative && PNB.freeForCreative ? 0 : quantity * costPerItem;
		// scan the inventory to collect the total number of resources and the slots
		// where they are
		Item item;
		Inventory inv = player.getInventory();
		int resources = 0; // the number of resources available in the player inventory
		ArrayList<Integer> sourceSlots = new ArrayList<>();
		if (cost > 0)
			for (int invType = 0; invType < slotTypeValues.length; invType++) {
				Inventory.SlotType slotType = slotTypeValues[invType];
				for (int j = 0; j < inv.getSlotCount(slotType); j++) {
					if ((item = inv.getItem(j, slotType)) == null)
						continue;
						// Debug code to find type id and variation
						// log.out(item.getTypeID()+"-"+item.getVariation());
					if (item.getTypeID() == itemId && item.getVariation() == itemVar) {
						resources += item.getStacksize();
						sourceSlots.add((invType << 16) + j);
					}
				}
			}
		// does the player have enough resources to pay for the items?
		if (resources < cost)
			return ERR_NO_RESOURCES;
		// give the items to the player
		item = inv.insertNewItem((short) type, variation, quantity);
		if (item == null)
			return ERR_GENERIC;
		// scan the collected slots to withdraw the corresponding number of resources
		int size;
		for (Integer i : sourceSlots) {
			item = inv.getItem((i & 0xFFFF), slotTypeValues[(i >> 16)]);
			size = item.getStacksize();
			if (size <= cost) // if item does not -- or barely -- fulfil the cost, remove it entirely
			{
				inv.removeItem((i & 0xFFFF), slotTypeValues[(i >> 16)]);
				cost -= size;
			} else // if item fulfils or exceeds the cost, remove only needed resources
			{
				inv.removeItem((i & 0xFFFF), slotTypeValues[(i >> 16)], cost);
				cost = 0;
			}
			if (cost <= 0) // when all resources are payed, stop
				break;
		}
		return ERR_SUCCESS;
	}

	// All stuff for plugin updates

	/**
	 *
	 * @param i18nIndex
	 * @param playerCount
	 */
	private void broadcastMessage(String i18nIndex, int playerCount) {
		getServer().getAllPlayers().forEach((player) -> {
			try {
				String lang = player.getSystemLanguage();
				player.sendTextMessage(c.warning + pluginName + ":> " + c.text
						+ t.get(i18nIndex, lang).replace("PH_PLAYERS", playerCount + ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void onFileChangeEvent(Path file) {
		if (file.toString().endsWith("jar")) {
			if (restartOnUpdate) {
				Server server = getServer();

				if (server.getPlayerCount() > 0) {
					flagRestart = true;
					this.broadcastMessage("BC_UPDATE_FLAG", server.getPlayerCount());
				} else {
					log.out("onFileCreateEvent: <" + file + "> changed, restarting now (no players online)", 100);
				}

			} else {
				log.out("onFileCreateEvent: <" + file + "> changed but restartOnUpdate is false", 1);
			}
		} else {
			log.out("onFileCreateEvent: <" + file + ">", 0);
		}
	}

	@Override
	public void onFileCreateEvent(Path file) {
		if (file.toString().endsWith("settings.properties")) {
			this.initSettings();
		} else {
			log.out(file.toString() + " was changed", 0);
		}
	}
}