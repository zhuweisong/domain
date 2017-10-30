package com.house.main;

import java.io.IOException;

public class HouseMain {
	
	
	public static void main(String[] args) throws IOException {
		HouseController controller = new HouseController();
		controller.init();
		controller.update();
	}
	
}
