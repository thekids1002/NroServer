package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogHistory {

    /**
     * No logging will be created.
     */
    public static final int NONE = 0;

    /**
     * Level 1 priority (high).
     */
    public static final int ERROR = 1;

    /**
     * Level 2 priority (medium).
     */
    public static final int INFO = 2;

    /**
     * Level 3 priority (low).
     */
    public static final int DEBUG = 3;

    /**
     * String values of priorities
     */
    public static final String[] PRIORITY_STRS = {"NONE", "ERROR", "INFO ", "DEBUG"};

    // Declare some default values
    /**
     * Default priority (if problems reading from the properties file.
     */
    public static final int DEFAULT_CONSOLE_PRIORITY = INFO;

    /**
     * Default value for file output.
     */
    public static final int DEFAULT_FILE_PRIORITY = DEBUG;

    /**
     * Default for showing a priority.
     */
    public static final boolean DEFAULT_SHOW_PRIORITY = false;

    // declare keys are which are stored in the resources strings
    private static final String KEY_LOG_CONSOLE_PRIORITY = "log.priority.console";
    private static final String KEY_LOG_FILE_PRIORITY = "log.priority.file";
    private static final String KEY_LOG_SHOW_PRIORITY = "log.show.priority";

    // name of the log directory
    private static final String LOG_DIRECTORY = "log";

    // dont log constant.
    private static final String DONT_LOG = "-1";

    /**
     * Name of this class.
     */
    protected String className;

    /**
     * Priority of console logging.
     */
    protected int consolePriority;

    /**
     * Priority of file logging.
     */
    protected int filePriority;

    /**
     * If this is true then show the priority of the log as a String.
     */
    protected boolean showPriority;

    // Date formatters (declare once for performance)
    private static final String DATE_FORMAT_DIR = "yyyy_MM";
    private static final String DATE_FORMAT_FILE = "dd_MMM_yyyy";
    private static final String DATE_FORMAT_LOG = "yyyy/MM/dd hh:mm:ss:SSS";
    private SimpleDateFormat dateFormatDir = null;
    private SimpleDateFormat dateFormatFile = null;
    private SimpleDateFormat dateFormatLog = null;

    /**
     * Constructor which takes the class of the logged Class.
     *
     * @param loggedClass
     */
    public LogHistory(Class loggedClass) {
        // Populate the class Name
        String fullClassName = loggedClass.getName();
        this.className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

        // Initilise simple date formatters
        this.dateFormatDir = new SimpleDateFormat(DATE_FORMAT_DIR);
        this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
        this.dateFormatLog = new SimpleDateFormat(DATE_FORMAT_LOG);

        // Set up booleans
        initilise();
    }

    public LogHistory() {
//        String fullClassName = "userItem";
//        this.className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

        // Initilise simple date formatters
        this.dateFormatDir = new SimpleDateFormat(DATE_FORMAT_DIR);
        this.dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
        this.dateFormatLog = new SimpleDateFormat(DATE_FORMAT_LOG);

        // Set up booleans
        initilise();
    }

    /**
     * Log an error message.
     *
     * @param method Method in a class.
     * @param message Message to log.
     */
    public void error(String method, String message) {
        log(ERROR, method, message);
    }

    /**
     * Log an information message.
     *
     * @param method Method in a class.
     * @param message Message to log.
     *
     */
    public void info(String method, String message) {
        log(INFO, method, message);
    }

    /**
     * Log a debug message.
     *
     * @param method Method in a class.
     * @param message Message to log.
     */
    public void debug(String method, String message) {
        log(DEBUG, method, message);
    }

    /**
     * Simple log which will go in at INFO level.
     *
     * @param message Message to log
     */
    public void log(String message) {
        log(INFO, DONT_LOG, message);
    }
    public void log1(String message) {
        log1(NONE, DONT_LOG, message);
    }
    public void log2(String message) {
        log2(NONE, DONT_LOG, message);
    }



    /**
     * Logs a stacktrace.
     *
     * @param e Exception object.
     */
    public void stacktrace(Exception e) {
        error("Exception", e.getMessage());
    }

    /**
     * Intilise the priority level and booleans of where to output the logs.
     */
    private void initilise() {

        // Set defaults
        consolePriority = DEBUG;// DEFAULT_CONSOLE_PRIORITY;
        filePriority = DEBUG; // DEFAULT_FILE_PRIORITY; shutting this off for applets until better solution
        showPriority = DEFAULT_SHOW_PRIORITY;

        // read in priorities for console and file logging
        /*
         * TODO - Change this as there is a seperate server and API now. GameProperties properties = GameProperties.getInstance();
         * 
         * if (properties != null) { consolePriority = properties.getInt (KEY_LOG_CONSOLE_PRIORITY, DEFAULT_CONSOLE_PRIORITY); filePriority = properties.getInt (KEY_LOG_FILE_PRIORITY, DEFAULT_FILE_PRIORITY); showPriority = properties.getBoolean (KEY_LOG_SHOW_PRIORITY, DEFAULT_SHOW_PRIORITY); }
         */
        // Create to see if top level "log" directory is created.
        // FileUtils.createDirectory (LOG_DIRECTORY);
    }

    /**
     * Method which performs the debug.
     *
     * @param logPriority Priority of the log (should be between 1 and 3)
     * @param method Used to show class.method () in log. If this is equal to
     * DONT_LOG then this isnt displayed.
     * @param message Message to log.
     */
    private void log(int logPriority, String method, String message) {

        // Check priority
        // Create log message from priority, time stamp, classs & message
        Calendar calender = Calendar.getInstance();
        Date date = calender.getTime();

        // Create log Strings
        String timeStamp = dateFormatLog.format(date);
        String classMethodStr = "";
        String priorityStr = "";

        if (!method.equals(DONT_LOG)) {
            classMethodStr = className + "." + method + "(): ";
        }

        // Create the priority string if required
        if (showPriority && logPriority > 0 && logPriority < 4 && !method.equals(DONT_LOG)) {
            priorityStr = PRIORITY_STRS[logPriority] + " ";
        }

        String logMessage = "[" + timeStamp + "] "
                + // timestamp
                priorityStr
                + // error e.g. ERROR INFO DEBUG
                classMethodStr
                + // class and method
                message; // actual message

        if (logPriority <= consolePriority) {
            writeToConsole(logMessage);
        }
//        if (logPriority <= filePriority) {
//            writeToFile(logMessage, date);
//        }
    }
    private void log1(int logPriority, String method, String message) {

        // Check priority
        // Create log message from priority, time stamp, classs & message
        Calendar calender = Calendar.getInstance();
        Date date = calender.getTime();

        // Create log Strings
        String timeStamp = dateFormatLog.format(date);
        String classMethodStr = "";
        String priorityStr = "";

        if (!method.equals(DONT_LOG)) {
            classMethodStr = className + "." + method + "(): ";
        }

        // Create the priority string if required
        if (showPriority && logPriority > 0 && logPriority < 4 && !method.equals(DONT_LOG)) {
            priorityStr = PRIORITY_STRS[logPriority] + " ";
        }

        String logMessage = "[" + timeStamp + "] "
                + // timestamp
                priorityStr
                + // error e.g. ERROR INFO DEBUG
                classMethodStr
                + // class and method
                message; // actual message
        writeToFile(logMessage, date);
    }
    private void log2(int logPriority, String method, String message) {

        // Check priority
        // Create log message from priority, time stamp, classs & message
        Calendar calender = Calendar.getInstance();
        Date date = calender.getTime();

        // Create log Strings
        String timeStamp = dateFormatLog.format(date);
        String classMethodStr = "";
        String priorityStr = "";

        if (!method.equals(DONT_LOG)) {
            classMethodStr = className + "." + method + "(): ";
        }

        // Create the priority string if required
        if (showPriority && logPriority > 0 && logPriority < 4 && !method.equals(DONT_LOG)) {
            priorityStr = PRIORITY_STRS[logPriority] + " ";
        }

        String logMessage = "[" + timeStamp + "] "
                + // timestamp
                priorityStr
                + // error e.g. ERROR INFO DEBUG
                classMethodStr
                + // class and method
                message; // actual message
        writeToFile1(logMessage, date);
    }


    /**
     * Log to the console.
     *
     * @param logMessage Log message which is going to the console
     */
    private void writeToConsole(String logMessage) {
        System.out.println(logMessage);
    }

    /**
     * Log to the console.
     *
     * @param logMessage Message to log.
     * @param date Date reference for working out directory names etc.
     */
    private void writeToFile(String logMessage, Date date) {
        // Create to see if top level "log" directory is created.
        createDirectory(LOG_DIRECTORY);

        // Create the name of the directory and file
        char PS = File.separatorChar; // path seperater
        String logDirectory = dateFormatDir.format(date);
        String logFilename = "log_" + dateFormatFile.format(date) + ".txt";

        // Create actual log file
        String fullFileName = LOG_DIRECTORY + PS + logDirectory + PS + logFilename;
        File logFile = new File(fullFileName);

        // Create the log directory if it doesn't exist
        createDirectory(LOG_DIRECTORY + PS + logDirectory);

        try {
            BufferedWriter fileout = null; // declare

            if (logFile.exists()) {
                fileout = new BufferedWriter(new FileWriter(fullFileName, true)); // true = append
                fileout.newLine();
            } else // else create the file
            {
                fileout = new BufferedWriter(new FileWriter(fullFileName));
            }

            // write text to file.
            fileout.write(logMessage);
            fileout.close();
        } catch (IOException ioe) {
            // if error don't worry (do nothing)
        }
    }

    private void writeToFile1(String logMessage, Date date) {
        // Create to see if top level "log" directory is created.
        createDirectory(LOG_DIRECTORY);

        // Create the name of the directory and file
        char PS = File.separatorChar; // path seperater
        String logDirectory = dateFormatDir.format(date);
        String logFilename = "log_" + dateFormatFile.format(date) + ".txt";

        // Create actual log file
        String fullFileName = LOG_DIRECTORY + PS + logDirectory + PS + logFilename;
        File logFile = new File(fullFileName);

        // Create the log directory if it doesn't exist
        createDirectory(LOG_DIRECTORY + PS + logDirectory);

        try {
            BufferedWriter fileout = null; // declare

            if (logFile.exists()) {
                fileout = new BufferedWriter(new FileWriter(fullFileName, true)); // true = append
                fileout.newLine();
            } else // else create the file
            {
                fileout = new BufferedWriter(new FileWriter(fullFileName));
            }

            // write text to file.
            fileout.write(logMessage);
            fileout.close();
        } catch (IOException ioe) {
            // if error don't worry (do nothing)
        }
    }

    public static void createDirectory(String directory) {
        File logDirectory = new File(directory);
        if (!logDirectory.isDirectory()) // creates new directory if it doesn't exist
        {
            logDirectory.mkdir();
        }
    }
}
