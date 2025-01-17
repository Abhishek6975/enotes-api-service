/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

public class RemoveDuplicate
{
    public static boolean[] map=new boolean[26];
    public static void removeDuplicate(String str,int size,int idx,String newStr){
       
        if(idx==size){
            System.out.println(newStr);
            return;
        }
        // char curChar = str.charAt(idx);
        if(map[str.charAt(idx) -'a']==true){
            removeDuplicate(str,size,idx+1,newStr);
        }else{
            newStr+=str.charAt(idx);
            map[str.charAt(idx)]=true;
            removeDuplicate(str,size,idx+1,newStr);
        }
    }
  public static void main(String[] args)
  {
    String str="abbccda";
    String newStr="";
    removeDuplicate(str,str.length(),0,newStr);
  }
}
