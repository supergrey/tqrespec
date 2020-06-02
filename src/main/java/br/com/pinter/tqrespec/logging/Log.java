/*
 * Copyright (C) 2020 Emerson Pinter - All Rights Reserved
 */

/*    This file is part of TQ Respec.

    TQ Respec is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TQ Respec is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TQ Respec.  If not, see <http://www.gnu.org/licenses/>.
*/

package br.com.pinter.tqrespec.logging;

import br.com.pinter.tqrespec.core.State;
import br.com.pinter.tqrespec.core.UnhandledRuntimeException;
import br.com.pinter.tqrespec.util.Constants;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.*;

public class Log {
    private Log() {
    }

    public static System.Logger getLogger(String name) {
        return System.getLogger(name);
    }

    public static FileHandler getLogFileHandler() {
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler(Constants.LOGFILE, false);
            fileHandler.setFormatter(new SimpleFormatter());

        } catch (IOException e) {
            throw new UnhandledRuntimeException(e);
        }
        return fileHandler;
    }

    public static void setupGlobalLogging() {
        Logger rootLogger = LogManager.getLogManager().getLogger("");

        rootLogger.addHandler(getLogFileHandler());
        for (Handler h : rootLogger.getHandlers()) {
            if (h instanceof ConsoleHandler) {
                rootLogger.removeHandler(h);
            }
        }

        if (State.get().getDebugPrefix().containsKey("*")) {
            Level level = State.get().getDebugPrefix().get("*");
            if (level != null) {
                rootLogger.setLevel(level);
            }
        }
        System.setOut(new PrintStream(new OutputStreamLog(Level.INFO)));
        System.setErr(new PrintStream(new OutputStreamLog(Level.SEVERE)));
    }

}
