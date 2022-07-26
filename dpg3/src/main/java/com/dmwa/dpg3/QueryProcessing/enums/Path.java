package com.dmwa.dpg3.QueryProcessing.enums;

import com.dmwa.dpg3.QueryProcessing.UseDatabase;
import com.dmwa.dpg3.utils.CreateRootFolder;

/**
 * @author Pavithra Gunasekaran
 */
public class Path {
   public static final String ROOT = CreateRootFolder.PATH;
   public static final String DBNAME = UseDatabase.getDbName();
//   public static final String DB_PATH = ROOT+DBNAME;

}
