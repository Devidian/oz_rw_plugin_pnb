/****************************
	P l a n k s A n d B e a m  s  -  A Rising World Java plug-in distributing non-standard planks and beams.

	Gui.java - The plug-in GUI.

	Created by : Maurizio M. Gavioli 2016-10-22

	(C) Maurizio M. Gavioli (a.k.a. Miwarre), 2016
	Licensed under the Creative Commons by-sa 3.0 license (see http://creativecommons.org/licenses/by-sa/3.0/ for details)

*****************************/

package de.omegazirkel.risingworld;

import com.vistamaresoft.rwgui.GuiCheckBox;
import com.vistamaresoft.rwgui.GuiDialogueBox;
import com.vistamaresoft.rwgui.GuiLayout;
import com.vistamaresoft.rwgui.GuiMessageBox;
import com.vistamaresoft.rwgui.GuiTableLayout;
import com.vistamaresoft.rwgui.RWGui;
import com.vistamaresoft.rwgui.RWGui.RWGuiCallback;

import net.risingworld.api.Plugin;
import net.risingworld.api.gui.GuiImage;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.ImageInformation;

public class PNBGui extends GuiDialogueBox {
	// CONSTANTS
	//
	private static final int NUM_OF_IMG_ROWS = 6;
	private static final int NUM_OF_IMG_COLS = 9;
	private static final int IMAGE_HEIGHT = 64;
	private static final int IMAGE_WIDTH = 64;
	private static final int IMAGE_BORDERCOL = RWGui.ACTIVE_COLOUR;
	// Constants
	private static final int NUM_OF_IMAGES = NUM_OF_IMG_ROWS * NUM_OF_IMG_COLS;
	private static final int NUM_OF_TEXTURES = 201;

	private static final int PGUPBUTT_ID = (NUM_OF_IMG_COLS * NUM_OF_IMG_ROWS) + 1;
	private static final int PGDNBUTT_ID = PGUPBUTT_ID + 1;
	private static final int PLANKBUTT_ID = PGDNBUTT_ID + 1;
	private static final int BEAMBUTT_ID = PLANKBUTT_ID + 1;
	private static final int LOGBUTT_ID = BEAMBUTT_ID + 1;
	private static final int PLANK3BUTT_ID = LOGBUTT_ID + 1;
	private static final int WINDOW1BUTT_ID = PLANK3BUTT_ID + 1;
	private static final int WINDOW2BUTT_ID = WINDOW1BUTT_ID + 1;
	private static final int WINDOW3BUTT_ID = WINDOW2BUTT_ID + 1;
	private static final int WINDOW4BUTT_ID = WINDOW3BUTT_ID + 1;
	private static final int MINBUTT_ID = WINDOW4BUTT_ID + 1;
	private static final int MINUSBUTT_ID = MINBUTT_ID + 1;
	private static final int PLUSBUTT_ID = MINUSBUTT_ID + 1;
	private static final int MAXBUTT_ID = PLUSBUTT_ID + 1;
	private static final int DOBUTT_ID = MAXBUTT_ID + 1;

	// FIELDS
	//
	private int textureFirst; // the index of the first texture shown
	private int textureFirstOld;
	private int imageSel; // the index of the selected image
	private int imageSelOld;
	private final boolean free;
	private final GuiImage[] images; // the texture images
	private final GuiImage pgUpButt;
	private final GuiImage pgDnButt;
	private int type;
	private final GuiCheckBox plankCheck;
	private final GuiCheckBox beamCheck;
	private final GuiCheckBox logCheck;
	private final GuiCheckBox plank3Check;
	private final GuiCheckBox window1Check;
	private final GuiCheckBox window2Check;
	private final GuiCheckBox window3Check;
	private final GuiCheckBox window4Check;
	private int quant;
	private final GuiLabel minButt;
	private final GuiImage minusButt;
	private final GuiLabel quantText;
	private final GuiImage plusButt;
	private final GuiLabel maxButt;
	private final GuiLabel resourcesText;
	// to convert an image index into a variation number
	private final short[] image2variation = { 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 113, 114, 115, 116, 117,
			118, 119, 120, 121, // stone - stone bricks: stone
			33, 34, 35, 36, // sandstone: sandstone
			37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, // cobblestone:
																														// stone
			49, 50, 51, 52, // loam: dirt
			53, 54, 55, 56, 57, 58, 59, 60, // marble: stone
			61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 85, 86, 87, 88, 89, 90, 91, 92, 93, // wood block: log
			72, 73, 74, 75, 76, 77, 78, 79, 80, 94, 95, 96, 97, 98, 99, 100, // wood plank: log
			81, 82, 83, 84, // stone tiles: stone
			122, 123, 124, // asphalt: stone
			125, 126, 127, 128, 129, 130, // concrete: stone
			131, 132, 133, 134, 135, 136, // concrete plates: stone
			137, 138, 139, 140, // reinforced concrete: stone
			141, 142, 143, 144, 145, 146, 147, 148, 199, 200, 201, 202, 203, 204, // plaster: stone
			149, 150, 151, 152, 153, 154, 155, 156, 157, 158, // tiles: stone
			159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, // marble tiles: stone
			171, 172, 173, 174, // copper: copper ingot
			175, 176, 177, 178, 179, 180, 181, 182, 183, 184, // metal: iron ingot
			185, 186, 187, 188, 189, 190, 191, 192, 193, // metal plates: iron ingot
			194, 195, 196, 197, 198, // recycled metal: iron ingot
			205, 206, 207, 208, 209, 210, 211, 212, // ornamental: stone
			213, 214, 215, 216, 217, 218, 219, 220, 221 // natural: various
	};
	// to convert an image index into a texture name
	private final short[] image2texture = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 92, 93, 94, 95, 96, 97, 98, 99, 100, // stone
																														// -
																														// stone
																														// bricks
			12, 13, 14, 15, // sandstone
			16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, // cobblestone
			28, 29, 30, 31, // loam
			32, 33, 34, 35, 36, 37, 38, 39, // marble
			40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 64, 65, 66, 67, 68, 69, 70, 71, 72, // wood block
			51, 52, 53, 54, 55, 56, 57, 58, 59, 73, 74, 75, 76, 77, 78, 79, // wood plank
			60, 61, 62, 63, // stone tiles
			101, 102, 103, // asphalt
			104, 105, 106, 107, 108, 109, // concrete
			110, 111, 112, 113, 114, 115, // concrete plates
			116, 117, 118, 119, // reinforced concrete
			120, 121, 122, 123, 124, 125, 126, 127, 178, 179, 180, 181, 182, 183, // plaster
			128, 129, 130, 131, 132, 133, 134, 135, 136, 137, // tiles
			138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, // marble tiles
			150, 151, 152, 153, // copper
			154, 155, 156, 157, 158, 159, 160, 161, 162, 163, // metal
			164, 165, 166, 167, 168, 169, 170, 171, 172, // metal plates
			173, 174, 185, 176, 177, // recycled metal
			184, 185, 186, 187, 188, 189, 190, 191, // ornamental
			192, 193, 194, 195, 196, 197, 198, 199, 200 // natural
	};

	private final String[] resIndex2tIndex = { "PNB_017", // 0 stone
			"PNB_018", // 1 sandstone
			"PNB_019", // 2 dirt
			"PNB_020", // 3 lumber
			"PNB_021", // 4 copper
			"PNB_022", // 5 iron
			"PNB_023", // 6 gravel
			"PNB_024", // 7 sand
			"PNB_030", // 8 snow
			"PNB_031", // 9 coal
			"PNB_032" // 10 hellstone
	};

	public PNBGui(Plugin plugin, Player player, String lang) {
		super(plugin, PNB.t.get("PNB_000"), RWGui.LAYOUT_VERT, null);
		setCallback(new DlgHandler());
		Boolean freeCreative = (Boolean) player.getPermissionValue("creative_freecrafting");
		free = player.isAdmin() && PNB.freeForAdmin
				|| player.isCreativeModeEnabled() && freeCreative && PNB.freeForCreative;
		// The TEXTURES
		addChild(new GuiLabel(PNB.t.get("PNB_001", lang), 0, 0, false));
		textureFirst = 0;
		textureFirstOld = -1; // impossible index to force initial reloading of images
		imageSel = 0;
		imageSelOld = 0;
		images = new GuiImage[NUM_OF_IMAGES];
		GuiLayout layout = addNewLayoutChild(RWGui.LAYOUT_HORIZ, RWGui.LAYOUT_H_LEFT | RWGui.LAYOUT_V_TOP);
		GuiLayout layout2 = layout.addNewTableLayoutChild(NUM_OF_IMG_COLS, NUM_OF_IMG_ROWS, 0);
		for (int i = 0; i < NUM_OF_IMAGES; i++) {
			images[i] = new GuiImage(0, 0, false, IMAGE_WIDTH, IMAGE_HEIGHT, false);
			layout2.addChild(images[i], i + 1);
			images[i].setBorderColor(IMAGE_BORDERCOL);
		}
		// The PGUP and PGDOWN buttons
		layout2 = layout.addNewLayoutChild(RWGui.LAYOUT_VERT, RWGui.LAYOUT_H_LEFT | RWGui.LAYOUT_V_SPREAD);
		pgUpButt = new GuiImage(0, 0, false, RWGui.BUTTON_SIZE, RWGui.BUTTON_SIZE, false);
		layout2.addChild(pgUpButt, PGUPBUTT_ID);
		RWGui.setImage(pgUpButt, RWGui.ICN_ARROW_UP);
		pgDnButt = new GuiImage(0, 0, false, RWGui.BUTTON_SIZE, RWGui.BUTTON_SIZE, false);
		RWGui.setImage(pgDnButt, RWGui.ICN_ARROW_DOWN);
		layout2.addChild(pgDnButt, PGDNBUTT_ID);

		// The TABLE of settings
		layout = addNewTableLayoutChild(2, 3, 0);

		// The TYPE
		type = PNB.PLANK_ID;
		layout.addChild(new GuiLabel(PNB.t.get("PNB_002", lang), 0, 0, false));
		// the sub-table of types
		layout2 = layout.addNewTableLayoutChild(4, 2, 0);
		plankCheck = new GuiCheckBox(PNB.t.get("PNB_003", lang), GuiCheckBox.CHECKED, true, PLANKBUTT_ID, null);
		layout2.addChild(plankCheck);
		beamCheck = new GuiCheckBox(PNB.t.get("PNB_004", lang), GuiCheckBox.UNCHECKED, true, BEAMBUTT_ID, null);
		layout2.addChild(beamCheck);
		logCheck = new GuiCheckBox(PNB.t.get("PNB_010", lang), GuiCheckBox.UNCHECKED, true, LOGBUTT_ID, null);
		layout2.addChild(logCheck);
		plank3Check = new GuiCheckBox(PNB.t.get("PNB_005", lang), GuiCheckBox.UNCHECKED, true, PLANK3BUTT_ID, null);
		layout2.addChild(plank3Check);
		window1Check = new GuiCheckBox(PNB.t.get("PNB_006", lang), GuiCheckBox.UNCHECKED, true, WINDOW1BUTT_ID, null);
		layout2.addChild(window1Check);
		window2Check = new GuiCheckBox(PNB.t.get("PNB_007", lang), GuiCheckBox.UNCHECKED, true, WINDOW2BUTT_ID, null);
		layout2.addChild(window2Check);
		window3Check = new GuiCheckBox(PNB.t.get("PNB_008", lang), GuiCheckBox.UNCHECKED, true, WINDOW3BUTT_ID, null);
		layout2.addChild(window3Check);
		window4Check = new GuiCheckBox(PNB.t.get("PNB_009", lang), GuiCheckBox.UNCHECKED, true, WINDOW4BUTT_ID, null);
		layout2.addChild(window4Check);

		// The QUANTITY
		quant = 1;
		layout.addChild(new GuiLabel(PNB.t.get("PNB_011", lang), 0, 0, false));
		layout2 = layout.addNewLayoutChild(RWGui.LAYOUT_HORIZ, RWGui.LAYOUT_H_LEFT | RWGui.LAYOUT_V_MIDDLE);
		minButt = new GuiLabel(PNB.t.get("PNB_012", lang), 0, 0, false);
		minButt.setColor(RWGui.ACTIVE_COLOUR);
		layout2.addChild(minButt, MINBUTT_ID);
		minusButt = new GuiImage(0, 0, false, RWGui.BUTTON_SIZE, RWGui.BUTTON_SIZE, false);
		RWGui.setImage(minusButt, RWGui.ICN_MINUS);
		layout2.addChild(minusButt, MINUSBUTT_ID);
		quantText = new GuiLabel("1", 0, 0, false);
		layout2.addChild(quantText);
		plusButt = new GuiImage(0, 0, false, RWGui.BUTTON_SIZE, RWGui.BUTTON_SIZE, false);
		RWGui.setImage(plusButt, RWGui.ICN_PLUS);
		layout2.addChild(plusButt, PLUSBUTT_ID);
		maxButt = new GuiLabel(PNB.t.get("PNB_013", lang), 0, 0, false);
		maxButt.setColor(RWGui.ACTIVE_COLOUR);
		layout2.addChild(maxButt, MAXBUTT_ID);

		// The RESOURCES
		layout.addChild(new GuiLabel(PNB.t.get("PNB_014", lang), 0, 0, false));
		resourcesText = new GuiLabel(0, 0, false);
		layout.addChild(resourcesText);

		((GuiTableLayout) layout).setColFlag(0, RWGui.LAYOUT_H_RIGHT);

		// The BUY button
		layout = addNewLayoutChild(RWGui.LAYOUT_HORIZ, RWGui.LAYOUT_H_CENTRE | RWGui.LAYOUT_V_TOP);
		GuiLabel lbl = new GuiLabel(PNB.t.get("PNB_016", lang), 0, 0, false);
		lbl.setColor(RWGui.ACTIVE_COLOUR);
		layout.addChild(lbl, DOBUTT_ID);
		updateImages(lang);
	}

	// ********************
	// HANDLERS
	// ********************

	private class DlgHandler implements RWGuiCallback {
		@Override
		public void onCall(Player player, int id, Object data) {
			String lang = player.getSystemLanguage();
			if (id >= 1 && id <= NUM_OF_IMAGES) {
				imageSel = id - 1;
				updateSelected(lang);
				return;
			}
			switch (id) {
			case RWGui.ABORT_ID:
				free();
				return;
			case PGUPBUTT_ID:
				if (textureFirst <= 0)
					return;
				textureFirst -= NUM_OF_IMAGES;
				if (textureFirst <= 0)
					pgUpButt.setVisible(false);
				pgDnButt.setVisible(true);
				updateImages(lang);
				break;
			case PGDNBUTT_ID:
				if (textureFirst + NUM_OF_IMAGES >= NUM_OF_TEXTURES)
					return;
				textureFirst += NUM_OF_IMAGES;
				if (textureFirst + NUM_OF_IMAGES >= NUM_OF_TEXTURES)
					pgDnButt.setVisible(false);
				pgUpButt.setVisible(true);
				if (textureFirst + imageSel >= NUM_OF_TEXTURES)
					imageSel = NUM_OF_TEXTURES - textureFirst - 1;
				updateImages(lang);
				break;
			case PLANKBUTT_ID:
				type = PNB.PLANK_ID;
				break;
			case BEAMBUTT_ID:
				type = PNB.BEAM_ID;
				break;
			case LOGBUTT_ID:
				type = PNB.LOG_ID;
				break;
			case PLANK3BUTT_ID:
				type = PNB.PLANKTRI_ID;
				break;
			case WINDOW1BUTT_ID:
				type = PNB.WINDOW1_ID;
				break;
			case WINDOW2BUTT_ID:
				type = PNB.WINDOW2_ID;
				break;
			case WINDOW3BUTT_ID:
				type = PNB.WINDOW3_ID;
				break;
			case WINDOW4BUTT_ID:
				type = PNB.WINDOW4_ID;
				break;
			case MINBUTT_ID:
				quant = 1;
				updateResources(lang);
				break;
			case MINUSBUTT_ID:
				quant--;
				updateResources(lang);
				break;
			case PLUSBUTT_ID:
				quant++;
				updateResources(lang);
				break;
			case MAXBUTT_ID:
				quant = 64;
				updateResources(lang);
				break;
			case DOBUTT_ID:
				int variation = image2variation[textureFirst + imageSel];
				int retVal = PNB.plugin.buy(player, type, variation, quant);
				switch (retVal) {
				case PNB.ERR_NO_RESOURCES:
					player.sendTextMessage(PNB.c.error + PNB.pluginName + ":> " + PNB.c.text + PNB.t.get("PNB_025", lang));
					break;
				// case PNB.ERR_GENERIC: // same as default
				// player.sendTextMessage(Msgs.msg[Msgs.txt_newitem_failed]);
				// break;
				case PNB.ERR_SUCCESS:
					String[] texts = new String[1];
					String typeMsgId = type == PNB.WINDOW4_ID ? "PNB_009"
							: (type == PNB.WINDOW3_ID ? "PNB_008"
									: (type == PNB.WINDOW2_ID ? "PNB_007"
											: (type == PNB.WINDOW1_ID ? "PNB_006"
													: (type == PNB.PLANKTRI_ID ? "PNB_005"
															: (type == PNB.LOG_ID ? "PNB_010"
																	: (type == PNB.BEAM_ID ? "PNB_004"
																			: "PNB_003"))))));
					texts[0] = PNB.t.get("PNB_027", lang).replace("PH_RES_QUANTITY", quant + "")
							.replace("PH_RES_VARIATION", variation + "")
							.replace("PH_RES_OUTPUT", PNB.t.get(typeMsgId, lang));
					push(player, new GuiMessageBox(PNB.plugin, player, PNB.t.get("PNB_000", lang), texts, 0));
					break;
				default:
					player.sendTextMessage(PNB.c.error + PNB.pluginName + ":> " + PNB.c.text + PNB.t.get("PNB_026", lang));
					break;
				}
				break;
			}
		}
	}

	// ********************
	// PRIVATE UTILITY METHODS
	// ********************

	private void updateImages(String lang) {
		if (textureFirst != textureFirstOld) {
			for (int i = 0; i < NUM_OF_IMAGES; i++) {
				if (textureFirst + i >= NUM_OF_TEXTURES) {
					for (int j = i; j < NUM_OF_IMAGES; j++) {
						images[j].setVisible(false);
					}
					break;
				}
				ImageInformation ii;
				ii = new ImageInformation(PNB.pluginPath + "/assets/" + image2texture[textureFirst + i] + ".png");
				images[i].setImage(ii);
				images[i].setVisible(true);
			}
			textureFirstOld = textureFirst;
			updateSelected(lang);
		}
	}

	private void updateSelected(String lang) {
		images[imageSelOld].setBorderThickness(0, false);
		images[imageSel].setBorderThickness(RWGui.BORDER_THICKNESS * 2, false);
		imageSelOld = imageSel;
		updateResources(lang);
	}

	private void updateResources(String lang) {
		if (quant < 1)
			quant = 1;
		minusButt.setVisible(quant > 1);
		if (quant > 64)
			quant = 64;
		plusButt.setVisible(quant < 64);
		quantText.setText(Integer.toString(quant));
		int resIndex = PNB.resourcePerVariation[image2variation[textureFirst + imageSel] - PNB.firstVariation];
		String text = PNB.t.get("PNB_015", lang).replace("PH_RES_NAME", PNB.t.get(resIndex2tIndex[resIndex], lang))
				.replace("PH_RES_COST", free ? "0" : (quant * PNB.costPerItem) + "");

		resourcesText.setText(text);
	}

}
