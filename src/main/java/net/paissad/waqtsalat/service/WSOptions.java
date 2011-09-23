/*
 * This file is part of WaqtSalat-Service.
 * 
 * WaqtSalat-Service is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WaqtSalat-Service is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WaqtSalat-Service. If not, see <http://www.gnu.org/licenses/>.
 */

package net.paissad.waqtsalat.service;

import static net.paissad.waqtsalat.service.WSConstants.JAVA_VENDOR;
import static net.paissad.waqtsalat.service.WSConstants.JAVA_VERSION;
import static net.paissad.waqtsalat.service.WSConstants.JVM_NAME;
import static net.paissad.waqtsalat.service.WSConstants.OS_ARCH;
import static net.paissad.waqtsalat.service.WSConstants.OS_NAME;
import static net.paissad.waqtsalat.service.WSConstants.OS_VERSION;
import static net.paissad.waqtsalat.service.WSConstants.WS_NAME;
import static net.paissad.waqtsalat.service.WSConstants.WS_VERSION;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.ResourceBundle;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class WSOptions {

    private static final int HELP_WIDTH_SCREEN = 120;

    @Option(name = "-h", aliases = { "--help" }, usage = "options_show_help")
    private boolean          help;

    @Option(name = "-v", aliases = { "--version" }, usage = "options_print_version")
    private boolean          version;

    @Option(name = "-l", aliases = { "--log" }, metaVar = "<log_file>", usage = "options_specify_logfile")
    private File             logfile;

    @Option(name = "-c", aliases = { "--conf" }, metaVar = "<conf_file>", usage = "options_conf_file")
    private File             configFile;

    @Option(name = "--color", usage = "options_colorize_stdout")
    private boolean          color;

    @Option(name = "-g", aliases = { "--debug" }, usage = "options_enable_debug")
    private boolean          debug;

    @Argument
    private List<String>     arguments;

    // _________________________________________________________________________
    // Getters / Setters ...

    /**
     * @return <code>true</code> if the "-h | --help" option is specified,
     *         <code>false</code> otherwise.
     */
    public boolean isHelp() {
        return help;
    }

    /**
     * @return <code>true</code> if the "-v | --version" option is specified,
     *         <code>false</code> otherwise.
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * @return The log file specified by the "-l | --log" option,
     *         <code>null</code> if the option is not used.
     */
    public File getLogfile() {
        return logfile;
    }

    /**
     * @return The configuration file which is specified by the "-c | --conf"
     *         option, <code>null</code> if the option is not used.
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * @return <code>true</code> if the '--color' option is specified,
     *         <code>false</code> otherwise.
     */
    public boolean isColor() {
        return color;
    }

    /**
     * @return <code>true</code> if the '-g | --debug' option is specified,
     *         <code>false</code> otherwise.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @return The list of arguments specified during the application startup,
     *         <code>null</code> if there are not arguments apart from the
     *         specified options which are not considered as arguments !
     */
    public List<String> getArguments() {
        return arguments;
    }

    // _________________________________________________________________________

    /**
     * Prints the usage to STDOUT.
     */
    public void printHelpToStdout() {
        CmdLineParser cmdParser = new CmdLineParser(this);
        cmdParser.setUsageWidth(HELP_WIDTH_SCREEN);
        Writer out = new PrintWriter(System.out);
        ResourceBundle rb = I18N.getResourceBundle();
        cmdParser.printUsage(out, rb);
        cmdParser.printExample(ExampleMode.REQUIRED, rb);
    }

    // _________________________________________________________________________

    /**
     * Prints the version information to STDOUT.
     */
    public void printVersionToStdout() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(WS_NAME).append(" ")
                .append(WS_VERSION).append(" [")
                .append(OS_NAME).append(" - ")
                .append(OS_VERSION).append(" - ")
                .append(OS_ARCH).append("]");
        sb
                .append(" / (")
                .append(JVM_NAME).append("  ")
                .append(JAVA_VENDOR).append("  ")
                .append(JAVA_VERSION)
                .append(")");

        System.out.println(sb.toString());
    }

    // _________________________________________________________________________

}
