package egovframework.example.cmmn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.dcn.RowChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;

public class DBNotificationManagerImpl {

    private static DatabaseChangeRegistration dcr = null;
    private static Connection conn = null;
    private static DatabaseChangeListener listener = null;
    
    public static void main(String[] argv) throws Exception
    {
    	DBNotificationManagerImpl dcn = new DBNotificationManagerImpl();
    	dcn.startListening();
    }
//    
//    @Override
//    public void init() throws ServletException {
//        try {
//            startListening();
//            
//            System.out.println("DB listener started.....");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalStateException(e);
//        }
//    }
//    
//    @Override
//    public void destroy() {
//        try {
//            dcr.removeListener(listener);
//            conn.unregisterDatabaseChangeNotification(dcr);
//            conn.close();
//            
//            System.out.println("DB listener destroyed.....");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new IllegalStateException(e);
//        }
//    }
    
    public void startListening() throws Exception {
        conn = getConnection();
        OracleConnection oraConn = (OracleConnection)conn;
        Properties prop = new Properties();
        prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
        dcr = oraConn.registerDatabaseChangeNotification(prop);

        try {
            listener = new DatabaseChangeListener() {
                public void onDatabaseChangeNotification(DatabaseChangeEvent dce) {
                    System.out.println("DB table updated");
                                       		
                    TableChangeDescription[] tc = dce.getTableChangeDescription();
                    for (int i = 0; i < tc.length; i++) {
                    	System.out.println("tc[i].getTableName() :"+tc[i].getTableName());

                    //	System.out.println(tc[i].);
                    	
//Iterator<TableOperation> iter = tc[i].getTableOperations().iterator();  
//while (iter.hasNext())  {
//  System.out.println(iter.next());  
//}  
                        RowChangeDescription[] rcds = tc[i].getRowChangeDescription();
//                        for (int j = 0; j < rcds.length; j++) {
//                      	System.out.println(rcds[j].getRowOperation() + " " + rcds[j].getRowid().stringValue());
//                        }
                  	}
                      synchronized( this ){
                        this.notify();
                      }

                    //List<Job> jobs = getDbData();
                   // for (Job job : jobs) {
                   //     System.out.println("JOB ID: " + job.getJobId() + " : " + job.getJobAvgSal());
                  //  }
                }
            };
            dcr.addListener(listener);
            

            Statement stmt = oraConn.createStatement();
            ((OracleStatement)stmt).setDatabaseChangeRegistration(dcr);
            ResultSet rs = stmt.executeQuery(getNotificationSql());
            
           // ArrayList<Job> jobs = new ArrayList<Job>();
            while (rs.next()) {
              //  Job job = new Job();
              //  job.setJobId(rs.getString("job_id"));
              //  job.setJobAvgSal(rs.getLong("avg_salary"));
              //  jobs.add(job);
                
              //  System.out.println("JOB ID: " + job.getJobId() + " : " + job.getJobAvgSal());
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            if (oraConn != null) {
            	oraConn.unregisterDatabaseChangeNotification(dcr);
            	oraConn.close();
            	conn.close();
            }
            throw ex;
        }
    }
    
//    public List<Job> getDbData() {
//        ArrayList<Job> jobs = new ArrayList<Job>();
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        try {
//            stmt = conn.prepareStatement(getNotificationSql());
//            rs = stmt.executeQuery();
//            while (rs.next()) {
//                Job job = new Job();
//                job.setJobId(rs.getString("job_id"));
//                job.setJobAvgSal(rs.getLong("avg_salary"));
//                jobs.add(job);
//            }         
//        } catch (SQLException e) {
//            throw new IllegalStateException(e);
//        } finally {
//            try {
//                if(rs != null)
//                    rs.close();
//                if(stmt != null)
//                    stmt.close();
//
//            } catch (SQLException e) {}
//        }
//        return jobs;
//    }
    
//    public String getNotificationSql() {
//        return "SELECT job_id," + 
//        "  ROUND(AVG(salary)) AS avg_salary" + 
//        " FROM employees" + 
//        " WHERE job_id = 'IT_PROG'" + 
//        " OR job_id    = 'ST_CLERK'" + 
//        " OR job_id    = 'SA_REP'" + 
//        " OR job_id    = 'SH_CLERK'" + 
//        " OR job_id    = 'FI_ACOUNT'" + 
//        " GROUP BY job_id";
//    }
//    
    public String getNotificationSql() {
        return "SELECT * FROM SAMPLE10 WHERE 1=2";
    }
    public static final Connection getConnection() throws SQLException{
    	OracleDriver dr = new OracleDriver();
        Properties prop = new Properties();
        prop.setProperty("user","rian");
        prop.setProperty("password","rian12345!");
        return (OracleConnection)dr.connect("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=192.168.0.243)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=SKYSOFT)))",prop);

      }
//    public static final OracleConnection getConnection() {
//        InitialContext ic;
//        Connection con = null;
//        try {
//            ic = new InitialContext();
//            DataSource ds = (DataSource) ic.lookup("jdbc/HrDS");
//            con = ds.getConnection();
//
//            return (OracleConnection) con;
//        } catch (NamingException e) {
//           throw new IllegalStateException(e);
//
//        } catch (SQLException e) {
//            throw new IllegalStateException(e);
//        }
//    }
}
