package com.cb.platform.yq.base.customsign.service.sign;

import com.cb.platform.yq.base.customsign.entity.SignPrintPDF;

import java.util.Comparator;

public class SignPrintPdfComparator implements Comparator<SignPrintPDF>{

	@Override
	public int compare(SignPrintPDF o1, SignPrintPDF o2) {
		if(o1.getZ_index()>o2.getZ_index()){
			return 1;
		}else{
			return -1;
		}
	}

}
