import java.util.ArrayList;

import javax.swing.*;


public class InfoCenter {
    private ArrayList<String> mInfoList;
    private JTextArea txtArea;
    private int curMes;
    
    public InfoCenter(JTextArea jta){
    	txtArea = jta;
    	mInfoList = new ArrayList<String>();
    	curMes = 0;
    	txtArea.setLineWrap(true);
    	txtArea.setWrapStyleWord(true);
    }
	
    public boolean post(String s){
    	if(s!=null){
    		if(curMes == mInfoList.size()-1)
    			curMes++;
    		else
    			curMes =mInfoList.size();
    		mInfoList.add(s);
    		txtArea.setText(s);
    		return true;
    	}
    	return false;
    }
    
    public void reset(){
    	txtArea.setText("");
    	mInfoList = new ArrayList<String>();
    	curMes = 0;   	
    }
    
    public boolean preInfo(){
    	if(curMes>0){
    		curMes--;
    		txtArea.setText(mInfoList.get(curMes));
    		return true;
    	}else if(curMes==0){
    		curMes--;
    		txtArea.setText("");
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean nextInfo(){
    	if(curMes<mInfoList.size()-1){ 	
    		System.out.println(curMes+""+mInfoList.size());
    		curMes++;
    		txtArea.setText(mInfoList.get(curMes));		
    		return true;
    	}else{
    		return false;
    	}
    }
    
}
