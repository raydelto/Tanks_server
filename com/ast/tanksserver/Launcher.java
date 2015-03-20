/**
 * @author Raydelto
 */
package com.ast.tanksserver;

import com.ast.tanksserver.network.Server;

public class Launcher {
	public static void main(String[] args) {
		 Server.getInstance().listen(2222);
	}

}
