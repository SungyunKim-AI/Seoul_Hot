package sereve;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class ds {
	public static void main(String[] args) throws Exception, Exception {
		BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        Connection con = null;
        PreparedStatement st = null;
		//run("C:\\Users\\DGCOM\\Desktop\\Seoul_hot\\Seoul_Hot\\data.csv","euc-kr");
        
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://ksun1234.cafe24.com/ksun1234?characterEncoding=UTF-8&serverTimezone=UTC","ksun1234","kwonsunjae1!");
			Statement stmt = conn.createStatement();
			br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\DGCOM\\Desktop\\Seoul_hot\\Seoul_Hot\\data.csv"),"euc-kr"));
			Statement state = conn.createStatement();
        	 while ((line = br.readLine()) != null) {
            	
            	if(line.equals(",,,,,,"))continue;
            	String fin= "";
            	String[] field = line.split(cvsSplitBy);
            	
            	if(field[1].equals("고유번호")|| field[1].equals("22996"))continue;
				            	if(field.length>=7&& field.length<10) {
				            		int hash_num = 0;
				            		String request = "Select H from HashTag where HASH= '";
				            		request+=field[field.length-1];
				            		request += '\'';
				            		ResultSet rs = state.executeQuery(request);
				            		while(rs.next()) {
				            			hash_num = rs.getInt("H");
				            		}
				            		fin += Integer.toString(hash_num);
				         			
				         			
				         			String sql  = "insert into Connect_hash values(?,?)";
				         			st = conn.prepareStatement(sql);
				         			st.setInt(1,Integer.parseInt(field[1]) );
				         			st.setString(2, fin);
				         			System.out.println(st);
				         			st.executeUpdate();
				         			continue;
				            	}
            	if(line.split(",\"").length>2) {
           		 	int i=0;
           		 	String sql = "Insert into Connect_hash values(?,?)";
           		 	st = conn.prepareStatement(sql);
           		 	st.setInt(1, Integer.parseInt(field[1])); // ID_NUM
      			
           		 	
           		 	field = line.split(",\"")[line.split(",\"").length-1].split(",");// HASHTAG
        		 	int hash_num = 0;
        		 	String request = "Select H from HashTag where HASH= '";
        		 	request+=field[i];
        		 	request += '\'';
        		 	ResultSet rs = state.executeQuery(request);
        		 	while(rs.next()) {
        		 		hash_num = rs.getInt("H");
        		 	}
        		 	fin += Integer.toString(hash_num) + ',';
        		 	
        		 	
        		 
          			i++;
                	 for(;!field[i].contains("\"");i+=1) {
                		 request = "Select H from HashTag where HASH= '";
             		 	request+=field[i];
             		 	request += '\'';
             		 	rs = state.executeQuery(request);
             		 	while(rs.next()) {
             		 		hash_num = rs.getInt("H");
             		 	}
             		 	fin += Integer.toString(hash_num) + ',';
             			
             			//st.executeUpdate();
             			
                	 }
                	request = "Select H from HashTag where HASH= '";
          		 	request+=field[i].split("\"")[0];
          		 	request += '\'';
          		 	rs = state.executeQuery(request);
          		 	while(rs.next()) {
          		 		hash_num = rs.getInt("H");
          		 	}
                	 
                	 fin += Integer.toString(hash_num);
          			st.setString(2,fin);
          			
          			System.out.println(st);
          			
          			st.executeUpdate();
          			continue;
           	 }
            	 int i = 6;
            	 while(!field[i].contains("\"")) {
            		 i++;
            	 }
            	 String sql = "Insert into Connect_hash values(?,?)";
        		 	st = conn.prepareStatement(sql);
        		 	st.setInt(1, Integer.parseInt(field[1])); // ID_NUM
   			
        		 	
        		 	
     		 	int hash_num = 0;
     		 	String request = "Select H from HashTag where HASH= '";
     		 	request+=field[i];
     		 	request += '\'';
     		 	ResultSet rs = state.executeQuery(request);
     		 	while(rs.next()) {
     		 		hash_num = rs.getInt("H");
     		 	}
     		 	fin += Integer.toString(hash_num) + ',';
     		 	
     		 	
     		 
       			i++;
             	 for(;!field[i].contains("\"");i+=1) {
             		 request = "Select H from HashTag where HASH= '";
          		 	request+=field[i];
          		 	request += '\'';
          		 	rs = state.executeQuery(request);
          		 	while(rs.next()) {
          		 		hash_num = rs.getInt("H");
          		 	}
          		 	fin += Integer.toString(hash_num) + ',';
          			
          			//st.executeUpdate();
          			
             	 }
             	request = "Select H from HashTag where HASH= '";
       		 	request+=field[i].split("\"")[0];
       		 	request += '\'';
       		 	rs = state.executeQuery(request);
       		 	while(rs.next()) {
       		 		hash_num = rs.getInt("H");
       		 	}
             	 
             	 fin += Integer.toString(hash_num);
       			st.setString(2,fin);
       			
       			System.out.println(st);
       			
       			st.executeUpdate();
            	 /*String sql = "Insert into HashTag values(?)";
      			st = conn.prepareStatement(sql);
      			st.setString(1,field[i].split("\"")[1]);
      			
      			//System.out.println(st);
      			
      			//st.executeUpdate();
      			i++;
            	 for(;!field[i].contains("\"");i+=1) {
            		sql = "Insert into HashTag values(?)";
         			st = conn.prepareStatement(sql);
         			st.setString(1,field[i]);
         			
         			//System.out.println(st);
         			
         			//st.executeUpdate();
         			
            	 }
            	 sql = "Insert into HashTag values(?)";
      			st = conn.prepareStatement(sql);
      			st.setString(1,field[i].split("\"")[0]);
      			
      			//System.out.println(st);
      			
      			//st.executeUpdate();*/
               
            }
            conn.close();
		}catch( ClassNotFoundException e )
        {
        System.out.println("JDBC 드라이버가 존재하지 않습니다. "+e);}
		catch( java.sql.SQLException e )
		{
       System.out.println("DB 쿼리오류"+e); 
		}           
		catch( Exception e )
		{
			System.out.println("기타 오류 "+e);
		}
		finally { //예외가 있든 없든 무조건 실행
			
		}
	}
	

}
