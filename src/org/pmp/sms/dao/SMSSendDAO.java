/**
 * Author            : Elan
 * Created On        : 2012-5-7 下午04:43:00
 * 
 * Copyright 2012.  All rights reserved. 
 *
 * Revision History
 * 
 *    Date       Modifier       Comments
 * ----------    -------------  --------------------------------------------
 * 
 */
package org.pmp.sms.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


import org.apache.log4j.Logger;
import org.hibernate.jdbc.Work;
import org.pmp.sms.vo.SMSSend;

/**
 * @author Elan
 * @version 1.0
 * @update TODO
 */
public class SMSSendDAO extends BaseDAO {

    //~ Static Fields ==================================================================================================
    private static Logger logger = Logger.getLogger(SMSSendDAO.class.getName());
    
    //~ Methods ========================================================================================================
    public List<SMSSend> loadSmsList_ByIds(List<Integer> ids){
	List<SMSSend> list = null;
	String debugMsg = "load SMSSend list whose ID is in the list";
	String hql = "from SMSSend where smssId in (:ids)";
	try {
	    list = (List<SMSSend>) loadList(hql,ids,debugMsg);
	} catch (RuntimeException e){
	    throw e;
	}
	return list;
    }
    
    public void updateState(final String ids,final String state){
	Work work = new Work(){
            public void execute(Connection connection)throws SQLException{
                String procedure = "{call update_sms_state(?,?) }";
                CallableStatement cstmt = connection.prepareCall(procedure);
                cstmt.setString(1, ids);
                cstmt.setString(2, state);
                cstmt.executeUpdate();
            }
        };
        executeProcedure(work);
    }
}
