package me.sabbertran.wastelandutils.deathlog;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLThread implements Runnable
{

    private String command;
    private Statement st;

    public SQLThread(Statement st, String command)
    {
        this.st = st;
        this.command = command;
    }

    @Override
    public void run()
    {
        try
        {
            this.st.execute(this.command);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
