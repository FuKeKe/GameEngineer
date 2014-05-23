package com.gameEngine.util.dds2bmp;

import android.text.GetChars;

public class PluginDDSFactory {
	private static PluginDDS pluginDDS = new PluginDDS();
	public static PluginDDS getPluginDDS(){
		return pluginDDS;
	}
}
