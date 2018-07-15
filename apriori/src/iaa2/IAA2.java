package iaa2;
import java.util.*;
import java.sql.*;

public class IAA2{

 public static ArrayList<ArrayList<Integer>> array1=new ArrayList<ArrayList<Integer>>();
 public static int min_sup;
    public static void main(String[] args) {
       Connection conn;
        try
        {    
        String q1="select * from Transactions";    
        PreparedStatement stm=MyDataBase.createStatement(q1);
	ResultSet r=stm.executeQuery();
	int i1,j1=0,val=0,flag=0,pos,i,tid=0;
	while(r.next())
	{
		for(int j=1;j<=10;j++)
		{
		if(j==1)
		{ 
		tid=r.getInt(j);
		val=tid;
		}
		else
		val=r.getInt(j);
		for( i=0;i<array1.size();i++)
		{
			//System.out.println("Exception before arraylist");
			if(array1.get(i).get(0)==val)
			{
			flag=1;
			break;
			}			
		}

			if(flag==1)
			{
				System.out.println("From if........");
				array1.get(i).add(tid);
			}
			else
			{		
			System.out.println("From else........");
			array1.add(new ArrayList<Integer>());
			array1.get(j1).add(tid);
			j1++;
			}
		}
		
	}
        r.close();
        stm.close();
        
        System.out.println("here also there is no exception");
         
        
	}
	catch(Exception e){
		System.out.println("Exception while displlaying"+e);
	}	
    }
   
}

