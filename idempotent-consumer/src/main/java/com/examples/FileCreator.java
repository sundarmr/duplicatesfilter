package com.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

public class FileCreator {
	
	private long noOfRecords;
	private int duplicateInterval;
	private final String fileDirectory="/Users/smunirat/apps/myfile";
	
	
	
	public long getNoOfRecords() {
		return noOfRecords;
	}

	public void setNoOfRecords(long noOfRecords) {
		this.noOfRecords = noOfRecords;
	}

	public int getDuplicateInterval() {
		return duplicateInterval;
	}

	public void setDuplicateInterval(int duplicateInterval) {
		this.duplicateInterval = duplicateInterval;
	}

	public  void createFile() {
	
		try {
			String mainTag = "<orders>";
			String stopTag = "</orders>";
			String orderid=UUID.randomUUID().toString();
			System.out.println(orderid);
			File file = new File(fileDirectory);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			int count=0;
			int i = 0;
			for ( ;i < getNoOfRecords(); i++) {
				if (i == 0) {
					bw.write(mainTag);
				}
				if(getDuplicateInterval()!=0 && i%getDuplicateInterval() ==0){
					count++;
					bw.write("<order><orderid>" +  orderid
							+ "</orderid><products><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product></products><customername>"
							+ RandomStringUtils.randomAlphabetic(9)
							+ "</customername></order>");
				}else{
					bw.write("<order><orderid>" +  UUID.randomUUID().toString()
							+ "</orderid><products><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product><product><productname>"
							+ RandomStringUtils.randomAlphabetic(10)
							+ "</productname><skuid>" + UUID.randomUUID().toString()
							+ "</skuid><price>" + Math.round(Math.random())
							+ "</price></product></products><customername>"
							+ RandomStringUtils.randomAlphabetic(9)
							+ "</customername></order>");
				}
				
				if (i == (getNoOfRecords()-1)) {
					bw.write(stopTag);
				}
			}
			System.out.println(count +" "+i);
			bw.flush();
		
			bw.close();
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
