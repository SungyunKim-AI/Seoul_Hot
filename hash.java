package sereve;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
			for(int i=0;i<1507;i++) {
        		br.readLine();
        		continue;
        	} while ((line = br.readLine()) != null) {
            	
            	if(line.equals(",,,,,,"))continue;
            	
            	String[] field = line.split(cvsSplitBy);
            	if(field[1].equals("고유번호"))continue;
            	if(field.length>=7&& field.length<10) {
            		String sql = "Insert into HashTag values(?)";
         			st = conn.prepareStatement(sql);
         			st.setString(1,field[field.length-1]);
         			
         			System.out.println(st);
         			
         			st.executeUpdate();
         			continue;
            	}
            	if(line.split(",\"").length>2) {
           		 int i=0;
           		 String sql = "Insert into HashTag values(?)";
          			st = conn.prepareStatement(sql);
          			field = line.split(",\"")[line.split(",\"").length-1].split(",");
          			st.setString(1,field[0]);
          			
          			System.out.println(st);
          			
          			st.executeUpdate();
          			i++;
                	 for(;!field[i].contains("\"");i+=1) {
                		sql = "Insert into HashTag values(?)";
             			st = conn.prepareStatement(sql);
             			st.setString(1,field[i]);
             			
             			System.out.println(st);
             			
             			st.executeUpdate();
             			
                	 }
                	 sql = "Insert into HashTag values(?)";
          			st = conn.prepareStatement(sql);
          			st.setString(1,field[i].split("\"")[0]);
          			
          			System.out.println(st);
          			
          			st.executeUpdate();
          			continue;
           	 }
            	 int i = 6;
            	 while(!field[i].contains("\"")) {
            		 i++;
            	 }
            	 
            	 String sql = "Insert into HashTag values(?)";
      			st = conn.prepareStatement(sql);
      			st.setString(1,field[i].split("\"")[1]);
      			
      			System.out.println(st);
      			
      			st.executeUpdate();
      			i++;
            	 for(;!field[i].contains("\"");i+=1) {
            		sql = "Insert into HashTag values(?)";
         			st = conn.prepareStatement(sql);
         			st.setString(1,field[i]);
         			
         			System.out.println(st);
         			
         			st.executeUpdate();
         			
            	 }
            	 sql = "Insert into HashTag values(?)";
      			st = conn.prepareStatement(sql);
      			st.setString(1,field[i].split("\"")[0]);
      			
      			System.out.println(st);
      			
      			st.executeUpdate();
               
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
	}
	

}
