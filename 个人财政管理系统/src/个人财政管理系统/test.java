package 个人财政管理系统;

import javax.swing.*;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class test {
	public static void main(String[] args) {
		//初始化UI界面
		  try { 
			  BeautyEyeLNFHelper.frameBorderStyle =BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			  org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			 
		  } catch(Exception e) { 
			  System.out.println(e);
		  }		  
		 LoginFrame lf=new LoginFrame();
		 lf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}




