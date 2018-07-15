package iaa2;
import java.util.*;
import java.sql.*;

public class IAA {
 public static ArrayList<ArrayList<Integer>> array=new ArrayList<ArrayList<Integer>>();
 public static ArrayList<ArrayList<Integer>> array1=new ArrayList<ArrayList<Integer>>();
 public static ArrayList<ArrayList<Integer>> l2=new ArrayList<ArrayList<Integer>>();
 public static int min_sup;
    public static void main(String[] args) {
       Connection conn;
 	try
        {    
        String q1="select * from Transactions";    
        PreparedStatement stm=MyDataBase.createStatement(q1);
	ResultSet r=stm.executeQuery();
	ResultSetMetaData rsmd1=r.getMetaData();
	int i1,j1=0;
	int cols1=rsmd1.getColumnCount();
	while(r.next())
	{
		array.add(new ArrayList<Integer>());
		for(i1=1;i1<=cols1;i1++)
		 array.get(j1).add(r.getInt(i1));	
		j1++;
	}
        r.close();
        stm.close();
        System.out.println("here there is no exception");
        String q="select * from TransactionIds";
        PreparedStatement stmt=MyDataBase.createStatement(q);
	ResultSet rs=stmt.executeQuery();
	ResultSetMetaData rsmd=rs.getMetaData();
	int i,j=0;
	int cols=rsmd.getColumnCount();
	while(rs.next())
	{
		array1.add(new ArrayList<Integer>());
		for(i=1;i<=cols;i++)
                {
		 array1.get(j).add(rs.getInt(i));
                 //System.out.println();
                }
		j++;
	}
        rs.close();
        stmt.close();
        System.out.println("here also there is no exception");
         /*Iterator<ArrayList<Integer>> itr = array.iterator();
         while(itr.hasNext()){
         System.out.println(itr.next());
         }*/
         int a[]=new int[6];
            for(int k=0;k<array.size();k++)
            {
                for(int l=0;l<array.get(k).size();l++)
                {
                    a[array.get(k).get(l)]++;
                    System.out.print(array.get(k).get(l)+",");
                }    
                System.out.println();
            } 
        try
        {    
        String drp="drop table l1";
        MyDataBase.executeUpdate(drp);
        }
        catch(Exception e){}
        String s="create table l1(item1 number,count number)";    
        PreparedStatement stmt1=MyDataBase.createStatement(s);
        stmt1.executeUpdate();
        stmt1.close();
        String s1="insert into l1 values(?,?)";
        PreparedStatement ps=MyDataBase.createStatement(s1);
        for(int k=1;k<=5;k++)
        {
            ps.setInt(1,k);
            ps.setInt(2,a[k]);
            System.out.println("hi...."+k+"..."+a[k]);
            ps.executeUpdate();
            System.out.println("bye....");
        }    
        
        int stop=1;
        for(int k=1;k<=3;k++)
        {
         stop=itemsetGeneration(k);
         if(stop==0)
             break;
        }
	}
	catch(Exception e){
		System.out.println("Exception while displlaying"+e);
	}	
    }
    public static int itemsetGeneration(int p)
    {
        int stop=1;
		try
		{
		int count1;
                try
                {
                        String drp1="drop table l"+p;
                        MyDataBase.executeUpdate(drp1);
                }
                catch(Exception e){}
                String enter="";
                for(int i=1;i<=p;i++)
                {
                    enter+="item"+i+" number, ";
                }    
               try
               {
                String q1="create table l"+p+"("+enter+"count number)";
                MyDataBase.executeUpdate(q1);
               }
               catch(Exception e)
               {
                   System.out.println("Exception!!!!!!!!!!!!!!!!!!!1 "+e);
               }
                
                String select="";
                int p1,p2;
                p1=p-1;
                p2=p-2;
                for(int j=1;j<=p1;j++)
                {
                        select+=" t1.item"+j+" , ";
                }    
                select+="t2.item"+p1;
                String condition="";
                for(int k=1;k<=p2;k++)
                {
                         condition+=" t1.item"+k+"=t2.item"+k+" and ";
                }    
                condition+="t1.item"+p1+"<t2.item"+p1;   
                String q1="select "+select+" from l"+p1+" as t1 inner join l"+p1+" as t2 on "+condition;
                 
                PreparedStatement stmt2=MyDataBase.createStatement(q1);
                ResultSet rs1=stmt2.executeQuery();
                String s="";
                for(int i=1;i<=p;i++)
                {
                    s+="?,";
                }    
                s+="?";
                 String q4="insert into l"+p+" values("+s+")";
                 PreparedStatement pstmt2=MyDataBase.createStatement(q4);
		int a[]=new int[p],i,j;  
                int m=0;
                System.out.println("here there is no exception....."+p);
		while(rs1.next())
		{
                        for(i=1;i<=p;i++)
                        {    
			a[i-1]=rs1.getInt(i);
                        }
                         boolean flag=false;
                        if(p==2)
                        {                                 
			count1=getCount(a);                            
                        if(count1>=min_sup) 
                        {    
                            l2.add(new ArrayList<Integer>());
                            for(j=0;j<a.length;j++)
                            {    
                                pstmt2.setInt(j+1,a[j]);
                                l2.get(m).add(a[j]);                     
                            }
                            pstmt2.setInt(j+1,count1); 
                            l2.get(m).add(count1);
                            m++;                            
                            pstmt2.executeUpdate();
                            stop=0;                        
                        }
                        }
                        else
                        {    
                            try
                            {                              
                             for(i=0;i<l2.size();i++)
                             {                                                               
                                    if(l2.get(i).get(0)==a[p-2])
                                    {                                
                                       if(l2.get(i).get(1)==a[p-1])
                                       {    
                                        flag=true;    
                                         break;   
                                       }
                                     }                                                             
                             }
                        if(flag==true)
                        {    
                         count1=getCount(a);
                        if(count1>=min_sup)
                        { 
                        for(i=0;i<a.length;i++)
                        {    
                        pstmt2.setInt(i+1,a[i]);
                        }			
                        pstmt2.setInt(i+1,count1);             
                        pstmt2.executeUpdate();
                        stop=0;
                        }    
                        }
                        }
                         catch(Exception e)
                         {
                               System.out.println("Exeception in itemsetGeneration"+p+e);
                         }    
                        }
                        }
                rs1.close();
                stmt2.close();
                pstmt2.close();
               } catch(Exception e)
                {System.out.println("Exception after condiotin "+e);}
                
          return stop;      
    }
    public static int getCount(int a[])
	{
		int count=0;
		try
		{
                    int available=0;
                    //Iterator<ArrayList<Integer>> itr = array.iterator();                           
                    int flag;
                    for(int k=0;k<array.size();k++)
                    {                  
                     for(int j=0;j<a.length;j++)
                    {
                         available=0;
                        for(int l=0;l<array.get(k).size();l++)
                        {
                            if(a[j]==array.get(k).get(l))
                            {
                                available++;
                                break;
                            }    
                        }
                        if(available!=j+1)
                            break;
                    }
                      if(available==a.length)        
                                    count++;	
                    }                                                                                   
		}
		catch(Exception e)
                {System.out.println("Exception in getCount()"+e);}
		return count;
	}
}


