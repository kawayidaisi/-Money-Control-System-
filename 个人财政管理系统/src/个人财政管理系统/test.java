package ���˲�������ϵͳ;

import javax.swing.*;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class test {
	public static void main(String[] args) {
		//��ʼ��UI����
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




