package cooksys.daoProject;

import cooksys.daoProject.DB.DBManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DBManager manager = new DBManager();
        
        manager.runTests();
        manager.closeConnection();
    }
}
