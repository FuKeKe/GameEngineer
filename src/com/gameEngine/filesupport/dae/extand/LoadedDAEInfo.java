package com.gameEngine.filesupport.dae.extand;

import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryAnimations;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryAsset;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryCamera;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryControllers;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryEffects;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryGeometries;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryImages;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryLights;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryMaterials;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryScene;
import com.gameEngine.filesupport.dae.extand.libraryNode.LibraryVisualScenes;

public class LoadedDAEInfo {
		//Dae文件中的数据
		public String fileAddress;
		public LibraryAsset libraryAsset;
		public LibraryAnimations libraryAnimations;
		public LibraryCamera libraryCamera;
		public LibraryControllers libraryControllers;
		public LibraryEffects libraryEffects;
		public LibraryGeometries libraryGeometries;
		public LibraryImages libraryImages;
		public LibraryLights libraryLights;
		public LibraryMaterials libraryMaterials;
		public LibraryScene libraryScene;
		public LibraryVisualScenes libraryVisualScenes;
}
